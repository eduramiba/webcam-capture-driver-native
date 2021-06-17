package capturemanager.classes;

import capturemanager.interfaces.ISpreaderNodeFactory;
import capturemanager.interfaces.IStreamNode;
import java.util.List;

final class SpreaderNodeFactory extends SpreaderNodeFactoryNative implements ISpreaderNodeFactory {

	SpreaderNodeFactory(long aPtr)  {
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
	public IStreamNode createSpreaderNode(List<IStreamNode> aDownStreamTopologyNodelist) {
		IStreamNode lresult = null;
		
		do
		{			
			  if(mPtr == 0)
				  break;
			  
			  if(aDownStreamTopologyNodelist == null)
				  break;
			  
			  long[] lDownStreamTopologyNodelist = new long[aDownStreamTopologyNodelist.size()];
			  
			  for(int lIndex = 0; lIndex < aDownStreamTopologyNodelist.size(); lIndex++)
			  {
				  
				  StreamNode lStreamNode = (StreamNode)aDownStreamTopologyNodelist.get(lIndex);
				  
				  if(lStreamNode != null)
					  lDownStreamTopologyNodelist[lIndex] = lStreamNode.mPtr;
			  }
			  
			  long lPtr = createSpreaderNode(
					  mPtr,
					  lDownStreamTopologyNodelist);
			  
			  if(lPtr == 0)
				  break;
			  
			  lresult = new StreamNode(lPtr);
		}
		while(false);
		
		return lresult;
	}

}
