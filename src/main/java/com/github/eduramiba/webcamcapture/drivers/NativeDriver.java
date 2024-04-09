package com.github.eduramiba.webcamcapture.drivers;

import com.github.eduramiba.webcamcapture.drivers.avfoundation.driver.AVFDriver;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.CaptureManagerDriver;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NativeDriver implements WebcamDriver, WebcamDiscoverySupport {
    private static final Logger LOG = LoggerFactory.getLogger(NativeDriver.class);

    private final WebcamDriver driver;

    public NativeDriver() {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((os.contains("mac")) || (os.contains("darwin"))) {
            this.driver = new AVFDriver();
        } else if (os.contains("win")) {
            this.driver = new CaptureManagerDriver();
        } else {
            // TODO support at least Linux and Raspberry
            LOG.warn("Unsupported OS {}. No devices will be returned!", os);
            this.driver = new WebcamDriver() {
                @Override
                public List<WebcamDevice> getDevices() {
                    return Collections.emptyList();
                }

                @Override
                public boolean isThreadSafe() {
                    return true;
                }
            };
        }
    }

    @Override
    public List<WebcamDevice> getDevices() {
        return driver.getDevices();
    }

    @Override
    public boolean isThreadSafe() {
        return driver.isThreadSafe();
    }

    @Override
    public long getScanInterval() {
        return DEFAULT_SCAN_INTERVAL;
    }

    @Override
    public boolean isScanPossible() {
        return true;
    }
}
