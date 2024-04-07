package com.github.eduramiba.webcamcapture.drivers.capturemanager;

import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceExtended;
import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceWithCustomEvents;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.eduramiba.webcamcapture.utils.Pair;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

import com.github.eduramiba.webcamcapture.utils.Utils;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureManagerVideoDevice implements WebcamDeviceExtended {

    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerVideoDevice.class);

    private final CaptureManagerSource source;
    private final List<CaptureManagerSinkFactory> sinksFactories;
    private final Dimension[] resolutions;
    private Dimension resolution;

    private final LinkedHashSet<WebcamDeviceWithCustomEvents.Listener> listeners = new LinkedHashSet<>();

    private CaptureManagerFrameGrabberSession session = null;

    private long t1 = -1;
    private long t2 = -1;

    private volatile long fps = 0;

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
            if (t1 == -1 || t2 == -1) {
                t1 = System.currentTimeMillis();
                t2 = System.currentTimeMillis();
            }

            BufferedImage image = session.toBufferedImage();

            t1 = t2;
            t2 = System.currentTimeMillis();
            fps = (4 * fps + 1000 / (t2 - t1 + 1)) / 5;

            return image;
        }
        return null;
    }

    @Override
    public void open() {
        if (isOpen()) {
            return;
        }

        session = new CaptureManagerFrameGrabberSession();

        session.setCustomEventListener(this::customEventReceived);

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
        if (resolution == null) {
            return null;
        }

        final List<CaptureManagerMediaType> mediaTypesForResolution = new ArrayList<>();

        for (CaptureManagerMediaType mediaType : mediaTypes) {
            if (mediaType == null) {
                continue;
            }

            if (resolution.width != mediaType.getWidth() || resolution.height != mediaType.getHeight()) {
                continue;
            }

            mediaTypesForResolution.add(mediaType);
        }

        LOG.info("Media types for resolution {}x{} = {}", resolution.width, resolution.height, mediaTypesForResolution);

        if (mediaTypesForResolution.isEmpty()) {
            return null;
        }

        //Prefer NV12 (decodes faster with GPU support), then YUV and MJPG:
        return Utils.coalesce(
                findMediaTypeContaining(mediaTypesForResolution, "NV12"),
                findMediaTypeContaining(mediaTypesForResolution, "YUV"),
                findMediaTypeContaining(mediaTypesForResolution, "MJPG"),
                mediaTypesForResolution.get(0)
        );
    }

    private static CaptureManagerMediaType findMediaTypeContaining(final Collection<CaptureManagerMediaType> mediaTypes, final String subType) {
        for (CaptureManagerMediaType mediaType : mediaTypes) {
            if(mediaType.getSubType().contains(subType)) {
                return mediaType;
            }
        }

        return null;
    }

    @Override
    public double getFPS() {
        return fps;
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

    @Override
    public void addCustomEventsListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeCustomEventsListener(Listener listener) {
        return listeners.remove(listener);
    }

    public void customEventReceived(String eventType) {
        for (Listener l: listeners) {
            l.customEventReceived(eventType, eventType);
        }
    }
}
