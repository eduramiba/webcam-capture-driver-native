package capturemanager.interfaces;

public interface ISourceControl {
	IMediaType getSourceOutputMediaType(
            String aSymbolicLink,
            int aIndexStream,
            int aIndexMediaType);

//    IStreamNode createSource(
//    		String aSymbolicLink);

    IStreamNode createSourceNode(
    		String aSymbolicLink, 
            int aIndexStream, 
            int aIndexMediaType, 
            IStreamNode aPtrDownStreamTopologyNode);

    IStreamNode createSourceNode(
    		String aSymbolicLink, 
            int aIndexStream, 
            int aIndexMediaType);

//    IStreamNode createSourceNodeFromExternalSource(
//            object aPtrMediaSource, 
//            uint aIndexStream, 
//            uint aIndexMediaType, 
//            out object aPtrPtrTopologyNode);

//    IStreamNode createSourceNodeFromExternalSourceWithDownStreamConnection(
//            object aPtrMediaSource,
//            uint aIndexStream, 
//            uint aIndexMediaType, 
//            object aPtrDownStreamTopologyNode, 
//            out object aPtrPtrTopologyNode);

    String getCollectionOfSources();
        
//    IStreamNode getSourceOutputMediaTypeFromMediaSource(
//            object aPtrMediaSource, 
//            uint aIndexStream, 
//            uint aIndexMediaType, 
//            out object aPtrPtrOutputMediaType);

//        IWebCamControl createWebCamControl(string aSymbolicLink);
}
