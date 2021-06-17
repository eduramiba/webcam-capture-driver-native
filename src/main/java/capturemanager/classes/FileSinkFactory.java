package capturemanager.classes;

import capturemanager.interfaces.IFileSinkFactory;
import capturemanager.interfaces.IMediaType;
import capturemanager.interfaces.IStreamNode;
import java.util.ArrayList;
import java.util.List;

final class FileSinkFactory extends FileSinkFactoryNative implements IFileSinkFactory {

	FileSinkFactory(long aPtr)  {
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
	public List<IStreamNode> createOutputNodes(
			List<IMediaType> aArrayPtrCompressedMediaTypes, 
			String aPtrFileName) {

		List<IStreamNode> lresult = new ArrayList<IStreamNode>();
		
		do
		{
			if(mPtr == 0)
				break;
			
			if(aArrayPtrCompressedMediaTypes == null)
				break;
			
			long[] PtrCompressedMediaTypes = new long[aArrayPtrCompressedMediaTypes.size()];
			
			for(int lIndex = 0; lIndex < aArrayPtrCompressedMediaTypes.size(); lIndex++)
			{
				MediaType lMediaType = (MediaType)aArrayPtrCompressedMediaTypes.get(lIndex);
				
				if(lMediaType != null)				
					PtrCompressedMediaTypes[lIndex] = lMediaType.mPtr;
			}
									
			long[] lPtrOutputNodes = super.createOutputNodes(
					mPtr, 
					PtrCompressedMediaTypes, 
					aPtrFileName);
			
			if(lPtrOutputNodes == null)
				break;
						
			for(int lIndex = 0; lIndex < lPtrOutputNodes.length; lIndex++)
			{				
				lresult.add(new StreamNode(lPtrOutputNodes[lIndex]));
			}		
			
		}
		while(false);
				
		return lresult;
	}

}
