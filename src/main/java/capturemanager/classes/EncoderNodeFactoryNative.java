package capturemanager.classes;

abstract class EncoderNodeFactoryNative {

	native protected long createCompressedMediaType(
			long aPtr,
    		long aPtrUncompressedMediaType,
    		String aStringEncodingModeGUID, 
    		int aEncodingModeValue, 
    		int aIndexCompressedMediaType);
  
	native protected long createEncoderNode(
			long aPtr,
			long aPtrUncompressedMediaType,
			String aStringEncodingModeGUID,
			int aEncodingModeValue,
			int aIndexCompressedMediaType,
			long aPtrDownStreamNode);

}
