package com.github.eduramiba.webcamcapture.drivers;

import java.util.List;
import java.util.Locale;

import com.github.eduramiba.webcamcapture.drivers.avfoundation.driver.AVFDriver;
import com.github.eduramiba.webcamcapture.drivers.capturemanager.CaptureManagerDriver;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;

public class NativeDriver implements WebcamDriver {

    private final WebcamDriver driver;

    public NativeDriver() {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((os.contains("mac")) || (os.contains("darwin"))) {
            this.driver = new AVFDriver();
        } else if (os.contains("win")) {
            this.driver = new CaptureManagerDriver();
        } else {
            throw new IllegalStateException("Unsupported OS = " + os);
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
}
