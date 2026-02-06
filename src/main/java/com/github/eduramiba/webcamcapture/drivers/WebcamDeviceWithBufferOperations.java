package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javafx.scene.image.WritableImage;

public interface WebcamDeviceWithBufferOperations extends WebcamDevice {

    enum RawFramePixelFormat {
        BYTE_RGB(3);

        private final int bytesPerPixel;

        RawFramePixelFormat(final int bytesPerPixel) {
            this.bytesPerPixel = bytesPerPixel;
        }

        public int getBytesPerPixel() {
            return bytesPerPixel;
        }
    }

    BufferedImage getImage(ByteBuffer byteBuffer);

    boolean updateFXIMage(WritableImage writableImage);

    boolean updateFXIMage(WritableImage writableImage, long lastFrameTimestamp);

    long getLastFrameTimestamp();

    RawFramePixelFormat getRawFramePixelFormat();

    default int getRawFrameBytesPerPixel() {
        return getRawFramePixelFormat().getBytesPerPixel();
    }

    default int getRawFrameBytesPerRow() {
        final Dimension currentResolution = getResolution();
        if (currentResolution == null || currentResolution.width <= 0) {
            return -1;
        }
        return currentResolution.width * getRawFrameBytesPerPixel();
    }

    default int getRawFrameExpectedBufferSizeBytes() {
        final Dimension currentResolution = getResolution();
        final int rowBytes = getRawFrameBytesPerRow();
        if (currentResolution == null || currentResolution.height <= 0 || rowBytes <= 0) {
            return -1;
        }
        return rowBytes * currentResolution.height;
    }
}
