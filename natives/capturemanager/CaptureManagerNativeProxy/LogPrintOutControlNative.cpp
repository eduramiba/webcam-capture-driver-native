#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>


#include "JNI\capturemanager_classes_LogPrintOutControlNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_LogPrintOutControlNative
	* Method:    addPrintOutDestinationNative
	* Signature: (JILjava/lang/String;)V
	*/
	JNIEXPORT void JNICALL Java_capturemanager_classes_LogPrintOutControlNative_addPrintOutDestinationNative
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jint aLevel, jstring aFilePath)
	{
		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;
						
			ILogPrintOutControl* lPtrILogPrintOutControl = (ILogPrintOutControl*)aPtr;

			const jchar *lPtrFilePath = aPtrEnv->GetStringChars(aFilePath, nullptr);

			HRESULT lres = lPtrILogPrintOutControl->addPrintOutDestination(
				aLevel,
				(BSTR)lPtrFilePath);

			if (FAILED(lres))
			{
				lres = E_FAIL;
			}

		} while (false);
	}
	/*
	* Class:     capturemanager_classes_LogPrintOutControlNative
	* Method:    removePrintOutDestinationNative
	* Signature: (JILjava/lang/String;)V
	*/
	JNIEXPORT void JNICALL Java_capturemanager_classes_LogPrintOutControlNative_removePrintOutDestinationNative
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jint aLevel, jstring aFilePath)
	{
		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			ILogPrintOutControl* lPtrILogPrintOutControl = (ILogPrintOutControl*)aPtr;

			const jchar *lPtrFilePath = aPtrEnv->GetStringChars(aFilePath, nullptr);

			lPtrILogPrintOutControl->removePrintOutDestination(
				aLevel,
				(BSTR)lPtrFilePath);

		} while (false);
	}

	/*
	* Class:     capturemanager_classes_LogPrintOutControlNative
	* Method:    setVerboseNative
	* Signature: (JILjava/lang/String;Ljava/lang/Boolean;)V
	*/
	JNIEXPORT void JNICALL Java_capturemanager_classes_LogPrintOutControlNative_setVerboseNative
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jint aLevel, jstring aFilePath, jboolean aState)
	{
		do
		{
			if (aPtr == 0)
				break;

			if (aPtrEnv == nullptr)
				break;

			ILogPrintOutControl* lPtrILogPrintOutControl = (ILogPrintOutControl*)aPtr;

			const jchar *lPtrFilePath = aPtrEnv->GetStringChars(aFilePath, nullptr);

			lPtrILogPrintOutControl->setVerbose(
				aLevel,
				(BSTR)lPtrFilePath,
				aState);

		} while (false);
	}

#ifdef __cplusplus
}
#endif