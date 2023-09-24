package com.github.eduramiba.webcamcapture.drivers;

import com.github.sarxos.webcam.WebcamDevice;

public interface WebcamDeviceWithCustomEvents extends WebcamDevice {

    interface Listener {
        void customEventReceived(String type, Object event);
    }

    void addCustomEventsListener(Listener listener);

    boolean removeCustomEventsListener(Listener listener);
}
