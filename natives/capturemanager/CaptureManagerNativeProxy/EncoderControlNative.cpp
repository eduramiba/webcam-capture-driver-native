#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_EncoderControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_EncoderControlNative
	* Method:    createSinkFactory
	* Signature: (JLjava/lang/String;Ljava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_EncoderControlNative_createEncoderNodeFactory
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

			CComPtrCustom<IEncoderControl> lObject;

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

			lhr = lObject->createEncoderNodeFactory(
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
	* Class:     capturemanager_classes_EncoderControlNative
	* Method:    getCollectionOfSinks
	* Signature: (J)Ljava/lang/String;
	*/
	JNIEXPORT jstring JNICALL Java_capturemanager_classes_EncoderControlNative_getCollectionOfEncoders
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

			CComPtrCustom<IEncoderControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			BSTR lXMLstring = nullptr;

			lhr = lObject->getCollectionOfEncoders(&lXMLstring);

			if (FAILED(lhr))
				break;
			
			auto lLength = SysStringLen(lXMLstring);

			lresult = aPtrEnv->NewString((jchar*)lXMLstring, lLength);

			SysFreeString(lXMLstring);

		} while (false);

		return lresult;
	}


	/*
	* Class:     capturemanager_classes_EncoderControlNative
	* Method:    getMediaTypeCollectionOfEncoder
	* Signature: (JJLjava/lang/String;)Ljava/lang/String;
	*/
	JNIEXPORT jstring JNICALL Java_capturemanager_classes_EncoderControlNative_getMediaTypeCollectionOfEncoder
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jlong aIndex, jstring aStringEncoderCLSID)
	{
		jstring lresult = nullptr;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aStringEncoderCLSID == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IEncoderControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;


			const jchar *lPtrStringEncoderCLSID = aPtrEnv->GetStringChars(aStringEncoderCLSID, nullptr);

			CLSID lEncoderCLSID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringEncoderCLSID, &lEncoderCLSID);

			if (FAILED(lhr))
				break;


			lPtrIUnknown = (IUnknown*)aIndex;
			
			BSTR lXMLstring = nullptr;

			lhr = lObject->getMediaTypeCollectionOfEncoder(
				lPtrIUnknown,
				lEncoderCLSID,
				&lXMLstring);

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