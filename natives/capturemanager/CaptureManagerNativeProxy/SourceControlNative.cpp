#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_SourceControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_SourceControlNative
	* Method:    createSourceControl
	* Signature: (JLjava/lang/String;Ljava/lang/String;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SourceControlNative_createSourceControl
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aSymbolicLink, jstring aStringIID)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aSymbolicLink == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISourceControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;


			const jchar *lPtrStringIID = aPtrEnv->GetStringChars(aStringIID, nullptr);

			CLSID lIID;

			lhr = CLSIDFromString((wchar_t*)lPtrStringIID, &lIID);

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lControl;

			const jchar *lPtrSymbolicLink = aPtrEnv->GetStringChars(aSymbolicLink, nullptr);

			lhr = lObject->createSourceControl(
				(wchar_t*)lPtrSymbolicLink,
				lIID,
				&lControl);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lControl.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SourceControlNative
	* Method:    createSourceNode
	* Signature: (JLjava/lang/String;II)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SourceControlNative_createSourceNode
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aSymbolicLink, jint aStreamIndex, jint aMediaTypeIndex)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aSymbolicLink == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISourceControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			CComPtrCustom<IUnknown> lSourceNode;

			const jchar *lPtrSymbolicLink = aPtrEnv->GetStringChars(aSymbolicLink, nullptr);

			lhr = lObject->createSourceNode(
				(wchar_t*)lPtrSymbolicLink,
				aStreamIndex,
				aMediaTypeIndex,
				&lSourceNode);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lSourceNode.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SourceControlNative
	* Method:    createSourceNodeWithDownStreamConnection
	* Signature: (JLjava/lang/String;IIJ)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SourceControlNative_createSourceNodeWithDownStreamConnection
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aSymbolicLink, jint aStreamIndex, jint aMediaTypeIndex, jlong aDownStreamPtr)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aDownStreamPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISourceControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			IUnknown* lPtrDownStream = (IUnknown*)aDownStreamPtr;

			CComPtrCustom<IUnknown> lSourceNode;
			
			const jchar *lPtrSymbolicLink = aPtrEnv->GetStringChars(aSymbolicLink, nullptr);

			lhr = lObject->createSourceNodeWithDownStreamConnection(
				(wchar_t*)lPtrSymbolicLink,
				aStreamIndex,
				aMediaTypeIndex,
				lPtrDownStream,
				&lSourceNode);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lSourceNode.detach();

		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SourceControlNative
	* Method:    getCollectionOfSources
	* Signature: (J)Ljava/lang/String;
	*/
	JNIEXPORT jstring JNICALL Java_capturemanager_classes_SourceControlNative_getCollectionOfSources
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

			CComPtrCustom<ISourceControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;

			BSTR lXMLstring = nullptr;

			lhr = lObject->getCollectionOfSources(&lXMLstring);

			if (FAILED(lhr))
				break;

			auto lLength = SysStringLen(lXMLstring);

			lresult = aPtrEnv->NewString((jchar*)lXMLstring, lLength);

			SysFreeString(lXMLstring);
			
		} while (false);

		return lresult;
	}

	/*
	* Class:     capturemanager_classes_SourceControlNative
	* Method:    getSourceOutputMediaType
	* Signature: (JLjava/lang/String;II)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_SourceControlNative_getSourceOutputMediaType
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jstring aSymbolicLink, jint aStreamIndex, jint aMediaTypeIndex)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aSymbolicLink == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<ISourceControl> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;
			
			CComPtrCustom<IUnknown> lMediaType;

			const jchar *lPtrSymbolicLink = aPtrEnv->GetStringChars(aSymbolicLink, nullptr);

			lhr = lObject->getSourceOutputMediaType(
				(wchar_t*)lPtrSymbolicLink,
				aStreamIndex,
				aMediaTypeIndex,
				&lMediaType);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lMediaType.detach();

		} while (false);

		return lresult;
	}

#ifdef __cplusplus
}
#endif