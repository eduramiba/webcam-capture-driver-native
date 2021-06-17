package capturemanager.classes;

import capturemanager.interfaces.*;


final class SampleGrabberCallSinkFactory extends SampleGrabberCallSinkFactoryNative
		implements ISampleGrabberCallSinkFactory {

	SampleGrabberCallSinkFactory(long aPtr)  {
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
	public ISampleGrabberCall createOutputNode(
			String aStringMajorType, 
			String aStringSubType,
			int aSampleByteSize) {

		ISampleGrabberCall lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
		
			long lPtr = createOutputNode(
					mPtr,
					aStringMajorType,
					aStringSubType, 
					aSampleByteSize,
					"{118AD3F7-D9A3-4146-AB35-F16421DC995E}");
			
			SampleGrabberCall lSampleGrabberCall = new SampleGrabberCall(lPtr);
						
			lresult = lSampleGrabberCall;
		}
		while(false);
		
		return lresult;
	}

}
