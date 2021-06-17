package capturemanager.classes;

import java.nio.ByteBuffer;

abstract class SampleGrabberCallNative {
	
	native protected int readData(
			long aPtr,
			ByteBuffer byteBuffer);

	native protected void RGB32ToBGRA(
			ByteBuffer byteBuffer);
}
