#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_SampleGrabberCallSinkFactoryNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SampleGrabberCallSinkFactoryNative
	* Method:    createOutputNode
	* Signature: (JLjava/lang/String;Ljava/lang/String;ILjava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SampleGrabberCallSinkFactoryNative_createOutputNode
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aStringMajorType, jstring aStringSubType, jint aSampleByteSize, jstring aStringIID)
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

			if (aStringIID == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISampleGrabberCallSinkFactory> lObject;

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

			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lIID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lIID);

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lOutputNode;
			
			lhr = lObject->createOutputNode(
				lMajorTypeGUID,
				lSubTypeGUID,
				aSampleByteSize,
				lIID,
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