package capturemanager.classes;

abstract class SampleGrabberCallSinkFactoryNative {
	native protected long createOutputNode(
			long aPtr,
			String aStringMajorType, 
			String aStringSubType, 
			int aSampleByteSize, 
			String aStringIID);
}
