package capturemanager.classes;

abstract class CaptureManagerControlNative {

    native protected long createControl(
    		long aPtr,
    		String aStringIID);
    
    native protected long createMisc(
    		long aPtr,
    		String aStringIID);
}
