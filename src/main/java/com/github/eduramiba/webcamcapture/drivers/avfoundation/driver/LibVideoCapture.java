package com.github.eduramiba.webcamcapture.drivers.avfoundation.driver;

import com.sun.jna.*;

public interface LibVideoCapture extends Library {
    String JNA_LIBRARY_NAME = "videocapture";
    NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LibVideoCapture.JNA_LIBRARY_NAME);
    LibVideoCapture INSTANCE = Native.loadLibrary(LibVideoCapture.JNA_LIBRARY_NAME, LibVideoCapture.class);

    public static final int RESULT_OK = (0);
    public static final int ERROR_DEVICE_NOT_FOUND = (-1);
    public static final int ERROR_FORMAT_NOT_FOUND = (-2);
    public static final int ERROR_OPENING_DEVICE = (-3);
    public static final int ERROR_SESSION_ALREADY_STARTED = (-4);
    public static final int ERROR_SESSION_NOT_STARTED = (-5);
    public static final int STATUS_AUTHORIZED = (0);
    public static final int STATUS_NOT_DETERMINED = (-2);
    public static final int STATUS_DENIED = (-1);


    boolean vcavf_initialize();

    int vcavf_has_videocapture_auth();
    void vcavf_ask_videocapture_auth();

    int vcavf_devices_count();

    void vcavf_get_device_unique_id(int deviceIndex, Pointer pointer, int availableBytes);

    void vcavf_get_device_model_id(int deviceIndex, Pointer pointer, int availableBytes);

    void vcavf_get_device_name(int deviceIndex, Pointer pointer, int availableBytes);

    int vcavf_get_device_formats_count(int deviceIndex);

    void vcavf_get_device_format(int deviceIndex, int formatIndex, Pointer memory, int availableBytes);

    int vcavf_start_capture(int deviceIndex, int width, int height);

    int vcavf_stop_capture(int deviceIndex);

    boolean vcavf_has_new_frame(int deviceIndex);

    int vcavf_frame_width(int deviceIndex);

    int vcavf_frame_height(int deviceIndex);

    int vcavf_frame_bytes_per_row(int deviceIndex);

    boolean vcavf_grab_frame(int deviceIndex, Pointer pointer, int availableBytes);
}
