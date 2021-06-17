package capturemanager.classes;

import capturemanager.interfaces.ILogPrintOutControl;

class LogPrintOutControl extends LogPrintOutControlNative implements ILogPrintOutControl {

    protected static final String CLSID_CoLogPrintOut = "{4563EE3E-DA1E-4911-9F40-88A284E2DD69}";

    protected static final String IID_ILogPrintOutControl = "{73B67834-E7BD-40B7-9730-8C13BF098B9F}";

	LogPrintOutControl(long aPtr) {
		mPtr = aPtr;
	}
	
	protected long mPtr = 0;

	public void release() 
	{	
		  if(mPtr != 0)
			  CaptureManagerNativeProxy.getInstance().Release(mPtr);
		  
		  mPtr = 0;		  
	}

	@Override
	public void addPrintOutDestination(int aLevelType, String aFilePath) {

		if(mPtr != 0)
			addPrintOutDestinationNative(
					mPtr,
					aLevelType,
					aFilePath);
		
	}

	@Override
	public void removePrintOutDestination(int aLevelType, String aFilePath) {

		if(mPtr != 0)
			removePrintOutDestinationNative(
					mPtr,
					aLevelType,
					aFilePath);
		
	}

	@Override
	public void setVerbose(int aLevelType, String aFilePath, Boolean aState) {

		if(mPtr != 0)
			setVerboseNative(
					mPtr,
					aLevelType,
					aFilePath,
					aState);
	}
	

}
