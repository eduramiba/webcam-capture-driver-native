package capturemanager.classes;

abstract class SampleGrabberCallbackSinkFactoryNative {

	native protected long createOutputNode(
			long aPtr,
			String aStringMajorType,
			String aStringSubType, 
			Object aPtrISampleGrabberCallback);
}
