package capturemanager.classes;

import capturemanager.interfaces.ISampleGrabberCall;
import capturemanager.interfaces.IStreamNode;
import java.nio.ByteBuffer;

final class SampleGrabberCall extends SampleGrabberCallNative implements ISampleGrabberCall {

    private StreamNode mStreamNode = null;

    SampleGrabberCall(long aPtr) {
        mStreamNode = new StreamNode(aPtr);
    }

    @Override
    public int readData(ByteBuffer byteBuffer) {
        if (!byteBuffer.isDirect()) {
            throw new IllegalArgumentException("You must user a direct byte buffer");
        }

        return super.readData(mStreamNode.mPtr, byteBuffer);
    }

    @Override
    public void RGB32ToBGRA(ByteBuffer byteBuffer) {
        super.RGB32ToBGRA(byteBuffer);
    }

    @Override
    public IStreamNode getStreamNode() {
        return mStreamNode;
    }

}
