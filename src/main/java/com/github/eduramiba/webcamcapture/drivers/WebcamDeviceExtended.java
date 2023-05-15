package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;

public interface WebcamDeviceExtended extends WebcamDevice, WebcamDevice.FPSSource, WebcamDevice.BufferAccess, WebcamDeviceWithId, WebcamDeviceWithBufferOperations {
}