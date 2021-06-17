#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>
#include <atomic>


#include "JNI\capturemanager_classes_SessionNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#define IID_PPV_ARGSIUnknown(ppType) __uuidof(**(ppType)), (IUnknown**)(ppType)



class SessionCallback : public ISessionCallback
{
private:

	JavaVM * mPtrJavaVM = nullptr;

	jobject mIUpdateStateListener = nullptr;

public:

	SessionCallback(
		JavaVM * aPtrJavaVM,
		jobject aIUpdateStateListener) :
		mRefCount(1),
		mPtrJavaVM(aPtrJavaVM),
		mIUpdateStateListener(aIUpdateStateListener)
	{}

	virtual /* [helpstring] */ HRESULT STDMETHODCALLTYPE invoke(
		/* [in] */ DWORD aCallbackEventCode,
		/* [in] */ DWORD aSessionDescriptor)
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
				"invoke", "(II)V");

			if (linvoke == nullptr)
				break;

			jdouble average = lPtrEnv->CallIntMethod(
				mIUpdateStateListener, linvoke, aCallbackEventCode, aSessionDescriptor);

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
				*ppvObject = static_cast<ISessionCallback*>(this);

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

	virtual ~SessionCallback()
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
	* Class:     capturemanager_classes_SessionNative
	* Method:    closeSession
	* Signature: (J)Z
	*/
	JNIEXPORT jboolean JNICALL Java_capturemanager_classes_SessionNative_closeSession
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		jboolean lresult = JNI_FALSE;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;
			
			lhr = lObject->closeSession();

			if (FAILED(lhr))
				break;

			lresult = JNI_TRUE;

		} while (false);

		return lresult;
	}


	/*
	* Class:     capturemanager_classes_SessionNative
	* Method:    addIUpdateStateListener
	* Signature: (JLjava/lang/Object;)Z
	*/
	JNIEXPORT jboolean JNICALL Java_capturemanager_classes_SessionNative_addIUpdateStateListener
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jobject aIUpdateStateListener)
	{
		jboolean lresult = JNI_FALSE;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;


			CComPtrCustom<IConnectionPointContainer> IConnectionPointContainer;

			lhr = lObject->getIConnectionPointContainer(
				IID_PPV_ARGSIUnknown(&IConnectionPointContainer));

			if (FAILED(lhr))
				break;

			CComPtrCustom<IConnectionPoint> lConnectionPoint;

			lhr = IConnectionPointContainer->FindConnectionPoint(
				__uuidof(ISessionCallback),
				&lConnectionPoint);

			if (FAILED(lhr))
				break;



			auto lGlobalIUpdateStateListener = aPtrEnv->NewGlobalRef(aIUpdateStateListener);

			JavaVM * lPtrJavaVM;

			auto EnvStat = aPtrEnv->GetJavaVM(&lPtrJavaVM);

			if (EnvStat != JNI_OK)
				break;

			//JNI_VERSION_1_8
			
			CComPtrCustom<ISessionCallback> lSessionCallback = new SessionCallback(
				lPtrJavaVM,
				lGlobalIUpdateStateListener);

			DWORD lStreamID;

			lhr = lConnectionPoint->Advise(
				lSessionCallback,
				&lStreamID);

			if (FAILED(lhr))
				break;

			lresult = JNI_TRUE;

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SessionNative
	* Method:    getSessionDescriptor
	* Signature: (J)I
	*/
	JNIEXPORT jint JNICALL Java_capturemanager_classes_SessionNative_getSessionDescriptor
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		jint lresult = -1;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;
			
			DWORD lSessionDescriptor = 0;

			lhr = lObject->getSessionDescriptor(&lSessionDescriptor);

			if (FAILED(lhr))
				break;

			lresult = lSessionDescriptor;

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SessionNative
	* Method:    pauseSession
	* Signature: (J)Z
	*/
	JNIEXPORT jboolean JNICALL Java_capturemanager_classes_SessionNative_pauseSession
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		jboolean lresult = JNI_FALSE;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;
			
			lhr = lObject->pauseSession();

			if (FAILED(lhr))
				break;

			lresult = JNI_TRUE;

		} while (false);

		return lresult;
	}
	/*
	* Class:     capturemanager_classes_SessionNative
	* Method:    startSession
	* Signature: (JJLjava/lang/String;)Z
	*/
	JNIEXPORT jboolean JNICALL Java_capturemanager_classes_SessionNative_startSession
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jlong aStartTime, jstring aStringGUIDTimeType)
	{
		jboolean lresult = JNI_FALSE;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringGUIDTimeType, nullptr);

			CLSID lGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lGUID);

			if (FAILED(lhr))
				break;

			lhr = lObject->startSession(
				aStartTime,
				lGUID);

			if (FAILED(lhr))
				break;

			lresult = JNI_TRUE;

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SessionNative
	* Method:    stopSession
	* Signature: (J)Z
	*/
	JNIEXPORT jboolean JNICALL Java_capturemanager_classes_SessionNative_stopSession
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		jboolean lresult = JNI_FALSE;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISession> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;
			
			lhr = lObject->stopSession();

			if (FAILED(lhr))
				break;

			lresult = JNI_TRUE;

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif