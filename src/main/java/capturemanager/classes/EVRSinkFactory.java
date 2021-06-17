package capturemanager.classes;

import capturemanager.interfaces.IEVRSinkFactory;
import capturemanager.interfaces.IStreamNode;
import java.awt.Component;

final class EVRSinkFactory extends EVRSinkFactoryNative implements IEVRSinkFactory {


	EVRSinkFactory(long aPtr)  {
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
	public IStreamNode createOutputNode(Component aGraphicComponent) {

		IStreamNode lresult = null;
		
		do
		{
			long lPtr = createOutputNode(
					mPtr,
					aGraphicComponent);
			
			if(lPtr == 0)
				break;
			
			lresult = new StreamNode(lPtr);
		}
		while(false);
		
		return lresult;
	}
	


}
