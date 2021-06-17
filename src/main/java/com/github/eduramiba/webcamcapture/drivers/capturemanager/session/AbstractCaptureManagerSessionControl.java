package com.github.eduramiba.webcamcapture.drivers.capturemanager.session;

import capturemanager.interfaces.ISession;
import capturemanager.interfaces.IUpdateStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCaptureManagerSessionControl implements CaptureManagerSessionControl, IUpdateStateListener {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCaptureManagerSessionControl.class);

    public static final int UnknownEvent = (0);
    public static final int Error = (1);
    public static final int Status_Error = (2);
    public static final int Execution_Error = (3);
    public static final int ItIsReadyToStart = (4);
    public static final int ItIsStarted = (5);
    public static final int ItIsPaused = (6);
    public static final int ItIsStopped = (7);
    public static final int ItIsEnded = (8);
    public static final int ItIsClosed = (9);
    public static final int VideoCaptureDeviceRemoved = (10);

    protected ISession mISession = null;

    @Override
    public void start() {
        if (mISession == null)
            return;

        mISession.startSession(0, "{00000000-0000-0000-0000-000000000000}");
    }

    @Override
    public void stop() {
        if (mISession == null)
            return;

        mISession.stopSession();

        mISession.closeSession();
    }

    @Override
    public void invoke(int aCallbackEventCode, int aSessionDescriptor) {
        LOG.info("invoke with (aCallbackEventCode, aSessionDescriptor) = ({}, {})", aCallbackEventCode, aSessionDescriptor);
    }

}
