package capturemanager.classes;

import capturemanager.interfaces.*;

final class EncoderNodeFactory extends EncoderNodeFactoryNative implements IEncoderNodeFactory {

	EncoderNodeFactory(long aPtr)  {
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
	public IMediaType createCompressedMediaType(
			IMediaType aPtrUncompressedMediaType, 
			String aStringEncodingModeGUID,
			int aEncodingModeValue, 
			int aIndexCompressedMediaType) {



		IMediaType lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			MediaType lMediaType = (MediaType)aPtrUncompressedMediaType;
			
			if(lMediaType == null)
				break;
						
			long lPtr = super.createCompressedMediaType(
					mPtr, 
					lMediaType.mPtr, 
					aStringEncodingModeGUID,
					aEncodingModeValue,
					aIndexCompressedMediaType);
			if(lPtr == 0)
				break;
			
			lresult = new MediaType(lPtr);
		}
		while(false);
				
		return lresult;
		
	}

	@Override
	public IStreamNode createEncoderNode(
			IMediaType aPtrUncompressedMediaType, 
			String aStringEncodingModeGUID,
			int aEncodingModeValue, 
			int aIndexCompressedMediaType, 
			IStreamNode aPtrDownStreamNode) {


		IStreamNode lresult = null;
		
		do
		{
			if(mPtr == 0)
				break;
			
			MediaType lMediaType = (MediaType)aPtrUncompressedMediaType;
			
			if(lMediaType == null)
				break;
			
			StreamNode lStreamNode = (StreamNode)aPtrDownStreamNode;
			
			if(lStreamNode == null)
				break;
						
			long lPtr = super.createEncoderNode(
					mPtr, 
					lMediaType.mPtr, 
					aStringEncodingModeGUID,
					aEncodingModeValue,
					aIndexCompressedMediaType,
					lStreamNode.mPtr);
			
			if(lPtr == 0)
				break;
			
			lresult = new StreamNode(lPtr);
		}
		while(false);
				
		return lresult;
	}

}
