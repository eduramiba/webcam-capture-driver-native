#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_CaptureManagerControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_CaptureManagerControlNative
	* Method:    createControl
	* Signature: (JLjava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_CaptureManagerControlNative_createControl
		(JNIEnv *aPtrEnv, jobject, jlong aPtr, jstring aStringIID)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ICaptureManagerControl> lICaptureManagerControl;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lICaptureManagerControl));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lIID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lIID);

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lIUnknown;

			lhr = lICaptureManagerControl->createControl(
				lIID,
				&lIUnknown);

			if (FAILED(lhr))
				break;
			
			lresult = (jlong)lIUnknown.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_CaptureManagerControlNative
	* Method:    createMisc
	* Signature: (JLjava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_CaptureManagerControlNative_createMisc
		(JNIEnv *aPtrEnv, jobject, jlong aPtr, jstring aStringIID)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ICaptureManagerControl> lICaptureManagerControl;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lICaptureManagerControl));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lIID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lIID);

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lIUnknown;

			lhr = lICaptureManagerControl->createMisc(
				lIID,
				&lIUnknown);

			if (FAILED(lhr))
				break;
			
			lresult = (jlong)lIUnknown.detach();

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif