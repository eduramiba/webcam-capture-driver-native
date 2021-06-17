package capturemanager.classes;

import capturemanager.interfaces.ISpreaderNodeFactory;
import capturemanager.interfaces.IStreamControl;

final class StreamControl extends StreamControlNative implements IStreamControl {

    protected static final String IID = "{E8F25B4A-8C71-4C9E-BD8C-82260DC4C21B}";

	StreamControl(long aPtr)  {
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
	public String getCollectionOfStreamControlNodeFactories() {
		String lresult = "";
		
		do
		{
			
			if(mPtr != 0)
				break;
			
			lresult = getCollectionOfStreamControlNodeFactories(mPtr);
			
		}
		while(false);
		
		return lresult;
	}

	@Override
	public ISpreaderNodeFactory createStreamControlNodeFactory() {
		ISpreaderNodeFactory lresult = null;
		
		do
		{
			
			if(mPtr != 0)
				break;
			
			long lPtr = createStreamControlNodeFactory(
					mPtr,
					"{85DFAAA1-4CC0-4A88-AE28-8F492E552CCA}");

			if(lPtr != 0)
				break;
			
			lresult = new SpreaderNodeFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public ISpreaderNodeFactory createStreamControlNodeFactory(String aStringIID) {
		ISpreaderNodeFactory lresult = null;
		
		do
		{
			
			if(mPtr != 0)
				break;
			
			long lPtr = createStreamControlNodeFactory(
					mPtr,
					aStringIID);

			if(lPtr != 0)
				break;
			
			lresult = new SpreaderNodeFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

}
