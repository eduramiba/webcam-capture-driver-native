package capturemanager.interfaces;

public interface ISession {
	boolean startSession(
            long aStartPositionInHundredNanosecondUnits,
            String aStringGUIDTimeFormat);
	boolean pauseSession();
	boolean stopSession();
	boolean closeSession();
	int getSessionDescriptor();
	void addUpdateStateListener(IUpdateStateListener aICallbackListener);
}
