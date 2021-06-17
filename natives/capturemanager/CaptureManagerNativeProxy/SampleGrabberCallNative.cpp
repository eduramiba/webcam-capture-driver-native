#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_SampleGrabberCallNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SampleGrabberCallNative
	* Method:    readData
	* Signature: (J[B)I
	*/
	JNIEXPORT jint JNICALL Java_capturemanager_classes_SampleGrabberCallNative_readData
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jobject byteBuffer)
	{
		jint lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (byteBuffer == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISampleGrabberCall> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			void* bufferAddress = aPtrEnv->GetDirectBufferAddress(byteBuffer);
			
			if (bufferAddress == nullptr)
				break;

			DWORD lReadSize;

			lhr = lObject->readData(
				bufferAddress,
				&lReadSize);

			if (FAILED(lhr))
				break;
			
			lresult = (jint) lReadSize;

		} while (false);

		return lresult;
	}

	JNIEXPORT void JNICALL Java_capturemanager_classes_SampleGrabberCallNative_RGB32ToBGRA
	(JNIEnv* aPtrEnv, jobject aClass,  jobject byteBuffer)
	{
		jbyte* bufferAddress = (jbyte*) aPtrEnv->GetDirectBufferAddress(byteBuffer);

		if (bufferAddress == nullptr)
			return;

		jlong capacity = aPtrEnv->GetDirectBufferCapacity(byteBuffer);

		//We just actually need to fill the alpha, all other values are in place for Java's BGRA. RGB32 does not set alpha (is 0).
		for (jlong i = 0; i < capacity; i += 4) {
			bufferAddress[i + 3] = 0xFF;//Alpha = 1.0
		}
	}

#ifdef __cplusplus
}
#endif