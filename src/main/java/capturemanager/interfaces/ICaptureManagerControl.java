package capturemanager.interfaces;

public interface ICaptureManagerControl {
    
	String getCollectionOfSources();
	
	String getCollectionOfSinks();
	
	ISourceControl createSourceControl();
	
	ISessionControl createSessionControl();
	
	ISinkControl createSinkControl();
	
	IStreamControl createStreamControl();
	
	IEncoderControl createEncoderControl();
	
    int getStrideForBitmapInfoHeader(
            String aStringMFVideoFormat,
            int aWidthInPixels);
    
    IVersionControl getVersionControl();
    
    void release();
}
