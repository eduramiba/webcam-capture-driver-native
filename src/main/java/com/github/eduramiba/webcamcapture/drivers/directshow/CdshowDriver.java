package com.github.eduramiba.webcamcapture.drivers.directshow;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.github.eduramiba.webcamcapture.drivers.directshow.LibCdshow.CDS_OK;

/**
 * Driver based on a native DirectShow binding (cdshow.dll).
 */
public class CdshowDriver implements WebcamDriver {

    private static final Logger LOG = LoggerFactory.getLogger(CdshowDriver.class);

    private static final int STRING_BUFFER_SIZE = 1024;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(STRING_BUFFER_SIZE);

    @Override
    public synchronized List<WebcamDevice> getDevices() {
        final var lib = LibCdshow.INSTANCE;

        final List<WebcamDevice> list = new ArrayList<>();

        if (lib.cds_initialize() != CDS_OK) {
            LOG.error("Error initializing DirectShow native library");
            return list;
        }

        final int devicesCount = lib.cds_devices_count();

        LOG.debug("Available DirectShow devices: {}", devicesCount);

        if (devicesCount < 1) {
            return list;
        }

        final Set<String> availableFormats = new LinkedHashSet<>();

        for (int devIndex = 0; devIndex < devicesCount; devIndex++) {
            final String uniqueId = deviceUniqueId(devIndex);
            final String name = deviceName(devIndex);

            final int formatCount = lib.cds_device_formats_count(devIndex);

            final Set<Dimension> resolutions = new LinkedHashSet<>();
            int maxFps = 0;

            for (int formatIndex = 0; formatIndex < formatCount; formatIndex++) {
                final String formatType = deviceFormatType(devIndex, formatIndex);
                final int formatWidth = lib.cds_device_format_width(devIndex, formatIndex);
                final int formatHeight = lib.cds_device_format_height(devIndex, formatIndex);
                final int formatFps = lib.cds_device_format_frame_rate(devIndex, formatIndex);

                availableFormats.add(String.format("%dx%d %s (%d fps)", formatWidth, formatHeight, formatType, formatFps));

                final Dimension resolution = new Dimension(formatWidth, formatHeight);
                resolutions.add(resolution);

                if (formatFps > maxFps) {
                    maxFps = formatFps;
                }
            }

            LOG.debug("Found DirectShow camera {} (id {}) with available formats: {}", name, uniqueId, availableFormats);

            final CdshowVideoDevice device = new CdshowVideoDevice(devIndex, uniqueId, name, resolutions, maxFps);
            if (device.isValid()) {
                list.add(device);
            }
        }

        return list;
    }

    @Override
    public boolean isThreadSafe() {
        return true;
    }

    private static String deviceUniqueId(final int deviceIndex) {
        final var bufferPointer = Native.getDirectBufferPointer(BUFFER);
        LibCdshow.INSTANCE.cds_device_unique_id(deviceIndex, bufferPointer, new NativeLong(BUFFER.capacity()));
        return bufferPointer.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceName(final int deviceIndex) {
        final var bufferPointer = Native.getDirectBufferPointer(BUFFER);
        LibCdshow.INSTANCE.cds_device_name(deviceIndex, bufferPointer, new NativeLong(BUFFER.capacity()));
        return bufferPointer.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceFormatType(final int deviceIndex, final int formatIndex) {
        final var bufferPointer = Native.getDirectBufferPointer(BUFFER);
        LibCdshow.INSTANCE.cds_device_format_type(deviceIndex, formatIndex, bufferPointer, new NativeLong(BUFFER.capacity()));
        return bufferPointer.getString(0, StandardCharsets.UTF_8.name());
    }
}
