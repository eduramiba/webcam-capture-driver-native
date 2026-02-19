package com.github.eduramiba.webcamcapture.drivers.directshow;

import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceExtended;
import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceWithBufferOperations.RawFramePixelFormat;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.eduramiba.webcamcapture.drivers.directshow.LibCdshow.CDS_OK;

public class CdshowVideoDevice implements WebcamDeviceExtended {
    private static final Logger LOG = LoggerFactory.getLogger(CdshowVideoDevice.class);

    private static final String BUTTON_PRESSED_EVENT_TYPE = "SnapTrigger";
    private static final int RGB32_BYTES_PER_PIXEL = RawFramePixelFormat.BYTE_RGB32.getBytesPerPixel();

    private final int deviceIndex;
    private final String id;
    private final String name;
    private final Dimension[] resolutions;
    private Dimension resolution;
    private final int maxFps;
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();

    // State:
    private boolean open = false;
    private int bytesPerRow = -1;
    private ByteBuffer imgBuffer = null;
    private byte[] frameByteBuffer = null;
    private BufferedImage bufferedImage = null;
    private long lastFrameTimestamp = -1;

    public CdshowVideoDevice(final int deviceIndex, final String id, final String name, final Collection<Dimension> resolutions, final int maxFps) {
        this.deviceIndex = deviceIndex;
        this.id = id;
        this.name = name;
        this.resolutions = resolutions != null ? resolutions.toArray(new Dimension[0]) : new Dimension[0];
        this.resolution = bestResolution(this.resolutions);
        this.maxFps = maxFps;
    }

    public boolean isValid() {
        return resolution != null && resolution.width > 0 && resolution.height > 0;
    }

    private Dimension bestResolution(final Dimension[] availableResolutions) {
        Dimension best = null;
        int bestPixels = 0;

        for (Dimension dim : availableResolutions) {
            final int px = dim.width * dim.height;
            if (px > bestPixels) {
                best = dim;
                bestPixels = px;
            }
        }

        return best;
    }

