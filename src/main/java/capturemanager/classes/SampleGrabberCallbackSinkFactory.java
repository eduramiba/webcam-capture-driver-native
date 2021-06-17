package capturemanager.classes;

import capturemanager.interfaces.ISampleGrabberCallback;
import capturemanager.interfaces.ISampleGrabberCallbackSinkFactory;

class SampleGrabberCallbackSinkFactory extends SampleGrabberCallbackSinkFactoryNative
        implements ISampleGrabberCallbackSinkFactory {

    SampleGrabberCallbackSinkFactory(long aPtr) {
        mPtr = aPtr;
    }

    protected long mPtr = 0;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (mPtr != 0)
            CaptureManagerNativeProxy.getInstance().Release(mPtr);

        mPtr = 0;
    }

    @Override
    public ISampleGrabberCallback createOutputNode(
            String aStringMajorType,
            String aStringSubType) {

        ISampleGrabberCallback lresult = null;

        do {
            if (mPtr == 0)
                break;

            SampleGrabberCallback lSampleGrabberCallback = new SampleGrabberCallback();

            long lPtr = createOutputNode(
                    mPtr,
                    aStringMajorType,
                    aStringSubType,
                    lSampleGrabberCallback
            );

            if (lPtr == 0) {
                break;
            }

            lSampleGrabberCallback.setPtr(lPtr);

            lresult = lSampleGrabberCallback;
        }
        while (false);

        return lresult;
    }

}
