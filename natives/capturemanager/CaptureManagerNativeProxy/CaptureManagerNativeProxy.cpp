#define WIN32_LEAN_AND_MEAN    


#include <windows.h>
#include <Commdlg.h>
#include <Objbase.h>


#include "JNI\capturemanager_classes_CaptureManagerNativeProxy.h"

#include "ComPtrCustom.h"

BOOL gComInit = FALSE;

typedef HRESULT(STDAPICALLTYPE *PDllGetClassObject) (REFCLSID, REFIID, void**);

#ifdef __cplusplus
extern "C" {
#endif

	/*
	* Class:     capturemanager_classes_CaptureManagerNativeProxy_getPtrClass
	* Method:    explicitGetPtrClass
	* Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_CaptureManagerNativeProxy_explicitGetPtrClass
		(JNIEnv * aPtrEnv, jobject aClass, jstring aStringFilePath, jstring aStringCLSID, jstring aStringGUID)
	{
		jlong lresult = 0;

		do
		{
			HRESULT lhresult(E_FAIL);
			
			gComInit = TRUE;

			HMODULE lhLibrary = nullptr;

			if (lhLibrary == nullptr)
			{
				const jchar *lPtrStringFilePath = aPtrEnv->GetStringChars(aStringFilePath, nullptr);
				
				lhLibrary = GetModuleHandle((wchar_t*)lPtrStringFilePath);

				if (lhLibrary == nullptr)
					break;
			}

			PDllGetClassObject lPtrFuncDllGetClassObject = nullptr;

			lPtrFuncDllGetClassObject = (PDllGetClassObject)GetProcAddress(lhLibrary, "DllGetClassObject");

			if (lPtrFuncDllGetClassObject == nullptr)
				break;

			const jchar *lPtrStringCLSID = aPtrEnv->GetStringChars(aStringCLSID, nullptr);

			CLSID lCLSID;

			lhresult = CLSIDFromString((wchar_t*)lPtrStringCLSID, &lCLSID);

			if (FAILED(lhresult))
				break;

			CComPtrCustom<IClassFactory> lClassFactory;

			lhresult = lPtrFuncDllGetClassObject(lCLSID, IID_PPV_ARGS(&lClassFactory));

			if (FAILED(lhresult))
				break;

			if (!lClassFactory)
				break;

			const jchar *lPtrStringGUID = aPtrEnv->GetStringChars(aStringGUID, nullptr);

			CLSID lInterfaceID;

			lhresult = CLSIDFromString((wchar_t*)lPtrStringGUID, &lInterfaceID);

			if (FAILED(lhresult))
				break;

			CComPtrCustom<IUnknown> lIUnknown;

			lhresult = lClassFactory->CreateInstance(
				nullptr,
				lInterfaceID,
				(void**)&lIUnknown);

			if (FAILED(lhresult))
				break;

			if (!lIUnknown)
				break;

			lresult = (jlong)lIUnknown.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_CaptureManagerNativeProxy_getPtrClass
	* Method:    getPtrClass
	* Signature: (Ljava/lang/String;Ljava/lang/String;)J
	*/
	JNIEXPORT void JNICALL Java_capturemanager_classes_CaptureManagerNativeProxy_freeLibrary
		(JNIEnv * aPtrEnv, jobject aClass, jstring aFileName)
	{
		do
		{
			HMODULE lhLibrary = nullptr;

			const jchar *lPtrFileName = aPtrEnv->GetStringChars(aFileName, nullptr);
			
			lhLibrary = GetModuleHandle((wchar_t*)lPtrFileName);

			if (lhLibrary == nullptr)
				break;

			BOOL lresult = FreeLibrary(lhLibrary);

			while (lresult != FALSE)
			{
				lresult = FreeLibrary(lhLibrary);
			}
			
		} while (false);
	}

	/*
	* Class:     capturemanager_classes_CaptureManagerNativeProxy_Release
	* Method:    Release
	* Signature: (J)V
	*/
	JNIEXPORT void JNICALL Java_capturemanager_classes_CaptureManagerNativeProxy_Release
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr)
	{
		do
		{
			if (aPtr != 0)
			{
				IUnknown* aPtrIUnknown = (IUnknown*)aPtr;

				if (aPtrIUnknown != nullptr)
					aPtrIUnknown->Release();
			}

		} while (false);
	}

#ifdef __cplusplus
}
#endif