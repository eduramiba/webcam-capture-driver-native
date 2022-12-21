package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import javafx.scene.image.WritableImage;

public interface WebcamDeviceWithBufferOperations extends WebcamDevice {

    BufferedImage getImage(ByteBuffer byteBuffer);

    boolean updateFXIMage(WritableImage writableImage);

    boolean updateFXIMage(WritableImage writableImage, long lastFrameTimestamp);

    long getLastFrameTimestamp();
}
