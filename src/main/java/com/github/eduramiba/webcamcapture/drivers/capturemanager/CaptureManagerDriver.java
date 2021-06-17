package com.github.eduramiba.webcamcapture.drivers.capturemanager;

import capturemanager.classes.CaptureManager;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.CaptureManagerSource;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.parser.CaptureManagerModelXMLParser;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.model.sinks.CaptureManagerSinkFactory;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaptureManagerDriver implements WebcamDriver {

    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerDriver.class);

    @Override
    public List<WebcamDevice> getDevices() {
        try {
            final CaptureManager captureManager = CaptureManager.getInstance();

            final CaptureManagerModelXMLParser parser = new CaptureManagerModelXMLParser();
            final List<CaptureManagerSource> sources = parser.parseVideoSources(captureManager.getICaptureManagerControl().getCollectionOfSources());

            final List<CaptureManagerSinkFactory> sinksFactories = parser.parseSinkFactories(captureManager.getICaptureManagerControl().getCollectionOfSinks());

            return sources.stream()
                    .filter(Objects::nonNull)
                    .map(source -> new CaptureManagerVideoDevice(source, sinksFactories))
                    .filter(CaptureManagerVideoDevice::isValid)
                    .collect(Collectors.toList());
        } catch (Throwable ex) {
            LOG.error("Unexpected error listing video devices", ex);

            return Collections.emptyList();
        }
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
}
