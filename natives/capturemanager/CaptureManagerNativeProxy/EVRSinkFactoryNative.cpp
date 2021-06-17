#define WIN32_LEAN_AND_MEAN    


#include <Unknwnbase.h>

#include "jawt_md.h"
#include "JNI\capturemanager_classes_EVRSinkFactoryNative.h"
#include "CaptureManagerTypeInfo.h"
#include "ComPtrCustom.h"

#ifdef __cplusplus
extern "C" {
#endif
	/*
	* Class:     capturemanager_classes_EVRSinkFactoryNative
	* Method:    createOutputNode
	* Signature: (JLjava/awt/Component;)J
	*/
	JNIEXPORT jlong JNICALL Java_capturemanager_classes_EVRSinkFactoryNative_createOutputNode
		(JNIEnv * aPtrEnv, jobject aClass, jlong aPtr, jobject aGraphicComponent)
	{
		jlong lresult = 0;

		do
		{
			if (aPtr == 0)
				break;

			if (aGraphicComponent == nullptr)
				break;

			if (aPtrEnv == nullptr)
				break;

			IUnknown* lPtrIUnknown = (IUnknown*)aPtr;

			CComPtrCustom<IEVRSinkFactory> lObject;

			HRESULT lhr = lPtrIUnknown->QueryInterface(IID_PPV_ARGS(&lObject));

			if (FAILED(lhr))
				break;			

			JAWT lJAWT;
			
			JAWT_DrawingSurface* lPtrJAWT_DrawingSurface;
			
			JAWT_DrawingSurfaceInfo* lPtrJAWT_DrawingSurfaceInfo;
			
			JAWT_Win32DrawingSurfaceInfo* lPtrJAWT_Win32DrawingSurfaceInfo;
			
			jboolean result;
			
			jint lock;
			
			
			lJAWT.version = JAWT_VERSION_1_3;

			if (JAWT_GetAWT(aPtrEnv, &lJAWT) == JNI_FALSE) 
				break;
					 
			lPtrJAWT_DrawingSurface = lJAWT.GetDrawingSurface(aPtrEnv, aGraphicComponent);
			
			lock = lPtrJAWT_DrawingSurface->Lock(lPtrJAWT_DrawingSurface);
		
			lPtrJAWT_DrawingSurfaceInfo = lPtrJAWT_DrawingSurface->GetDrawingSurfaceInfo(lPtrJAWT_DrawingSurface);
			
			lPtrJAWT_Win32DrawingSurfaceInfo = (JAWT_Win32DrawingSurfaceInfo*)lPtrJAWT_DrawingSurfaceInfo->platformInfo;

			HWND lHWND = lPtrJAWT_Win32DrawingSurfaceInfo->hwnd;

			CComPtrCustom<IUnknown> lEVRTopologyNode;

			lhr = lObject->createOutputNode(lHWND, &lEVRTopologyNode);

			lPtrJAWT_DrawingSurface->Unlock(lPtrJAWT_DrawingSurface);

			if (FAILED(lhr))
				break;

			lresult = (jlong)lEVRTopologyNode.detach();

		} while (false);

		return lresult;

	}

#ifdef __cplusplus
}
#endif