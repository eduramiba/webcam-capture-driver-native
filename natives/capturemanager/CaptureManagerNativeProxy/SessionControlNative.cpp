#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>

#include "jawt_md.h"
#include "JNI\capturemanager_classes_SessionControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SessionControlNative
	* Method:    createSession
	* Signature: (J[JLjava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SessionControlNative_createSession
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jlongArray aPtrSourceNodesOfTopologys, jstring aStringIID)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aClass == nullptr)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aPtrSourceNodesOfTopologys == nullptr)
				break;

			if (aStringIID == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISessionControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;


			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lInterfaceID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lInterfaceID);

			if (FAILED(lhr))
				break;


			auto lPtrSourceNodesOfTopologys = aPtrEnv->GetLongArrayElements(aPtrSourceNodesOfTopologys, nullptr);

			if (lPtrSourceNodesOfTopologys == nullptr)
				break;

			auto lArrayLength = aPtrEnv->GetArrayLength(aPtrSourceNodesOfTopologys);

			SAFEARRAY* pSA = NULL;
			SAFEARRAYBOUND bound[1];
			bound[0].lLbound = 0;
			bound[0].cElements = lArrayLength;
			pSA = SafeArrayCreate(VT_VARIANT, 1, bound);

			for (long i = 0; i < lArrayLength; i++)
			{
				jlong lPtr = lPtrSourceNodesOfTopologys[i];

				if (lPtr == 0)
					continue;

				IUnknown* lPtrIUnknown = (IUnknown*)lPtr;
				
				VARIANT lVar;

				VariantInit(&lVar);

				lVar.vt = VT_UNKNOWN;

				lVar.punkVal = lPtrIUnknown;
				
				lhr = SafeArrayPutElement(pSA, &i, &lVar);

				if (FAILED(lhr))
					break;

			}

			if (FAILED(lhr))
				break;



			VARIANT theArray;

			VariantInit(&theArray);

			theArray.vt = VT_SAFEARRAY | VT_UNKNOWN;

			theArray.parray = pSA;



			CComPtrCustom<IUnknown> lSession;

			lhr = lObject->createSession(
				theArray,
				lInterfaceID,
				&lSession);

			SafeArrayDestroy(pSA);

			VariantClear(&theArray);

			if (FAILED(lhr))
				break;
			
			lresult = (jlong)lSession.detach();

		} while (false);

		return lresult;

	};


#ifdef __cplusplus
}
#endif