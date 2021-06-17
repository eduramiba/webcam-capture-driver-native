package capturemanager.interfaces;

public interface ICaptureManagerNativeProxy {
		
    public long explicitGetPtrClass(String aStringFilePath, String aStringCLSID, String aStringGUID);  
    
    public void freeLibrary(String aFileName);  
 
    public void Release(long aPtr);
}
