package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;

public interface WebcamDeviceWithId extends WebcamDevice {
    String getId();
}
