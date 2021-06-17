package capturemanager.classes;

abstract class SessionNative {

    native protected boolean closeSession(long aPtr);
    
    native protected boolean addIUpdateStateListener(
    		long aPtr,
    		Object aIUpdateStateListener);

    native protected int getSessionDescriptor(long aPtr);
   
    native protected boolean pauseSession(long aPtr);
    
    native protected boolean startSession(
    		long aPtr,
    		long aStartPositionInHundredNanosecondUnits, 
    		String aStringGUIDTimeFormat);
    
    native protected boolean stopSession(long aPtr);
}
