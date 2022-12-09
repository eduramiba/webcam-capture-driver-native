package com.github.eduramiba.webcamcapture.drivers.avfoundation.driver;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;
import com.sun.jna.Native;

public class AVFDriver implements WebcamDriver {

    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(255);

    @Override
    public synchronized List<WebcamDevice> getDevices() {
        final var lib = LibVideoCapture.INSTANCE;

        final List<WebcamDevice> list = new ArrayList<>();

        lib.vcavf_initialize();
        final int count = lib.vcavf_devices_count();

        if (count < 1) {
            return list;
        }

        for (int devIndex = 0; devIndex < count; devIndex++) {
            final String uniqueId = deviceUniqueId(devIndex);
            final String name = deviceName(devIndex);

            final int formatCount = lib.vcavf_get_device_formats_count(devIndex);

            final Set<Dimension> resolutions = new LinkedHashSet<>();
            for (int formatIndex = 0; formatIndex < formatCount; formatIndex++) {
                final String format = deviceFormat(devIndex, formatIndex);

                final Dimension resolution = formatToDimension(format);
                if (resolution != null) {
                    resolutions.add(resolution);
                }
            }

            final AVFVideoDevice device = new AVFVideoDevice(devIndex, uniqueId, name, resolutions);
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
        LibVideoCapture.INSTANCE.vcavf_get_device_unique_id(deviceIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceModelId(final int deviceIndex) {
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibVideoCapture.INSTANCE.vcavf_get_device_model_id(deviceIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceName(final int deviceIndex) {
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibVideoCapture.INSTANCE.vcavf_get_device_name(deviceIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    private static String deviceFormat(final int deviceIndex, final int formatIndex) {
        final var bufferP = Native.getDirectBufferPointer(buffer);
        LibVideoCapture.INSTANCE.vcavf_get_device_format(deviceIndex, formatIndex, bufferP, buffer.capacity());
        return bufferP.getString(0, StandardCharsets.UTF_8.name());
    }

    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("[0-9]+x[0-9]+", Pattern.CASE_INSENSITIVE);

    private static Dimension formatToDimension(final String format) {
        final String[] parts = format.split(";");
        if (parts.length > 0) {
            final String resolution = parts[0].trim();

            if (RESOLUTION_PATTERN.matcher(resolution).matches()) {
                final String[] widthAndHeight = resolution.split("[Xx]");
                return new Dimension(Integer.parseInt(widthAndHeight[0]), Integer.parseInt(widthAndHeight[1]));
            }
        }

        return null;
    }
}
