package com.github.eduramiba.webcamcapture.drivers.directshow;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallFunctionMapper;
import com.sun.jna.win32.StdCallLibrary;

import java.util.HashMap;
import java.util.Map;

public interface LibCdshow extends StdCallLibrary {

    private static Map<String, Object> getCallOptions() {
        final Map<String, Object> options = new HashMap<>();

        options.put(Library.OPTION_CALLING_CONVENTION, Function.ALT_CONVENTION);
        options.put(Library.OPTION_FUNCTION_MAPPER, new StdCallFunctionMapper());

        return options;
    }

    String JNA_LIBRARY_NAME = "cdshow";
    LibCdshow INSTANCE = Native.load(LibCdshow.JNA_LIBRARY_NAME, LibCdshow.class, getCallOptions());

    int CDS_OK = 0;
    int CDS_ERR_DEVICE_NOT_FOUND = -1;
    int CDS_ERR_FORMAT_NOT_FOUND = -2;
    int CDS_ERR_OPENING_DEVICE = -3;
    int CDS_ERR_ALREADY_STARTED = -4;
    int CDS_ERR_NOT_STARTED = -5;
    int CDS_ERR_NOT_INITIALIZED = -6;
    int CDS_ERR_READ_FRAME = -8;
    int CDS_ERR_BUF_NULL = -10;
    int CDS_ERR_BUF_TOO_SMALL = -11;
    int CDS_ERR_UNKNOWN = -512;

    int cds_initialize();

    void cds_shutdown_capture_api();

    int cds_devices_count();

    NativeLong cds_device_name(int deviceIndex, Pointer buffer, NativeLong bufferLen);

    NativeLong cds_device_unique_id(int deviceIndex, Pointer buffer, NativeLong bufferLen);

    NativeLong cds_device_model_id(int deviceIndex, Pointer buffer, NativeLong bufferLen);

    int cds_device_vid(int deviceIndex);

    int cds_device_pid(int deviceIndex);

    int cds_device_formats_count(int deviceIndex);

    int cds_device_format_width(int deviceIndex, int formatIndex);

    int cds_device_format_height(int deviceIndex, int formatIndex);

    int cds_device_format_frame_rate(int deviceIndex, int formatIndex);

    NativeLong cds_device_format_type(int deviceIndex, int formatIndex, Pointer buffer, NativeLong bufferLen);

    int cds_start_capture(int deviceIndex, int width, int height);

    int cds_start_capture_with_format(int deviceIndex, int formatIndex);

    int cds_stop_capture(int deviceIndex);

    int cds_has_first_frame(int deviceIndex);

    int cds_grab_frame(int deviceIndex, Pointer buffer, NativeLong availableBytes);

    int cds_frame_width(int deviceIndex);

    int cds_frame_height(int deviceIndex);

    int cds_frame_bytes_per_row(int deviceIndex);

    int cds_button_pressed(int deviceIndex);

    long cds_button_timestamp(int deviceIndex);
}
