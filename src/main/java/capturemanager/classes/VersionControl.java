package capturemanager.classes;

import capturemanager.interfaces.IVersionControl;

final class VersionControl extends VersionControlNative implements IVersionControl {

    protected static final String IID = "{39DC3AEF-3B59-4C0D-A1B2-54BF2653C056}";

	VersionControl(long aPtr) {
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
	public String getXMLStringVersion() {
		return getXMLStringVersion(mPtr);
	}

}
