package capturemanager.classes;

abstract class SourceControlNative {

//    native protected long createSource(
//    		String aSymbolicLink,
//    		out object aPtrPtrMediaSource);
  
    native protected long createSourceControl(
    		long aPtr,
    		String aSymbolicLink, 
    		String aStringIID);
    
//    void createSourceFromCaptureProcessor(object aPtrCaptureProcessor, out object aPtrPtrMediaSource);
 
    native protected long createSourceNode(
    		long aPtr,
    		String aSymbolicLink,
    		int aIndexStream,
    		int aIndexMediaType);

//    void createSourceNodeFromExternalSource(object aPtrMediaSource, uint aIndexStream, uint aIndexMediaType, out object aPtrPtrTopologyNode);
    
//    void createSourceNodeFromExternalSourceWithDownStreamConnection(object aPtrMediaSource, uint aIndexStream, uint aIndexMediaType, object aPtrDownStreamTopologyNode, out object aPtrPtrTopologyNode);
   
    native protected long createSourceNodeWithDownStreamConnection(
    		long aPtr,
    		String aSymbolicLink,
    		int aIndexStream,
    		int aIndexMediaType,
    		long aPtrDownStreamTopologyNode);
 
    native protected String getCollectionOfSources(
    		long aPtr);
   
    native protected long getSourceOutputMediaType(
    		long aPtr,
    		String aSymbolicLink, 
    		int aIndexStream, 
    		int aIndexMediaType);

//    void getSourceOutputMediaTypeFromMediaSource(object aPtrMediaSource, uint aIndexStream, uint aIndexMediaType, out object aPtrPtrOutputMediaType);
	
}
