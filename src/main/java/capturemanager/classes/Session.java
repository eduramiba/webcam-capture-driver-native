package capturemanager.classes;

import capturemanager.interfaces.ISession;
import capturemanager.interfaces.IUpdateStateListener;

final class Session extends SessionNative implements ISession {


	Session(long aPtr)  {
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
	public boolean startSession(
			long aStartPositionInHundredNanosecondUnits,
			String aStringGUIDTimeFormat) {
		boolean lresult = false;
		
		do
		{
			if(mPtr == 0)
				break;
			
			lresult = startSession(
					mPtr,
					aStartPositionInHundredNanosecondUnits,
					aStringGUIDTimeFormat);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public boolean pauseSession() {
		boolean lresult = false;
		
		do
		{
			if(mPtr == 0)
				break;
			
			lresult = pauseSession(
					mPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public boolean stopSession() {
		boolean lresult = false;
		
		do
		{
			if(mPtr == 0)
				break;
			
			lresult = stopSession(
					mPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public boolean closeSession() {
		boolean lresult = false;
		
		do
		{
			if(mPtr == 0)
				break;
			
			lresult = closeSession(
					mPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public int getSessionDescriptor() {
		int lresult = -1;
		
		do
		{
			if(mPtr == 0)
				break;
			
			lresult = getSessionDescriptor(
					mPtr);
		}
		while(false);
		
		return lresult;
	}

	@Override
	public void addUpdateStateListener(IUpdateStateListener aIUpdateStateListener) {

		do
		{
			if(mPtr == 0)
				break;
			
			addIUpdateStateListener(
					mPtr,
					aIUpdateStateListener);
		}
		while(false);

	}

}
