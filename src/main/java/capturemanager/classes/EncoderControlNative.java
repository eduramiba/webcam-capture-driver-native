package capturemanager.classes;

abstract class EncoderControlNative {
		    		
	native protected long createEncoderNodeFactory(
			long aPtr,
			String aStringEncoderCLSID,
			String aStringIID);

    native protected String getCollectionOfEncoders(
			long aPtr);

    native protected String getMediaTypeCollectionOfEncoder(
			long aPtr,
    		long aPtrUncompressedMediaType,
    		String aStringEncoderCLSID);
}
