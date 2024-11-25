package com.github.eduramiba.webcamcapture.drivers.nokhwa;

import com.sun.jna.*;

public interface LibNokhwa extends Library {
    
    String JNA_LIBRARY_NAME = "cnokhwa";
    NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LibNokhwa.JNA_LIBRARY_NAME);
    LibNokhwa INSTANCE = Native.loadLibrary(LibNokhwa.JNA_LIBRARY_NAME, LibNokhwa.class);

    // Results:
    public static final int RESULT_OK = (0);
    public static final int RESULT_YES = (0);
    public static final int RESULT_NO = (-256);

    // Errors:
    public static final int ERROR_DEVICE_NOT_FOUND = (-1);
    public static final int ERROR_FORMAT_NOT_FOUND = (-2);
    public static final int ERROR_OPENING_DEVICE = (-3);
    public static final int ERROR_SESSION_ALREADY_STARTED = (-4);
    public static final int ERROR_SESSION_NOT_STARTED = (-5);
    public static final int ERROR_STATE_NOT_INITIALIZED = (-6);
    public static final int ERROR_READING_CAMERA_SESSION = (-7);
    public static final int ERROR_READING_FRAME = (-8);
    public static final int ERROR_DECODING_FRAME = (-9);
    public static final int ERROR_BUFFER_NULL = (-10);
    public static final int ERROR_BUFFER_NOT_ENOUGH_CAPACITY = (-11);

    // Auth status:
    public static final int STATUS_AUTHORIZED = (0);
    public static final int STATUS_DENIED = (-1);
    public static final int STATUS_NOT_DETERMINED = (-2);

    int cnokhwa_initialize();

    int cnokhwa_has_videocapture_auth();
    
    void cnokhwa_ask_videocapture_auth();

    int cnokhwa_devices_count();

    int cnokhwa_device_unique_id(int deviceIndex, Pointer pointer, int availableBytes);

    int cnokhwa_device_model_id(int deviceIndex, Pointer pointer, int availableBytes);

    int cnokhwa_device_name(int deviceIndex, Pointer pointer, int availableBytes);

    int cnokhwa_device_formats_count(int deviceIndex);

    int cnokhwa_device_format_width(int deviceIndex, int formatIndex);

    int cnokhwa_device_format_height(int deviceIndex, int formatIndex);

    int cnokhwa_device_format_frame_rate(int deviceIndex, int formatIndex);

    int cnokhwa_device_format_type(int deviceIndex, int formatIndex, Pointer pointer, int availableBytes);

    int cnokhwa_start_capture(int deviceIndex, int width, int height);

    int cnokhwa_stop_capture(int deviceIndex);

    int cnokhwa_has_new_frame(int deviceIndex);

    int cnokhwa_frame_width(int deviceIndex);

    int cnokhwa_frame_height(int deviceIndex);

    int cnokhwa_frame_bytes_per_row(int deviceIndex);

    int cnokhwa_grab_frame(int deviceIndex, Pointer pointer, int availableBytes);
}
