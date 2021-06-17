package capturemanager.classes;

import capturemanager.interfaces.IEVRSinkFactory;
import capturemanager.interfaces.IFileSinkFactory;
import capturemanager.interfaces.ISampleGrabberCallSinkFactory;
import capturemanager.interfaces.ISampleGrabberCallbackSinkFactory;
import capturemanager.interfaces.ISinkControl;

final class SinkControl extends SinkControlNative implements ISinkControl {

    protected static final String IID = "{C6BA3732-197E-438B-8E73-277759A7B58F}";


	SinkControl(long aPtr)  {
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
	public IFileSinkFactory createFileSinkFactory(String aStringContainerTypeGUID) {

		IFileSinkFactory lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long lPtr = createSinkFactory(
					mPtr,
					aStringContainerTypeGUID,
					"{D6E342E3-7DDD-4858-AB91-4253643864C2}");

			if(lPtr == 0)
				break;
			
			lresult = new FileSinkFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public ISampleGrabberCallSinkFactory createSampleGrabberCallSinkFactory(String aStringContainerTypeGUID) {

		ISampleGrabberCallSinkFactory lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long lPtr = createSinkFactory(
					mPtr,
					aStringContainerTypeGUID,
					"{759D24FF-C5D6-4B65-8DDF-8A2B2BECDE39}");

			if(lPtr == 0)
				break;
			
			lresult = new SampleGrabberCallSinkFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public ISampleGrabberCallbackSinkFactory createSampleGrabberCallbackSinkFactory(String aStringContainerTypeGUID) {

		ISampleGrabberCallbackSinkFactory lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long lPtr = createSinkFactory(
					mPtr,
					aStringContainerTypeGUID,
					"{3D64C48E-EDA4-4EE1-8436-58B64DD7CF13}");

			if(lPtr == 0)
				break;
			
			lresult = new SampleGrabberCallbackSinkFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public IEVRSinkFactory createEVRSinkFactory(String aStringContainerTypeGUID) {

		IEVRSinkFactory lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long lPtr = createSinkFactory(
					mPtr,
					aStringContainerTypeGUID,
					"{2F34AF87-D349-45AA-A5F1-E4104D5C458E}");

			if(lPtr == 0)
				break;
			
			lresult = new EVRSinkFactory(lPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public String getCollectionOfSinks() {
		String lresult = null;

		do
		{
			if(mPtr == 0)
				break;
			lresult = getCollectionOfSinks(mPtr);
		}
		while(false);		
		
		return lresult;
	}

}
