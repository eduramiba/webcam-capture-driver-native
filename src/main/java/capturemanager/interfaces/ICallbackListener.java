package capturemanager.interfaces;

public interface ICallbackListener {
	void invoke(byte[] aSampleBuffer, int aSampleSize);
}
