#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_SinkControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SinkControlNative
	* Method:    createSinkFactory
	* Signature: (JLjava/lang/String;Ljava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SinkControlNative_createSinkFactory
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aStringContainerTypeGUID, jstring aStringIID)

	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISinkControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringContainerTypeGUID = aPtrEnv->GetStringChars(aStringContainerTypeGUID, nullptr);

			CLSID lContainerTypeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringContainerTypeGUID, &lContainerTypeGUID);

			if (FAILED(lhr))
				break;
			
			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lInterfaceID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lInterfaceID);

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lIUnknown;

			lhr = lObject->createSinkFactory(
				lContainerTypeGUID,
				lInterfaceID,
				&lIUnknown);

			if (FAILED(lhr))
				break;

			if (!lIUnknown)
				break;

			lresult = (jlong)lIUnknown.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SinkControlNative
	* Method:    getCollectionOfSinks
	* Signature: (J)Ljava/lang/String;
	*/
	JNIEXPORT jstring JNICALL Java_capturemanager_classes_SinkControlNative_getCollectionOfSinks
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		jstring lresult = nullptr;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISinkControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			BSTR lXMLstring = nullptr;

			lhr = lObject->getCollectionOfSinks(&lXMLstring);

			if (FAILED(lhr))
				break;

			auto lLength = SysStringLen(lXMLstring);

			lresult = aPtrEnv->NewString((jchar*)lXMLstring, lLength);

			SysFreeString(lXMLstring);

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif