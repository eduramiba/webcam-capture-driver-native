package capturemanager.interfaces;

public interface IEncoderNodeFactory {
	
	IMediaType createCompressedMediaType(
    		IMediaType aPtrUncompressedMediaType,
    		String aStringEncodingModeGUID, 
    		int aEncodingModeValue,
    		int aIndexCompressedMediaType);
 
	IStreamNode createEncoderNode(
    		IMediaType aPtrUncompressedMediaType, 
    		String aStringEncodingModeGUID,
    		int aEncodingModeValue,
    		int aIndexCompressedMediaType,
    		IStreamNode aPtrDownStreamNode);

}
