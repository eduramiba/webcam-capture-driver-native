package com.github.eduramiba.webcamcapture.drivers.capturemanager.session;

import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerMediaType;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerStreamDescriptor;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import java.awt.*;
import java.util.List;

public interface CaptureManagerSessionControl {

    boolean init(
            final CaptureManagerSource source,
            final CaptureManagerStreamDescriptor stream,
            final CaptureManagerMediaType mediaType,
            final List<CaptureManagerSinkFactory> sinkFactories,
            Component aGraphicComponent
    );

    void start();

    void stop();
}
