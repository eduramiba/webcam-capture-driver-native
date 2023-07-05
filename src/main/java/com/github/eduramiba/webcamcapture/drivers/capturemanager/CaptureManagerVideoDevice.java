package com.github.eduramiba.webcamcapture.drivers.capturemanager;

import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceExtended;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.eduramiba.webcamcapture.utils.Pair;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureManagerVideoDevice implements WebcamDeviceExtended {

    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerVideoDevice.class);

    private final CaptureManagerSource source;
    private final List<CaptureManagerSinkFactory> sinksFactories;
    private final Dimension[] resolutions;
    private Dimension resolution;

    private CaptureManagerFrameGrabberSession session = null;

    public CaptureManagerVideoDevice(CaptureManagerSource source, List<CaptureManagerSinkFactory> sinksFactories) {
        this.source = Objects.requireNonNull(source, "source");
        this.sinksFactories = Objects.requireNonNull(sinksFactories, "sinksFactories");

        this.resolutions = source.getStreamDescriptors()
                .stream()
                .flatMap(s -> s.getMediaTypes().stream())
                .map(m -> new Dimension(m.getWidth(), m.getHeight()))
                .distinct()
                .toArray(Dimension[]::new);

        this.resolution = bestResolution(resolutions);
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

    @Override
    public String getId() {
        return source.getSymbolicLink();
    }

    @Override
    public String getName() {
        return source.getFriendlyName();
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
        this.resolution = resolution;
    }

    @Override
    public BufferedImage getImage() {
        if (isOpen()) {
            return session.toBufferedImage();
        }
        return null;
    }

    @Override
    public void open() {
        if (isOpen()) {
            return;
        }

        session = new CaptureManagerFrameGrabberSession();

        final Pair<CaptureManagerStreamDescriptor, CaptureManagerMediaType> best = findBestMediaTypeInStreams(
                source.getStreamDescriptors(),
                resolution
        );

        if (best != null) {
            final CaptureManagerStreamDescriptor stream = best.getLeft();
            final CaptureManagerMediaType mediaType = best.getRight();

            LOG.info("Using video media type: {}", mediaType);
            session.init(
                    source,
                    stream,
                    mediaType,
                    sinksFactories
            );
            session.start();
        } else {
            LOG.warn("Could not find best stream and mediaType for source = {} and resolution = {}", source, resolution);
        }
    }

    @Override
    public void close() {
        if (session != null && session.isOpen()) {
            session.stop();
        }
    }

    @Override
    public void dispose() {
        session = null;
    }

    @Override
    public boolean isOpen() {
        return session != null && session.isOpen();
    }

    @Override
    public long getLastFrameTimestamp() {
        if (isOpen()) {
            return session.getLastFrameTimestamp();
        }

        return -1;
    }

    private static Pair<CaptureManagerStreamDescriptor, CaptureManagerMediaType> findBestMediaTypeInStreams(
            final Collection<CaptureManagerStreamDescriptor> videoStreams,
            final Dimension resolution
    ) {
        CaptureManagerStreamDescriptor bestStream = null;
        CaptureManagerMediaType bestMediaType = null;

        for (CaptureManagerStreamDescriptor stream : videoStreams) {
            final CaptureManagerMediaType streamBestMediaType = findBestMediaType(stream, resolution);
            final CaptureManagerMediaType newBestMediaType = findBestMediaTypeInList(Arrays.asList(bestMediaType, streamBestMediaType), resolution);

            if (newBestMediaType == streamBestMediaType) {
                bestStream = stream;
            }

            bestMediaType = newBestMediaType;
        }

        if (bestStream != null && bestMediaType != null) {
            return Pair.of(bestStream, bestMediaType);
        } else {
            return null;
        }
    }

    private static CaptureManagerMediaType findBestMediaType(final CaptureManagerStreamDescriptor videoStream, final Dimension resolution) {
        if (videoStream == null || !videoStream.isVideoStream()) {
            return null;
        }

        return findBestMediaTypeInList(videoStream.getMediaTypes(), resolution);
    }

    private static CaptureManagerMediaType findBestMediaTypeInList(final Collection<CaptureManagerMediaType> mediaTypes, final Dimension resolution) {
        int maxPixels = 0;
        CaptureManagerMediaType bestMediaType = null;

        for (CaptureManagerMediaType mediaType : mediaTypes) {
            if (mediaType == null) {
                continue;
            }

            if (resolution != null && (resolution.width != mediaType.getWidth() || resolution.height != mediaType.getHeight())) {
                continue;
            }

            final int pixels = mediaType.getWidth() * mediaType.getHeight();

            if (pixels > maxPixels) {
                maxPixels = pixels;
                bestMediaType = mediaType;
            } else if (pixels == maxPixels && mediaType.getSubType().contains("NV12")) {//Prefer NV12
                bestMediaType = mediaType;
            } else if (pixels == maxPixels && (mediaType.getSubType().contains("YUV") || mediaType.getSubType().contains("YUY"))) {//Prefer YUV/YUY if no NV12
                bestMediaType = mediaType;
            }
        }

        return bestMediaType;
    }

    public static final int MAX_FPS = 30;

    @Override
    public double getFPS() {
        //TODO: Use actual FPS declared by stream
        return MAX_FPS;
    }

    @Override
    public ByteBuffer getImageBytes() {
        if (!isOpen()) {
            return null;
        }

        session.updateDirectBuffer();
        return session.getDirectBuffer();
    }

    @Override
    public void getImageBytes(final ByteBuffer byteBuffer) {
        if (isOpen()) {
            session.getImageBytes(byteBuffer);
        }
    }

    @Override
    public boolean updateFXIMage(final WritableImage writableImage, final long lastFrameTimestamp) {
        if (isOpen()) {
            session.updateFXIMage(writableImage, lastFrameTimestamp);
            return true;
        }

        return false;
    }

    @Override
    public boolean updateFXIMage(WritableImage writableImage) {
        return updateFXIMage(writableImage, -1);
    }

    @Override
    public String toString() {
        return source.toString();
    }

    @Override
    public BufferedImage getImage(ByteBuffer byteBuffer) {
        if (!isOpen()) {
            return null;
        }

        return session.toBufferedImage(byteBuffer);
    }
}
