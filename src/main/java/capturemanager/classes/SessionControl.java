package capturemanager.classes;

import capturemanager.interfaces.ISession;
import capturemanager.interfaces.ISessionControl;
import capturemanager.interfaces.IStreamNode;
import java.util.List;

final class SessionControl extends SessionControlNative implements ISessionControl {

    protected static final String IID = "{D0C58520-A941-4C0F-81B0-3ED8A4DE11ED}";


	SessionControl(long aPtr)  {
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
	public ISession createSession(
			List<IStreamNode> aArrayPtrSourceNodesOfTopology) {

		ISession lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			long[] lPtrSourceNodesOfTopologys = new long[aArrayPtrSourceNodesOfTopology.size()];
			
			for(int lIndex = 0; lIndex < aArrayPtrSourceNodesOfTopology.size(); ++lIndex)
			{
				StreamNode lStreamNode = (StreamNode)aArrayPtrSourceNodesOfTopology.get(lIndex);
				
				if(lStreamNode == null)
					continue;
				
				lPtrSourceNodesOfTopologys[lIndex] = lStreamNode.mPtr;
			}
			
			long lPtr = createSession(
					mPtr,
					lPtrSourceNodesOfTopologys,
					"{742AC001-D1E0-40A8-8EFE-BA1A550F8805}");

			if(lPtr == 0)
				break;
			
			lresult = new Session(lPtr);
		}
		while(false);
		
		return lresult;
	}

}
