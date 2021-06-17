package capturemanager.interfaces;

import java.nio.ByteBuffer;

public interface ISampleGrabberCall {
	int readData(ByteBuffer byteBuffer);
	void RGB32ToBGRA(ByteBuffer byteBuffer);
	IStreamNode getStreamNode();
}
