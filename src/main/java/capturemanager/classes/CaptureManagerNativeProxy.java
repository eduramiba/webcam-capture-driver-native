package capturemanager.classes;

import capturemanager.interfaces.ICaptureManagerNativeProxy;
import capturemanager.utils.NativeUtils;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CaptureManagerNativeProxy implements ICaptureManagerNativeProxy {
    private static final Logger LOG = LoggerFactory.getLogger(CaptureManagerNativeProxy.class);

    private static final String NativeProxyFile = "CaptureManagerNativeProxy.dll";

    private static final String CaptureManagerFile = "CaptureManager.dll";

    public static String CaptureManagerFileName = "";

    public static String NativeProxyFileName = "";

    private static File CaptureManagerFileRef = null;

    private static File NativeProxyFilePath = null;

    static {

        try {
            CaptureManagerFileRef = NativeUtils.loadLibrary(CaptureManagerFile);

            CaptureManagerFileName = CaptureManagerFileRef.getName();

            NativeProxyFilePath = NativeUtils.loadLibrary(NativeProxyFile);

            NativeProxyFileName = NativeProxyFilePath.getName();

        } catch (IOException ex) {
            LOG.error("Unexpected error loading CaptureManager libraries", ex);
        }
    }

    private static ICaptureManagerNativeProxy mInstance = null;

    private CaptureManagerNativeProxy() {
    }

    protected static ICaptureManagerNativeProxy getInstance() {
        if (mInstance == null) {
            mInstance = new CaptureManagerNativeProxy();
        }

        return mInstance;
    }

    protected static void freeInstance() {
        mInstance = null;
    }

    static public void release() {
        //NOOP
        //This used to delete the dll file. Not nice idea
    }

    public native long explicitGetPtrClass(String aStringFilePath, String aStringCLSID, String aStringGUID);

    public native void freeLibrary(String aFileName);

    public native void Release(long aPtr);
}
