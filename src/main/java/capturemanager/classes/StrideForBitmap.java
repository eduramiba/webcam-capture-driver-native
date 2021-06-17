package capturemanager.classes;

import capturemanager.interfaces.IStrideForBitmap;

class StrideForBitmap extends StrideForBitmapNative implements IStrideForBitmap {

    protected static final String IID = "{74D903C9-69E6-4FC7-BF7A-9F47605C52BE}";

	StrideForBitmap(long aPtr) {
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

	@Override
	public int getStrideForBitmap(String aStringMFVideoFormat, int aWidthInPixels) {

		int lresult = 0;

		do
		{			
			  if(mPtr == 0)
				  break;
			  
			  if(aStringMFVideoFormat == null)
				  break;
			  			  
			  lresult = getStrideForBitmap(
					  mPtr,
					  aStringMFVideoFormat,
					  aWidthInPixels);
		}
		while(false);
		
		return lresult;
	}

}
