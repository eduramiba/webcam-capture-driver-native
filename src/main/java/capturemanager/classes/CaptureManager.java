package capturemanager.classes;

import capturemanager.interfaces.*;

public class CaptureManager {

    private static CaptureManager mInstance = null;

    private ICaptureManagerNativeProxy mCaptureManagerNativeProxy = null;

    private ICaptureManagerControl mICaptureManagerControl = null;

    private ILogPrintOutControl mILogPrintOutControl = null;

    private static Object mlockObject = new Object();

    public static CaptureManager getInstance() {
        synchronized (mlockObject) {
            if (mInstance != null)
                return mInstance;

            boolean lstate = false;

            try {
                if (mInstance == null) {
                    mInstance = new CaptureManager();

                    mInstance.mCaptureManagerNativeProxy = CaptureManagerNativeProxy.getInstance();

                    lstate = true;
                }

            } finally {

                if (mInstance != null && lstate == false)
                    mInstance.mCaptureManagerNativeProxy = null;
            }
        }

        return mInstance;
    }

    private CaptureManager() {
    }

    private boolean checkFailNative() {
        return mCaptureManagerNativeProxy == null;
    }

    public void freeLibrary() {
        do {
            if (checkFailNative())
                break;

            try {
                if (mICaptureManagerControl != null)
                    mICaptureManagerControl.release();

                if (mILogPrintOutControl != null)
                    mILogPrintOutControl.release();

                mCaptureManagerNativeProxy.freeLibrary(
                        CaptureManagerNativeProxy.CaptureManagerFileName
                );

                mCaptureManagerNativeProxy = null;

                CaptureManagerNativeProxy.freeInstance();

                CaptureManagerNativeProxy.release();

            } finally {

            }

        }
        while (false);
    }

    public ILogPrintOutControl getILogPrintOutControl() {
        ILogPrintOutControl lresult = null;

        do {
            if (checkFailNative())
                break;

            try {

                long aPtr = mCaptureManagerNativeProxy.explicitGetPtrClass(
                        CaptureManagerNativeProxy.CaptureManagerFileName,
                        LogPrintOutControl.CLSID_CoLogPrintOut,
                        LogPrintOutControl.IID_ILogPrintOutControl);

                if (aPtr == 0)
                    break;

                lresult = new LogPrintOutControl(aPtr);

                mILogPrintOutControl = lresult;
            } finally {

            }

        }
        while (false);

        return mILogPrintOutControl;
    }

    public ICaptureManagerControl getICaptureManagerControl() {
        ICaptureManagerControl lresult = new CaptureManagerControl(0);

        do {
            if (checkFailNative())
                break;

            try {
                if (mICaptureManagerControl != null) {
                    lresult = mICaptureManagerControl;

                    break;
                }

                long aPtr = mCaptureManagerNativeProxy.explicitGetPtrClass(
                        CaptureManagerNativeProxy.CaptureManagerFileName,
                        CaptureManagerControl.CLSID_CoCaptureManager,
                        CaptureManagerControl.IID_ICaptureManagerControl);

                if (aPtr == 0)
                    break;

                lresult = new CaptureManagerControl(aPtr);

                mICaptureManagerControl = lresult;
            } finally {

            }

        }
        while (false);

        return lresult;
    }

}
