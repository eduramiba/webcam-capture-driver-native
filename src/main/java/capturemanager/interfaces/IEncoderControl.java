package capturemanager.interfaces;

public interface IEncoderControl {
	IEncoderNodeFactory createEncoderNodeFactory(String aEncoderCLSID);
    String getCollectionOfEncoders();
    String getMediaTypeCollectionOfEncoder(IMediaType aPtrUncompressedMediaType, String aEncoderCLSID);
}
