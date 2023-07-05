package com.github.eduramiba.webcamcapture.drivers.capturemanager;

import capturemanager.classes.CaptureManager;
import capturemanager.interfaces.*;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.SinkValuePart;
import com.github.eduramiba.webcamcapture.utils.Utils;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureManagerFrameGrabberSession {

    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerFrameGrabberSession.class);

    private static final String MFMediaType_Video = "{73646976-0000-0010-8000-00AA00389B71}";
    private static final String MFVideoFormat_RGB32 = "{00000016-0000-0010-8000-00AA00389B71}";

    private ByteBuffer directBuffer = null;
    private byte[] arrayByteBuffer = null;
    private BufferedImage bufferedImage = null;

    private ISampleGrabberCall sampleGrabberCall = null;

    private SampleModel sampleModel = null;

    private ISession session = null;
    private int videoWidth = -1;
    private int videoHeight = -1;
    private int bytesPerRow = -1;
    private int bufferSizeBytes = -1;
    private long lastFrameTimestamp = -1;

    public boolean init(
            final CaptureManagerSource source,
            final CaptureManagerStreamDescriptor stream,
            final CaptureManagerMediaType mediaType,
            final List<CaptureManagerSinkFactory> sinkFactories
    ) {
        final int streamIndex = source.getStreamDescriptors().indexOf(stream);

        if (streamIndex < 0) {
            LOG.error("Could not find stream = {} in source = {}", stream, source);
            return false;
        }

        final int mediaTypeIndex = stream.getMediaTypes().indexOf(mediaType);

        if (mediaTypeIndex < 0) {
            LOG.error("Could not find media type = {} in stream {}", mediaType, stream);
            return false;
        }

        final ISourceControl sourceControl = CaptureManager.getInstance().getICaptureManagerControl().createSourceControl();

        if (sourceControl == null) {
            LOG.error("Could not create source control");
            return false;
        }

        final ISinkControl sinkControl = CaptureManager.getInstance().getICaptureManagerControl().createSinkControl();

        if (sinkControl == null) {
            LOG.error("Could not create sink control");
            return false;
        }

        final ISessionControl sessionControl = CaptureManager.getInstance().getICaptureManagerControl().createSessionControl();

        if (sessionControl == null) {
            LOG.error("Could not create session control");
            return false;
        }

        final CaptureManagerSinkFactory sinkFactory = sinkFactories.stream()
                .filter(
                        s ->
                                s.getGuid().equalsIgnoreCase("{759D24FF-C5D6-4B65-8DDF-8A2B2BECDE39}")
                                        || Utils.containsIgnoreCase(s.getName(), "SampleGrabberCallSinkFactory")
                )
                .findFirst()
                .orElse(null);

        if (sinkFactory == null) {
            LOG.error("Could not find SampleGrabberCallSinkFactory");
            return false;
        }

        final SinkValuePart sinkValuePart = Utils.coalesce(
                findSinkValuePart(sinkFactory, "ASYNC"),
                findSinkValuePart(sinkFactory, "SYNC"),
                findSinkValuePart(sinkFactory, null)
        );

        if (sinkValuePart == null) {
            LOG.error("Could not find sinkValuePart in sink factory = {}", sinkFactory);
        }

        final String sinkGUID = sinkFactory.getValueParts().stream().findFirst()
                .map(SinkValuePart::getGuid)
                .orElse(null);

        if (Utils.isBlank(sinkGUID)) {
            LOG.error("Could not find valid value part GUID in sink factory = {}", sinkFactory);
        }

        final ISampleGrabberCallSinkFactory sampleGrabberCallSinkFactory = sinkControl.createSampleGrabberCallSinkFactory(
                sinkGUID
        );

        if (sampleGrabberCallSinkFactory == null) {
            LOG.error("Could not create ISampleGrabberCallSinkFactory");
            return false;
        }

        this.videoWidth = mediaType.getWidth();
        this.videoHeight = mediaType.getHeight();

        final String videoFormat = MFVideoFormat_RGB32;

        this.bytesPerRow = Math.abs(CaptureManager.getInstance().getICaptureManagerControl().getStrideForBitmapInfoHeader(
                videoFormat,
                videoWidth
        ));

        bufferSizeBytes = bytesPerRow * videoHeight;

        directBuffer = ByteBuffer.allocateDirect(bufferSizeBytes);
        arrayByteBuffer = new byte[bufferSizeBytes];

        bufferedImage = new BufferedImage(videoWidth, videoHeight, BufferedImage.TYPE_INT_BGR);

        sampleModel = new ComponentSampleModel(
                DataBuffer.TYPE_BYTE, videoWidth, videoHeight, 4, bytesPerRow,
                new int[]{2, 1, 0} // Try {1,2,3}, {3,2,1}, {0,1,2}
        );

        sampleGrabberCall = sampleGrabberCallSinkFactory.createOutputNode(
                MFMediaType_Video,
                videoFormat,
                bufferSizeBytes
        );


        if (sampleGrabberCall == null) {
            LOG.error("Could not create ISampleGrabberCall");
            return false;
        }
        final IStreamNode streamNode = sampleGrabberCall.getStreamNode();

        if (streamNode == null) {
            LOG.error("Could not create streamNode");
            return false;
        }

        final IStreamNode sourceNode = sourceControl.createSourceNode(
                source.getSymbolicLink(),
                streamIndex,
                mediaTypeIndex,
                streamNode
        );

        if (sourceNode == null) {
            LOG.error("Could not create sourceNode");
            return false;
        }

        final List<IStreamNode> lArrayPtrSourceNodesOfTopology = new ArrayList<>();

        lArrayPtrSourceNodesOfTopology.add(sourceNode);

        session = sessionControl.createSession(
                lArrayPtrSourceNodesOfTopology
        );

        if (session == null) {
            LOG.error("Could not create session control last step");
            return false;
        }

        session.addUpdateStateListener(new IUpdateStateListener() {
            @Override
            public void invoke(int aCallEventCode, int aSessionDescriptor) {
                LOG.info("invoke with (aCallEventCode, aSessionDescriptor) = ({}, {})", aCallEventCode, aSessionDescriptor);
            }
        });

        LOG.info("Successfully created CallSessionControl");

        return true;
    }

    private SinkValuePart findSinkValuePart(final CaptureManagerSinkFactory sinkFactory, final String name) {
        return sinkFactory.getValueParts().stream()
                .filter(s -> name == null || s.getValue().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public synchronized void start() {
        if (session == null) {
            LOG.error("Call init before start!!");
            return;
        }

        session.startSession(0, "{00000000-0000-0000-0000-000000000000}");
    }

    public synchronized void stop() {
        if (session == null) {
            return;
        }

        session.stopSession();

        session.closeSession();
    }

    public long getLastFrameTimestamp() {
        return lastFrameTimestamp;
    }

    public synchronized BufferedImage toBufferedImage() {
        if (!isOpen()) {
            return null;
        }

        updateDirectBuffer();
        return toBufferedImage(directBuffer);
    }

    public synchronized BufferedImage toBufferedImage(final ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }

        byteBuffer.mark();
        byteBuffer.position(0);
        byteBuffer.get(arrayByteBuffer, 0, byteBuffer.capacity());
        byteBuffer.reset();

        final DataBuffer dataBuffer = new DataBufferByte(arrayByteBuffer, arrayByteBuffer.length);
        final Raster raster = Raster.createRaster(sampleModel, dataBuffer, null);
        bufferedImage.setData(raster);

        return bufferedImage;
    }

    public synchronized boolean updateFXIMage(final WritableImage writableImage, long lastFrameTimestamp) {
        if (!isOpen()) {
            return false;
        }

        final int sizeData = updateDirectBuffer();

        if (sizeData <= 0) {
            return false;
        }

        if (this.lastFrameTimestamp <= lastFrameTimestamp) {
            return false;
        }

        updateFXIMage(writableImage, directBuffer);

        return true;
    }

    public synchronized void updateFXIMage(final WritableImage writableImage, final ByteBuffer byteBuffer) {
        final PixelWriter pw = writableImage.getPixelWriter();

        byteBuffer.mark();
        byteBuffer.position(0);
        pw.setPixels(0, 0, videoWidth, videoHeight, PixelFormat.getByteBgraPreInstance(), byteBuffer, bytesPerRow);
        byteBuffer.reset();
    }

    public synchronized ByteBuffer getDirectBuffer() {
        if (!isOpen()) {
            return null;
        }

        directBuffer.position(0);
        return directBuffer;
    }


    public synchronized int updateDirectBuffer() {
        final int readSize = sampleGrabberCall.readData(directBuffer);
        if (readSize > 0) {
            lastFrameTimestamp = System.currentTimeMillis();
        }

        return readSize;
    }

    public boolean isOpen() {
        return session != null;
    }

    public synchronized void getImageBytes(final ByteBuffer target) {
        if (target == null) {
            return;
        }

        if (target.remaining() < bufferSizeBytes) {
            LOG.error("At least {} bytes needed but passed buffer has only {} remaining size", bufferSizeBytes, target.capacity());
            return;
        }

        updateDirectBuffer();
        copyBuffer(target, directBuffer);
    }

    private static int copyBuffer(ByteBuffer dest, ByteBuffer src) {
        final int nTransfer = Math.min(dest.remaining(), src.remaining());
        if (nTransfer > 0) {
            dest.put(
                    src.array(),
                    src.arrayOffset() + src.position(),
                    nTransfer
            );
            src.position(src.position() + nTransfer);
        }

        return nTransfer;
    }
}
