package capturemanager.interfaces;

public interface ISinkControl {
	
	IFileSinkFactory createFileSinkFactory(
	            String aStringContainerTypeGUID);
	        
//	IByteStreamSinkFactory createSinkFactory(
//	        		String aStringContainerTypeGUID);       

	ISampleGrabberCallSinkFactory createSampleGrabberCallSinkFactory(
	        		String aStringContainerTypeGUID);

	ISampleGrabberCallbackSinkFactory createSampleGrabberCallbackSinkFactory(
	        		String aStringContainerTypeGUID);

	IEVRSinkFactory createEVRSinkFactory(
	        		String aStringContainerTypeGUID);  
    
    String getCollectionOfSinks();
}
