package capturemanager.classes;

import capturemanager.interfaces.*;

final class CaptureManagerControl extends CaptureManagerControlNative implements ICaptureManagerControl {

    protected static final String CLSID_CoCaptureManager = "{D5F07FB8-CE60-4017-B215-95C8A0DDF42A}";

    protected static final String IID_ICaptureManagerControl = "{D4F5F10A-8F70-43CF-8CF1-EC331DA2F829}";

    CaptureManagerControl(long aPtr) {
        mPtr = aPtr;
    }

    protected long mPtr = 0;

    public void release() {
        if (mPtr != 0)
            CaptureManagerNativeProxy.getInstance().Release(mPtr);

        mPtr = 0;
    }

    @Override
    public String getCollectionOfSources() {
        String lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    SourceControl.IID);

            if (lPtr == 0)
                break;

            ISourceControl lISourceControl = new SourceControl(lPtr);

            lresult = lISourceControl.getCollectionOfSources();
        }
        while (false);

        return lresult;
    }

    @Override
    public String getCollectionOfSinks() {
        String lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    SinkControl.IID);

            if (lPtr == 0)
                break;

            ISinkControl lISinkControl = new SinkControl(lPtr);

            lresult = lISinkControl.getCollectionOfSinks();
        }
        while (false);

        return lresult;
    }

    @Override
    public ISourceControl createSourceControl() {
        ISourceControl lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    SourceControl.IID);

            if (lPtr == 0)
                break;

            lresult = new SourceControl(lPtr);

        } while (false);

        return lresult;
    }

    @Override
    public ISessionControl createSessionControl() {
        ISessionControl lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    SessionControl.IID);

            if (lPtr == 0)
                break;

            lresult = new SessionControl(lPtr);

        } while (false);

        return lresult;
    }

    @Override
    public ISinkControl createSinkControl() {
        ISinkControl lresult = null;

        do {
            do {
                if (mPtr == 0)
                    break;

                long lPtr = createControl(
                        mPtr,
                        SinkControl.IID);

                if (lPtr == 0)
                    break;

                lresult = new SinkControl(lPtr);
            }
            while (false);

        } while (false);

        return lresult;
    }

    @Override
    public IStreamControl createStreamControl() {
        IStreamControl lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    StreamControl.IID);

            if (lPtr == 0)
                break;

            lresult = new StreamControl(lPtr);

        } while (false);

        return lresult;
    }

    @Override
    public IEncoderControl createEncoderControl() {
        IEncoderControl lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createControl(
                    mPtr,
                    EncoderControl.IID);

            if (lPtr == 0)
                break;

            lresult = new EncoderControl(lPtr);

        } while (false);

        return lresult;
    }

    @Override
    public int getStrideForBitmapInfoHeader(
            String aStringMFVideoFormat,
            int aWidthInPixels) {

        int lresult = 0;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createMisc(
                    mPtr,
                    StrideForBitmap.IID);

            if (lPtr == 0)
                break;

            IStrideForBitmap lStrideForBitmap = new StrideForBitmap(lPtr);

            lresult = lStrideForBitmap.getStrideForBitmap(aStringMFVideoFormat, aWidthInPixels);

        } while (false);

        return lresult;
    }

    @Override
    public IVersionControl getVersionControl() {

        IVersionControl lresult = null;

        do {
            if (mPtr == 0)
                break;

            long lPtr = createMisc(
                    mPtr,
                    VersionControl.IID);

            if (lPtr == 0)
                break;

            lresult = new VersionControl(lPtr);

        } while (false);

        return lresult;
    }

}
