package capturemanager.classes;

import capturemanager.interfaces.IWebCamControl;

final class WebCamControl extends WebCamControlNative implements IWebCamControl {

	WebCamControl(long aPtr) {
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
	public String getCamParametrs() {
		String lresult = "";
		
		do
		{
			lresult = getCamParametrs(mPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public void setCamParametr(
			int aParametrIndex,
			int aNewValue,
			int aFlag) {
		
		do
		{
			if(mPtr == 0)
				break;
			
			setCamParametr(
					mPtr,
		    		aParametrIndex,
		    		aNewValue,
		    		aFlag);
		}
		while(false);
		
	}

}
