#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_EncoderNodeFactoryNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif


	/*
	* Class:     capturemanager_classes_EncoderNodeFactoryNative
	* Method:    createCompressedMediaType
	* Signature: (JJLjava/lang/String;II)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_EncoderNodeFactoryNative_createCompressedMediaType
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jlong aPtrUncompressedMediaType, 
		jstring aStringEncodingModeGUID, jint aEncoderModeValue, jint aCompressedMediaTypeIndex)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aPtrUncompressedMediaType == 0)
				break;

			if (aStringEncodingModeGUID == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IEncoderNodeFactory> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringEncodingModeGUID = aPtrEnv->GetStringChars(aStringEncodingModeGUID, nullptr);

			CLSID lEncodingModeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringEncodingModeGUID, &lEncodingModeGUID);

			if (FAILED(lhr))
				break;

			lPtrIUnknown = (IUnknown*)aPtrUncompressedMediaType;


			CComPtrCustom<IUnknown> l_CompressedMediaType;

			lhr = lObject->createCompressedMediaType(
				lPtrIUnknown,
				lEncodingModeGUID,
				aEncoderModeValue,
				aCompressedMediaTypeIndex,
				&l_CompressedMediaType);
			
			if (FAILED(lhr))
				break;

			if (!l_CompressedMediaType)
				break;

			lresult = (jlong)l_CompressedMediaType.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_EncoderNodeFactoryNative
	* Method:    createEncoderNode
	* Signature: (JJLjava/lang/String;IIJ)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_EncoderNodeFactoryNative_createEncoderNode
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jlong aPtrUncompressedMediaType,
		jstring aStringEncodingModeGUID, jint aEncodingModeValue, jint aIndexCompressedMediaType,
		jlong aPtrDownStreamNode)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aPtrUncompressedMediaType == 0)
				break;

			if (aStringEncodingModeGUID == nullptr)
				break;

			if (aPtrDownStreamNode == 0)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IEncoderNodeFactory> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringEncodingModeGUID = aPtrEnv->GetStringChars(aStringEncodingModeGUID, nullptr);

			CLSID lEncodingModeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringEncodingModeGUID, &lEncodingModeGUID);

			if (FAILED(lhr))
				break;

			lPtrIUnknown = (IUnknown*)aPtrUncompressedMediaType;

			IUnknown* lPtrIUnknownDownStreamNode = (IUnknown*)aPtrDownStreamNode;


			CComPtrCustom<IUnknown> l_Unknown;

			lhr = lObject->createEncoderNode(
				lPtrIUnknown,
				lEncodingModeGUID,
				aEncodingModeValue,
				aIndexCompressedMediaType,
				lPtrIUnknownDownStreamNode,
				&l_Unknown);

			if (FAILED(lhr))
				break;

			if (!l_Unknown)
				break;

			lresult = (jlong)l_Unknown.detach();

		} while (false);

		return lresult;
	}
	
#ifdef __cplusplus
}
#endif