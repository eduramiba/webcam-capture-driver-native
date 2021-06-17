package capturemanager.interfaces;

public interface ISampleGrabberCallback {

	void addCallbackListener(ICallbackListener aICallbackListener);
	
	IStreamNode getStreamNode();
	
}
