#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>
#include <vector>
#include <memory>


#include "jawt_md.h"
#include "JNI\capturemanager_classes_FileSinkFactoryNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif

	/*
	* Class:     capturemanager_classes_FileSinkFactoryNative
	* Method:    createOutputNodes
	* Signature: (J[JLjava/lang/String;)[J
	*/
	JNIEXPORT jlongArray JNICALL Java_capturemanager_classes_FileSinkFactoryNative_createOutputNodes
		(JNIEnv * aPtrEnv, 
		jobject aClass, 
		jlong aPtr,
		jlongArray aArrayPtrCompressedMediaTypes,
		jstring aPtrFileName)
	{
		jlongArray lresult = nullptr;

		do
		{
			if (aPtr == 0)
				break;

			if (aArrayPtrCompressedMediaTypes == nullptr)
				break;

			if (aPtrEnv == nullptr)
				break;

			if (aPtrFileName == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IFileSinkFactory> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;



			const jchar *lPtrFileName = aPtrEnv->GetStringChars(aPtrFileName, nullptr);




			auto lPtrSourceNodesOfTopologys = aPtrEnv->GetLongArrayElements(aArrayPtrCompressedMediaTypes, nullptr);

			if (lPtrSourceNodesOfTopologys == nullptr)
				break;

			auto lArrayLength = aPtrEnv->GetArrayLength(aArrayPtrCompressedMediaTypes);

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




			VARIANT theOutputNodes;

			VariantInit(&theOutputNodes);



			CComPtrCustom<IUnknown> lSession;

			lhr = lObject->createOutputNodes(
				theArray,
				(wchar_t*)lPtrFileName,
				&theOutputNodes);

			SafeArrayDestroy(pSA);

			VariantClear(&theArray);
			
			std::vector<IUnknown*> lCompressedMediaTypes;

			if (theOutputNodes.vt == VT_SAFEARRAY | VT_UNKNOWN && theOutputNodes.parray != nullptr)
			{
				LONG lBoundMediaTypes(0);

				LONG uBoundMediaTypes(0);

				SafeArrayGetUBound( theOutputNodes.parray, 1, &uBoundMediaTypes);

				SafeArrayGetLBound( theOutputNodes.parray, 1, &lBoundMediaTypes);

				for (LONG lIndex = lBoundMediaTypes; lIndex <= uBoundMediaTypes; lIndex++)
				{
					VARIANT lVar;

					auto lr = SafeArrayGetElement(theOutputNodes.parray, &lIndex, &lVar);

					if (SUCCEEDED(lr) && lVar.vt == VT_UNKNOWN && lVar.punkVal != nullptr)
					{
						lCompressedMediaTypes.push_back(lVar.punkVal);
					}

					//VariantClear(&lVar);
				}
				
				SafeArrayDestroy(theOutputNodes.parray);
				
				theOutputNodes.parray = nullptr;
			}
			

			VariantClear(&theOutputNodes);

			if (FAILED(lhr))
				break;


			lresult = aPtrEnv->NewLongArray(lCompressedMediaTypes.size());

			if (lresult == nullptr)
				break;

			jlong* l_longs = new jlong[lCompressedMediaTypes.size()];

			int l_index = 0;

			for (auto& l_item : lCompressedMediaTypes)
			{
				l_longs[l_index++] = (jlong)l_item;
			}

			aPtrEnv->SetLongArrayRegion(lresult, 0, lCompressedMediaTypes.size(), l_longs);
			
			delete[] l_longs;

		} while (false);

		return lresult;

	}

#ifdef __cplusplus
}
#endif