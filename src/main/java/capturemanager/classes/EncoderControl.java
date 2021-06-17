package capturemanager.classes;

import capturemanager.interfaces.IEncoderControl;
import capturemanager.interfaces.IEncoderNodeFactory;
import capturemanager.interfaces.IMediaType;

class EncoderControl extends EncoderControlNative implements IEncoderControl {

    protected static final String IID = "{96223507-D8FF-4EC1-B125-71AA7F9726A4}";

	EncoderControl(long aPtr) {
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
	public IEncoderNodeFactory createEncoderNodeFactory(String aEncoderCLSID) {

		IEncoderNodeFactory lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long lPtr = createEncoderNodeFactory(
					mPtr,
					aEncoderCLSID,
					"{A56E11D8-D602-4792-8570-38C283FC0AA3}");

			if(lPtr == 0)
				break;
			
			lresult = new EncoderNodeFactory(lPtr);
		}
		while(false);
				
		return lresult;
	}

	@Override
	public String getCollectionOfEncoders() {
		return super.getCollectionOfEncoders(
				mPtr);
	}

	@Override
	public String getMediaTypeCollectionOfEncoder(
			IMediaType aPtrUncompressedMediaType, 
			String aEncoderCLSID) {
		

		String lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			MediaType lMediaType = (MediaType)aPtrUncompressedMediaType;
			
			if(lMediaType == null)
				break;
						
			lresult = super.getMediaTypeCollectionOfEncoder(
					mPtr, 
					lMediaType.mPtr, 
					aEncoderCLSID);
		}
		while(false);
				
		return lresult;

	}

}