    public int getDeviceIndex() {
        return deviceIndex;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Dimension[] getResolutions() {
        return resolutions;
    }

    @Override
    public Dimension getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(final Dimension resolution) {
        if (isOpen()) {
            return;
        }

        this.resolution = resolution;
    }

    @Override
    public BufferedImage getImage() {
        return getImage(imgBuffer);
    }

    @Override
    public synchronized void open() {
        if (isOpen()) {
            return;
        }

        final int width = resolution.width;
        final int height = resolution.height;
        final int startResult = LibCdshow.INSTANCE.cds_start_capture(deviceIndex, width, height);

        if (startResult < CDS_OK) {
            LOG.error("Error starting DirectShow capture for device {} = {}", id, startResult);
            return;
        }

        this.open = true;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        LOG.info("DirectShow device {} opened successfully", id);
    }

    @Override
    public synchronized void close() {
        if (isOpen()) {
            LibCdshow.INSTANCE.cds_stop_capture(deviceIndex);
            open = false;
            bytesPerRow = -1;
            imgBuffer = null;
            frameByteBuffer = null;
            bufferedImage = null;
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public long getLastFrameTimestamp() {
        return lastFrameTimestamp;
    }

    @Override
    public double getFPS() {
        return maxFps;
    }

    @Override
    public synchronized ByteBuffer getImageBytes() {
        if (!isOpen()) {
            return null;
        }

        updateBuffer();
        return imgBuffer;
    }

    @Override
    public synchronized void getImageBytes(final ByteBuffer target) {
        if (!isOpen()) {
            return;
        }

        updateBuffer();
        if (imgBuffer == null) {
            return;
        }

        if (target.remaining() < imgBuffer.capacity()) {
            LOG.error("At least {} bytes needed but passed buffer has only {} remaining size", imgBuffer.capacity(), target.capacity());
            return;
        }

        imgBuffer.rewind();
        target.put(imgBuffer);
    }

    @Override
    public String toString() {
        return "CdshowVideoDevice{" +
            "deviceIndex=" + deviceIndex +
            ", id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", resolutions=" + Arrays.toString(resolutions) +
            '}';
    }

    @Override
    public synchronized BufferedImage getImage(final ByteBuffer byteBuffer) {
        if (!isOpen()) {
            return null;
        }

        updateBuffer();
        updateBufferedImage();

        return bufferedImage;
    }

    @Override
    public synchronized boolean updateFXIMage(final WritableImage writableImage) {
        return updateFXIMage(writableImage, -1);
    }

    @Override
    public synchronized boolean updateFXIMage(final WritableImage writableImage, final long lastFrameTimestamp) {
        if (!isOpen()) {
            return false;
        }

        updateBuffer();

        if (imgBuffer == null) {
            return false;
        }

        if (this.lastFrameTimestamp <= lastFrameTimestamp) {
            return false;
        }

        final int videoWidth = bufferedImage != null ? bufferedImage.getWidth() : resolution.width;
        final int videoHeight = bufferedImage != null ? bufferedImage.getHeight() : resolution.height;
        final PixelWriter pixelWriter = writableImage.getPixelWriter();
        final int effectiveBytesPerRow = bytesPerRow > 0 ? bytesPerRow : videoWidth * RGB32_BYTES_PER_PIXEL;
        if (effectiveBytesPerRow < videoWidth * RGB32_BYTES_PER_PIXEL) {
            LOG.error("Invalid RGB32 stride for device {}. bytesPerRow={}, width={}", id, effectiveBytesPerRow, videoWidth);
            return false;
        }
        final ByteBuffer readBuffer = imgBuffer.asReadOnlyBuffer().position(0);

        pixelWriter.setPixels(
            0, 0, videoWidth, videoHeight,
            PixelFormat.getByteBgraPreInstance(), readBuffer, effectiveBytesPerRow
        );

        return true;
    }

    @Override
    public synchronized RawFramePixelFormat getRawFramePixelFormat() {
        return RawFramePixelFormat.BYTE_RGB32;
    }

    @Override
    public synchronized int getRawFrameBytesPerRow() {
        if (!isOpen()) {
            return -1;
        }
        if (bytesPerRow <= 0) {
            updateBuffer();
        }
        if (bytesPerRow > 0) {
            return bytesPerRow;
        }
        return resolution != null ? resolution.width * getRawFrameBytesPerPixel() : -1;
    }

    private void updateBuffer() {
        if (imgBuffer == null) {
            final int hasFirstFrameResult = LibCdshow.INSTANCE.cds_has_first_frame(deviceIndex);
            if (hasFirstFrameResult > 0) {
                this.bytesPerRow = LibCdshow.INSTANCE.cds_frame_bytes_per_row(deviceIndex);

                int frameWidth = LibCdshow.INSTANCE.cds_frame_width(deviceIndex);
                int frameHeight = LibCdshow.INSTANCE.cds_frame_height(deviceIndex);

                if (frameWidth <= 0 || frameHeight <= 0) {
                    frameWidth = resolution.width;
                    frameHeight = resolution.height;
                }

                final int bufferSizeBytes = bytesPerRow * frameHeight;
                this.imgBuffer = ByteBuffer.allocateDirect(bufferSizeBytes);
                this.frameByteBuffer = new byte[imgBuffer.capacity()];
                this.bufferedImage = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_BGR);

                doGrab();
            } else if (hasFirstFrameResult < 0) {
                LOG.error("Error checking first frame availability = {}", hasFirstFrameResult);
            }
        } else {
            doGrab();
        }
    }

    private synchronized void doGrab() {
        final int grabResult = LibCdshow.INSTANCE.cds_grab_frame(
            deviceIndex,
            Native.getDirectBufferPointer(imgBuffer),
            new NativeLong(imgBuffer.capacity())
        );

        if (grabResult == CDS_OK) {
            lastFrameTimestamp = System.currentTimeMillis();
            checkButtonPressEvent();
        } else {
            LOG.error("Error grabbing DirectShow frame = {}", grabResult);
        }
    }

    private void checkButtonPressEvent() {
        final int buttonPressed = LibCdshow.INSTANCE.cds_button_pressed(deviceIndex);
        if (buttonPressed == 1) {
            final long timestamp100ns = LibCdshow.INSTANCE.cds_button_timestamp(deviceIndex);
            for (Listener listener : listeners) {
                listener.customEventReceived(BUTTON_PRESSED_EVENT_TYPE, timestamp100ns);
            }
        }
    }

    private void updateBufferedImage() {
        if (!isOpen() || imgBuffer == null) {
            return;
        }

        final int videoWidth = bufferedImage.getWidth();
        final int videoHeight = bufferedImage.getHeight();
        final int effectiveBytesPerRow = bytesPerRow > 0 ? bytesPerRow : videoWidth * RGB32_BYTES_PER_PIXEL;
        if (effectiveBytesPerRow < videoWidth * RGB32_BYTES_PER_PIXEL) {
            LOG.error("Invalid RGB32 stride for device {}. bytesPerRow={}, width={}", id, effectiveBytesPerRow, videoWidth);
            return;
        }

        final int expectedSize = effectiveBytesPerRow * videoHeight;
        if (imgBuffer.capacity() < expectedSize) {
            LOG.error("Invalid RGB32 buffer size for device {}. capacity={}, expected={}", id, imgBuffer.capacity(), expectedSize);
            return;
        }
        if (frameByteBuffer == null || frameByteBuffer.length != imgBuffer.capacity()) {
            frameByteBuffer = new byte[imgBuffer.capacity()];
        }

        final ByteBuffer readBuffer = imgBuffer.asReadOnlyBuffer().position(0);
        readBuffer.get(frameByteBuffer, 0, frameByteBuffer.length);

        final ComponentSampleModel sampleModel = new ComponentSampleModel(
            DataBuffer.TYPE_BYTE, videoWidth, videoHeight, RGB32_BYTES_PER_PIXEL, effectiveBytesPerRow,
            new int[]{2, 1, 0}
        );

        final DataBuffer dataBuffer = new DataBufferByte(frameByteBuffer, frameByteBuffer.length);
        final Raster raster = Raster.createRaster(sampleModel, dataBuffer, null);
        bufferedImage.setData(raster);
    }

    @Override
    public void addCustomEventsListener(final Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeCustomEventsListener(final Listener listener) {
        return listeners.remove(listener);
    }
}
