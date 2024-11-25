package com.github.eduramiba.webcamcapture.drivers;

import com.github.eduramiba.webcamcapture.drivers.avfoundation.driver.AVFDriver;
import com.github.eduramiba.webcamcapture.drivers.nokhwa.NokhwaDriver;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;

import java.util.List;
import java.util.Locale;

public class NativeDriver implements WebcamDriver, WebcamDiscoverySupport {

    private final WebcamDriver driver;
    private final boolean supportScan;

    public NativeDriver() {
        this(true);
    }

    public NativeDriver(boolean supportScan) {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((os.contains("mac")) || (os.contains("darwin"))) {
            this.driver = new AVFDriver();
        } else if (os.contains("win")) {
            this.driver = new NokhwaDriver();
        } else {
            this.driver = new NokhwaDriver();
        }

        this.supportScan = supportScan;
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
        return supportScan;
    }


}
