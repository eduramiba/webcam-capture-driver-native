package com.github.eduramiba.webcamcapture.drivers.avfoundation.driver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceExtended;
import com.sun.jna.Native;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.eduramiba.webcamcapture.drivers.avfoundation.driver.LibVideoCapture.STATUS_AUTHORIZED;

public class AVFVideoDevice implements WebcamDeviceExtended {
    private static final Logger LOG = LoggerFactory.getLogger(AVFVideoDevice.class);

    private final int deviceIndex;
    private final String id;
    private final String name;
    private final Dimension[] resolutions;
    private Dimension resolution;

    //State:
    private boolean open = false;
    private int bytesPerRow = -1;
    private ByteBuffer imgBuffer = null;
    private byte[] arrayByteBuffer = null;
    private BufferedImage bufferedImage = null;
    private long lastFrameTimestamp = -1;

    public AVFVideoDevice(final int deviceIndex, final String id, final String name, final Collection<Dimension> resolutions) {
        this.deviceIndex = deviceIndex;
        this.id = id;
        this.name = name;
        this.resolutions = resolutions != null ? resolutions.toArray(new Dimension[0]) : new Dimension[0];
        this.resolution = bestResolution(this.resolutions);
    }

    public boolean isValid() {
        return resolution != null && resolution.width > 0 && resolution.height > 0;
    }

    private Dimension bestResolution(final Dimension[] resolutions) {
        Dimension best = null;
        int bestPixels = 0;

        for (Dimension dim : resolutions) {
            int px = dim.width * dim.height;

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
    public void setResolution(Dimension resolution) {
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

        final var lib = LibVideoCapture.INSTANCE;
        final int authStatus = lib.vcavf_has_videocapture_auth();

        if (authStatus != STATUS_AUTHORIZED) {
            LOG.warn("Capture auth status = {}", authStatus);
        }

        if (authStatus != STATUS_AUTHORIZED) {
            lib.vcavf_ask_videocapture_auth();
        }

        final int width = resolution.width;
        final int height = resolution.height;
        final int startResult = lib.vcavf_start_capture(deviceIndex, width, height);

        if (startResult < 0) {
            LOG.warn("Capture start result for device {} = {}", id, startResult);
            return;
        }

    
        this.open = true;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        LOG.info("Device {} opened successfully", id);
    }

    @Override
    public synchronized void close() {
        if (isOpen()) {
            LibVideoCapture.INSTANCE.vcavf_stop_capture(deviceIndex);
            open = false;
            bytesPerRow = -1;
            imgBuffer = null;
            arrayByteBuffer = null;
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

    public static final int MAX_FPS = 30;

    @Override
    public double getFPS() {
        //TODO: Use actual FPS declared by stream
        return MAX_FPS;
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

        if (target.remaining() < imgBuffer.capacity()) {
            LOG.error("At least {} bytes needed but passed buffer has only {} remaining size", imgBuffer.capacity(), target.capacity());
            return;
        }

        updateBuffer();
        imgBuffer.rewind();
        target.put(imgBuffer);
    }

    @Override
    public String toString() {
        return "AVFVideoDevice{" +
            "deviceIndex=" + deviceIndex +
            ", id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", resolutions=" + Arrays.toString(resolutions) +
            '}';
    }

    @Override
    public synchronized BufferedImage getImage(ByteBuffer byteBuffer) {
        if (!isOpen()) {
            return null;
        }

        updateBuffer();
        updateBufferedImage();

        return bufferedImage;
    }

    @Override
    public boolean updateFXIMage(WritableImage writableImage) {
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
        
        final int videoWidth = resolution.width;
        final int videoHeight = resolution.height;
        
        final PixelWriter pw = writableImage.getPixelWriter();
        
        imgBuffer.mark();
        imgBuffer.position(0);
        pw.setPixels(
            0, 0, videoWidth, videoHeight,
            PixelFormat.getByteRgbInstance(), imgBuffer, bytesPerRow
        );
            
        return true;
    }

    private void updateBuffer() {
        if (LibVideoCapture.INSTANCE.vcavf_has_new_frame(deviceIndex)) {
            if (imgBuffer == null) {
                // Init buffer if still not initialized:
                this.bytesPerRow = LibVideoCapture.INSTANCE.vcavf_frame_bytes_per_row(deviceIndex);

                final var bufferSizeBytes = bytesPerRow * resolution.height;
                this.imgBuffer = ByteBuffer.allocateDirect(bufferSizeBytes);
                this.arrayByteBuffer = new byte[imgBuffer.capacity()];
            }

            if (LibVideoCapture.INSTANCE.vcavf_grab_frame(
                deviceIndex,
                Native.getDirectBufferPointer(imgBuffer), imgBuffer.capacity())) {
                lastFrameTimestamp = System.currentTimeMillis();
            };
        }
    }

    private void updateBufferedImage() {
        if (!isOpen() || imgBuffer == null) {
            return;
        }

        final int videoWidth = resolution.width;
        final int videoHeight = resolution.height;

        final ComponentSampleModel sampleModel = new ComponentSampleModel(
            DataBuffer.TYPE_BYTE, videoWidth, videoHeight, 3, bytesPerRow,
            new int[]{0, 1, 2}
        );

        imgBuffer.mark();
        imgBuffer.position(0);
        imgBuffer.get(arrayByteBuffer, 0, imgBuffer.capacity());
        imgBuffer.reset();

        final DataBuffer dataBuffer = new DataBufferByte(arrayByteBuffer, arrayByteBuffer.length);
        final Raster raster = Raster.createRaster(sampleModel, dataBuffer, null);
        bufferedImage.setData(raster);
    }
}
