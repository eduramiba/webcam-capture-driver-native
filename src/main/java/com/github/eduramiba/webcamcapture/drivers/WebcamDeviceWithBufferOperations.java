package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javafx.scene.image.WritableImage;

public interface WebcamDeviceWithBufferOperations extends WebcamDevice {

    BufferedImage getImage(final ByteBuffer byteBuffer);

    boolean updateFXIMage(final WritableImage writableImage, final ByteBuffer byteBuffer);

    boolean updateFXIMage(final WritableImage writableImage);
}
