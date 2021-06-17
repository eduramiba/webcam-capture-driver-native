#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_StrideForBitmapNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_StrideForBitmapNative
	* Method:    getStrideForBitmap
	* Signature: (JLjava/lang/String;I)I
	*/
	JNIEXPORT jint JNICALL Java_capturemanager_classes_StrideForBitmapNative_getStrideForBitmap
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aStringMediaType, jint aWidthInPixels)
	{
		jint lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aStringMediaType == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IStrideForBitmap> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			const jchar *lPtrStringContainerTypeGUID = aPtrEnv->GetStringChars(aStringMediaType, nullptr);

			CLSID lMediaTypeGUID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringContainerTypeGUID, &lMediaTypeGUID);

			if (FAILED(lhr))
				break;

			LONG lStride = 0;
			
			lhr = lObject->getStrideForBitmap(
				lMediaTypeGUID,
				aWidthInPixels,
				&lStride);

			if (FAILED(lhr))
				break;
			
			lresult = (jint)lStride;

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif