package com.github.eduramiba.webcamcapture.drivers;

import com.github.eduramiba.webcamcapture.drivers.avfoundation.driver.AVFDriver;
import com.github.eduramiba.webcamcapture.drivers.directshow.CdshowDriver;
import com.github.eduramiba.webcamcapture.drivers.nokhwa.NokhwaDriver;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDiscoverySupport;
import com.github.sarxos.webcam.WebcamDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class NativeDriver implements WebcamDriver, WebcamDiscoverySupport {

    public enum WindowsBackend {
        NOKHWA,
        DIRECTSHOW
    }

    public static final String WINDOWS_BACKEND_PROPERTY = "webcamcapture.windows.backend";

    private static final Logger LOG = LoggerFactory.getLogger(NativeDriver.class);

    private WebcamDriver driver;
    private final boolean supportScan;
    private final boolean windows;
    private final WindowsBackend windowsBackend;

    public NativeDriver() {
        this(true);
    }

    public NativeDriver(boolean supportScan) {
        this(supportScan, null);
    }

    public NativeDriver(final WindowsBackend windowsBackend) {
        this(true, windowsBackend);
    }

    public NativeDriver(final boolean supportScan, final WindowsBackend preferredWindowsBackend) {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        this.windows = os.contains("win");

        if ((os.contains("mac")) || (os.contains("darwin"))) {
            this.driver = new AVFDriver();
            this.windowsBackend = null;
        } else if (os.contains("win")) {
            this.windowsBackend = resolveWindowsBackend(preferredWindowsBackend);
            this.driver = buildWindowsDriver(this.windowsBackend);
        } else {
            this.driver = new NokhwaDriver();
            this.windowsBackend = null;
        }

        this.supportScan = supportScan;
    }

    @Override
    public synchronized List<WebcamDevice> getDevices() {
        try {
            return driver.getDevices();
        } catch (LinkageError e) {
            if (windows) {
                if (driver instanceof CdshowDriver) {
                    return fallbackToAlternativeBackend(new NokhwaDriver(), "DirectShow backend failed to load. Falling back to Nokhwa backend.", e);
                }

                if (driver instanceof NokhwaDriver) {
                    return fallbackToAlternativeBackend(new CdshowDriver(), "Nokhwa backend failed to load. Falling back to DirectShow backend.", e);
                }
            }

            throw e;
        }
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

    private static WebcamDriver buildWindowsDriver(final WindowsBackend backend) {
        if (backend == WindowsBackend.DIRECTSHOW) {
            return new CdshowDriver();
        }
        return new NokhwaDriver();
    }

    private static WindowsBackend resolveWindowsBackend(final WindowsBackend preferredWindowsBackend) {
        if (preferredWindowsBackend != null) {
            return preferredWindowsBackend;
        }

        final String configuredBackend = System.getProperty(WINDOWS_BACKEND_PROPERTY);
        if (configuredBackend == null || configuredBackend.isBlank()) {
            return WindowsBackend.NOKHWA;
        }

        final String normalized = configuredBackend.trim().toLowerCase(Locale.ENGLISH);

        if ("directshow".equals(normalized) || "cdshow".equals(normalized)) {
            return WindowsBackend.DIRECTSHOW;
        }

        if ("nokhwa".equals(normalized)) {
            return WindowsBackend.NOKHWA;
        }

        LOG.warn("Unknown value '{}' for system property '{}'. Using Nokhwa backend.", configuredBackend, WINDOWS_BACKEND_PROPERTY);
        return WindowsBackend.NOKHWA;
    }

    private List<WebcamDevice> fallbackToAlternativeBackend(final WebcamDriver alternativeDriver, final String message, final LinkageError primaryError) {
        LOG.warn(message, primaryError);
        this.driver = alternativeDriver;
        try {
            return this.driver.getDevices();
        } catch (LinkageError fallbackError) {
            primaryError.addSuppressed(fallbackError);
            throw primaryError;
        }
    }

}
