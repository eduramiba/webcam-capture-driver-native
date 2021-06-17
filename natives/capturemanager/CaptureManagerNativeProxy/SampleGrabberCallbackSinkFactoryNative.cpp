#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>
#include <atomic>


#include "JNI\capturemanager_classes_SampleGrabberCallbackSinkFactoryNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"


class SampleGrabberCallback : public ISampleGrabberCallback
{
private:

	JavaVM * mPtrJavaVM = nullptr;

	jobject mIUpdateStateListener = nullptr;

public:

	SampleGrabberCallback(
		JavaVM * aPtrJavaVM,
		jobject aIUpdateStateListener) :
		mRefCount(1),
		mPtrJavaVM(aPtrJavaVM),
		mIUpdateStateListener(aIUpdateStateListener)
	{}


	virtual /* [helpstring] */ HRESULT STDMETHODCALLTYPE invoke(
		/* [in] */ REFGUID aGUIDMajorMediaType,
		/* [in] */ DWORD aSampleFlags,
		/* [in] */ LONGLONG aSampleTime,
		/* [in] */ LONGLONG aSampleDuration,
		/* [in] */ LPVOID aPtrSampleBuffer,
		/* [in] */ DWORD aSampleSize)
	{
		do
		{
			if (mPtrJavaVM == nullptr)
				break;

			JNIEnv * lPtrEnv;

			// double check it's all ok
			int getEnvStat = mPtrJavaVM->GetEnv((void **)&lPtrEnv, JNI_VERSION_1_6);

			if (getEnvStat == JNI_EDETACHED) {

				if (mPtrJavaVM->AttachCurrentThread((void **)&lPtrEnv, NULL) != 0)
				{

				}
			}
			else if (getEnvStat == JNI_OK) {
				//
			}
			else if (getEnvStat == JNI_EVERSION) {
				//std::cout << "GetEnv: version not supported" << std::endl;
			}





			if (mIUpdateStateListener == nullptr)
				break;

			jclass thisClass = lPtrEnv->GetObjectClass(mIUpdateStateListener);

			if (thisClass == nullptr)
				break;

			jmethodID linvoke = lPtrEnv->GetMethodID(thisClass,
				"invoke", "([BI)V");

			if (linvoke == nullptr)
				break;

			jbyteArray lByteArray = lPtrEnv->NewByteArray(aSampleSize);

			//jdouble average = lPtrEnv->CallIntMethod(
			//	mIUpdateStateListener, linvoke, aCallbackEventCode, aSessionDescriptor);

			mPtrJavaVM->DetachCurrentThread();

		} while (false);


		return S_OK;
	}

	virtual HRESULT STDMETHODCALLTYPE QueryInterface(REFIID riid, _COM_Outptr_ void __RPC_FAR *__RPC_FAR *ppvObject)
	{
		HRESULT lhresult = E_NOINTERFACE;

		do
		{
			if (ppvObject == NULL)
			{
				lhresult = E_POINTER;

				break;
			}

			lhresult = S_OK;

			if (riid == IID_IUnknown)
			{
				*ppvObject = static_cast<IUnknown*>(this);

				break;
			}
			else if (riid ==
				__uuidof(ISessionCallback))
			{
				*ppvObject = static_cast<ISampleGrabberCallback*>(this);

				break;
			}

			*ppvObject = NULL;

			lhresult = E_NOINTERFACE;

		} while (false);

		if (SUCCEEDED(lhresult))
			AddRef();

		return lhresult;
	}

	virtual ULONG STDMETHODCALLTYPE AddRef(void)
	{
		return ++mRefCount;
	}

	virtual ULONG STDMETHODCALLTYPE Release(void)
	{
		ULONG lCount = --mRefCount;

		if (lCount == 0)
		{
			delete this;
		}
		return lCount;
	}

private:

	std::atomic<ULONG> mRefCount;

	virtual ~SampleGrabberCallback()
	{
		if (mPtrJavaVM == nullptr)
			return;

		JNIEnv * lPtrEnv;

		int getEnvStat = mPtrJavaVM->GetEnv((void **)&lPtrEnv, JNI_VERSION_1_6);

		if (getEnvStat == JNI_EDETACHED) {

			if (mPtrJavaVM->AttachCurrentThread((void **)&lPtrEnv, NULL) != 0)
			{

			}
		}
		else if (getEnvStat == JNI_OK) {
			//
		}
		else if (getEnvStat == JNI_EVERSION) {

			return;
		}

		if (lPtrEnv)
			lPtrEnv->DeleteGlobalRef(mIUpdateStateListener);

		mPtrJavaVM->DetachCurrentThread();

	}
};

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SampleGrabberCallbackSinkFactoryNative
	* Method:    createOutputNode
	* Signature: (JLjava/lang/String;Ljava/lang/String;Ljava/lang/Object;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SampleGrabberCallbackSinkFactoryNative_createOutputNode
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aStringMajorType, jstring aStringSubType, jobject aPtrISampleGrabberCallback)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aStringMajorType == nullptr)
				break;

			if (aStringSubType == nullptr)
				break;

			if (aPtrISampleGrabberCallback == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISampleGrabberCallbackSinkFactory> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringMajorType = aPtrEnv->GetStringChars(aStringMajorType, nullptr);

			CLSID lMajorTypeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringMajorType, &lMajorTypeGUID);

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringSubType = aPtrEnv->GetStringChars(aStringSubType, nullptr);

			CLSID lSubTypeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringSubType, &lSubTypeGUID);

			if (FAILED(lhr))
				break;



			auto lGlobalISampleGrabberCallback = aPtrEnv->NewGlobalRef(aPtrISampleGrabberCallback);

			JavaVM * lPtrJavaVM;

			auto EnvStat = aPtrEnv->GetJavaVM(&lPtrJavaVM);

			if (EnvStat != JNI_OK)
				break;

			//JNI_VERSION_1_8

			CComPtrCustom<ISampleGrabberCallback> lSampleGrabberCallback = new SampleGrabberCallback(
				lPtrJavaVM,
				lGlobalISampleGrabberCallback);


			CComPtrCustom<IUnknown> lOutputNode;

			lhr = lObject->createOutputNode(
				lMajorTypeGUID,
				lSubTypeGUID,
				lSampleGrabberCallback,
				&lOutputNode);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lOutputNode.detach();

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif