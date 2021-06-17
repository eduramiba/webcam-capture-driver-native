package com.github.eduramiba.webcamcapture.drivers.capturemanager.session.impl;

import capturemanager.classes.CaptureManager;
import capturemanager.interfaces.*;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.SinkValuePart;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.session.AbstractCaptureManagerSessionControl;
import com.github.eduramiba.webcamcapture.utils.Utils;
import java.awt.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallSessionControl extends AbstractCaptureManagerSessionControl {

    private static final Logger LOG = LoggerFactory.getLogger(CallSessionControl.class);

    private static final String MFMediaType_Video = "{73646976-0000-0010-8000-00AA00389B71}";

    private static final String MFVideoFormat_RGB32 = "{00000016-0000-0010-8000-00AA00389B71}";
    private static final String MFVideoFormat_DEFAULT = MFVideoFormat_RGB32;

    private ByteBuffer directBuffer = null;
    private byte[] rawData = null;

    private ISampleGrabberCall mISampleGrabberCall = null;

    private Component displayPanel = null;

    private BufferedImage mImage = null;

    private SampleModel sampleModel = null;

    private int mVideoWidth = 0;

    private int mVideoHeight = 0;

    @Override
    public boolean init(
            final CaptureManagerSource source,
            final CaptureManagerStreamDescriptor stream,
            final CaptureManagerMediaType mediaType,
            final List<CaptureManagerSinkFactory> sinkFactories,
            Component component
    ) {
        displayPanel = component;

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

        LOG.info("Sink factory: {}", sinkFactory);

        if (sinkFactory == null) {
            LOG.error("Could not find SampleGrabberCallbackSinkFactory");
            return false;
        }

        final SinkValuePart sinkValuePart = sinkFactory.getValueParts().stream()
                .filter(s -> s.getValue().equalsIgnoreCase("PULL"))
                .findFirst()
                .orElse(null);

        if (sinkValuePart == null) {
            LOG.error("Could not find sinkValuePart in sink factory = {}", sinkFactory);
        }

        LOG.info("Sink value part = {}", sinkValuePart);

        final String sinkGUID = sinkFactory.getValueParts().stream().findFirst()
                .map(SinkValuePart::getGuid)
                .orElse(null);

        if (Utils.isBlank(sinkGUID)) {
            LOG.error("Could not find valid value part GUID in sink factory = {}", sinkFactory);
        }

        final ISampleGrabberCallSinkFactory sampleGrabberCallbackSinkFactory = sinkControl.createSampleGrabberCallSinkFactory(
                sinkGUID
        );

        if (sampleGrabberCallbackSinkFactory == null) {
            LOG.error("Could not create ISampleGrabberCallbackSinkFactory");
            return false;
        }

        final int videoWidth = mediaType.getWidth();
        final int videoHeight = mediaType.getHeight();

        final String videoFormat = MFVideoFormat_DEFAULT;

        final int stride = CaptureManager.getInstance().getICaptureManagerControl().getStrideForBitmapInfoHeader(
                videoFormat,
                videoWidth
        );

        final int sampleByteSize = Math.abs(stride) * videoWidth;

        directBuffer = ByteBuffer.allocateDirect(sampleByteSize);
        rawData = new byte[sampleByteSize];

        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;

        mImage = new BufferedImage(mVideoWidth, mVideoHeight, BufferedImage.TYPE_INT_BGR);

        sampleModel = new ComponentSampleModel(
                DataBuffer.TYPE_BYTE, mVideoWidth, mVideoHeight, 4, mVideoWidth * 4,
                new int[]{2, 1, 0} // Try {1,2,3}, {3,2,1}, {0,1,2}
        );

        mISampleGrabberCall = sampleGrabberCallbackSinkFactory.createOutputNode(
                MFMediaType_Video,
                videoFormat,
                sampleByteSize
        );


        if (mISampleGrabberCall == null) {
            LOG.error("Could not create ISampleGrabberCallback");
            return false;
        }
        final IStreamNode streamNode = mISampleGrabberCall.getStreamNode();

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

        final List<IStreamNode> lArrayPtrSourceNodesOfTopology = new ArrayList<IStreamNode>();

        lArrayPtrSourceNodesOfTopology.add(sourceNode);

        mISession = sessionControl.createSession(
                lArrayPtrSourceNodesOfTopology
        );

        if (mISession == null) {
            LOG.error("Could not create session control last step");
            return false;
        }

        mISession.addUpdateStateListener(this);

        LOG.error("Successfully created CallSessionControl");

        return true;
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static final int MAX_FPS = 60;
    public static final int PERIOD_MILLIS = 1000 / MAX_FPS;

    @Override
    public void start() {
        super.start();

        executorService.scheduleAtFixedRate(() -> {
            final int sizeData = mISampleGrabberCall.readData(directBuffer);
            if (sizeData > 0) {
                drawImage(directBuffer, sizeData);
            }
        }, 0, PERIOD_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void drawImage(final ByteBuffer byteBuffer, final int sizeData) {
        byteBuffer.position(0);
        byteBuffer.get(rawData, 0, sizeData);

        final DataBuffer buffer = new DataBufferByte(rawData, rawData.length);
        final Raster raster = Raster.createRaster(sampleModel, buffer, null);
        mImage.setData(raster);

        final double p1 = (double) displayPanel.getHeight() / (double) displayPanel.getWidth();
        final double p2 = (double) mImage.getHeight() / (double) mImage.getWidth();

        if (p1 >= p2) {
            double height = (double) displayPanel.getWidth() * p2;

            double heightShift = ((double) displayPanel.getHeight() - height) * 0.5;
            synchronized (this) {
                displayPanel.getGraphics().drawImage(mImage, 0, (int) heightShift, displayPanel.getWidth(),
                        (int) (height + heightShift), 0, 0, mImage.getWidth(), mImage.getHeight(), null);
            }
        } else {
            double width = (double) displayPanel.getHeight() * (1 / p2);

            double widthShift = ((double) displayPanel.getWidth() - width) * 0.5;

            synchronized (this) {
                displayPanel.getGraphics().drawImage(mImage, (int) widthShift, 0, (int) (width + widthShift),
                        displayPanel.getHeight(), 0, 0, mImage.getWidth(), mImage.getHeight(), null);
            }
        }
    }


    @Override
    public void stop() {
        super.stop();
        executorService.shutdownNow();
    }

}
