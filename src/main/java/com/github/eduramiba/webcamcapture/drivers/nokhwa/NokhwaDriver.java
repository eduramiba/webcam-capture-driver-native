package com.github.eduramiba.webcamcapture.drivers.nokhwa;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.sun.jna.Native;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import static com.github.eduramiba.webcamcapture.drivers.nokhwa.LibNokhwa.*;

/**
 * Driver based on https://github.com/l1npengtul/nokhwa exported through https://github.com/eduramiba/lib-cnokhwa
 */
public class NokhwaDriver implements WebcamDriver {

    private static final Logger LOG = LoggerFactory.getLogger(NokhwaDriver.class);

    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(255);

    @Override
    public synchronized List<WebcamDevice> getDevices() {
        final var lib = LibNokhwa.INSTANCE;

        final List<WebcamDevice> list = new ArrayList<>();

        if (lib.cnokhwa_initialize() != RESULT_OK) {
            LOG.error("Error initializing native library");
            return list;
        }

        final int devicesCount = lib.cnokhwa_devices_count();

        LOG.info("Available devices: {}", devicesCount);

        if (devicesCount < 1) {
            return list;
        }

        final Set<String> availableFormats = new LinkedHashSet<>();

        for (int devIndex = 0; devIndex < devicesCount; devIndex++) {
            final String uniqueId = deviceUniqueId(devIndex);
            final String name = deviceName(devIndex);

            final int formatCount = lib.cnokhwa_device_formats_count(devIndex);

            final Set<Dimension> resolutions = new LinkedHashSet<>();
            int maxFps = 0;
            for (int formatIndex = 0; formatIndex < formatCount; formatIndex++) {
                final String formatType = deviceFormatType(devIndex, formatIndex);
                final int formatWidth = lib.cnokhwa_device_format_width(devIndex, formatIndex);
                final int formatHeight = lib.cnokhwa_device_format_height(devIndex, formatIndex);
                final int formatFps = lib.cnokhwa_device_format_frame_rate(devIndex, formatIndex);

                availableFormats.add(String.format("%dx%d %s (%d fps)", formatWidth, formatHeight, formatType, formatFps));

                final Dimension resolution = new Dimension(formatWidth, formatHeight);

                resolutions.add(resolution);

                if (formatFps > maxFps) {
                    maxFps = formatFps;
                }
            }

            LOG.info("Found camera {} (id {}) with available formats: {}", name, uniqueId, availableFormats);

            final NokhwaVideoDevice device = new NokhwaVideoDevice(devIndex, uniqueId, name, resolutions, maxFps);
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
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibNokhwa.INSTANCE.cnokhwa_device_unique_id(deviceIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceName(final int deviceIndex) {
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibNokhwa.INSTANCE.cnokhwa_device_name(deviceIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceFormatType(final int deviceIndex, final int formatIndex) {
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibNokhwa.INSTANCE.cnokhwa_device_format_type(deviceIndex, formatIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }
}
