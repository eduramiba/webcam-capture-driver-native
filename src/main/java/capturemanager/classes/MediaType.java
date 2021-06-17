package capturemanager.classes;

import capturemanager.interfaces.IMediaType;

final class MediaType implements IMediaType {

	MediaType(long aPtr) {
		mPtr = aPtr;
	}
	
	protected long mPtr = 0;
    
	@Override
	protected void finalize() throws Throwable 
	{
		  super.finalize();
	
		  if(mPtr != 0)
			  CaptureManagerNativeProxy.getInstance().Release(mPtr);
		  
		  mPtr = 0;		  
	}	
}
