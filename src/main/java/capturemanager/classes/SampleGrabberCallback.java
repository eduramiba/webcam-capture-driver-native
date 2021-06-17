package capturemanager.classes;

import capturemanager.interfaces.*;
import java.util.*;

final class SampleGrabberCallback extends SampleGrabberCallbackNative implements ISampleGrabberCallback {


	private StreamNode mStreamNode = null;

	SampleGrabberCallback()  {
	}
	   
	void setPtr(long aPtr)  {
		mStreamNode = new StreamNode(aPtr);
	}
	
	void invoke(byte[] aSampleBuffer, int aSampleSize)
	{
		synchronized(listeners)
		{
			for(int lIndex=0; lIndex < listeners.size(); ++lIndex)
			{
				listeners.get(lIndex).invoke(aSampleBuffer, aSampleSize);
			}						
		}
	}
		
	private List<ICallbackListener> listeners = new ArrayList<ICallbackListener>();
	
	@Override
	public void addCallbackListener(ICallbackListener aICallbackListener) {

		synchronized(listeners)
		{
			listeners.add(aICallbackListener);
		}
	}

	@Override
	public IStreamNode getStreamNode() {
		return mStreamNode;
	}
}
