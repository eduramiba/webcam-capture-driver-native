package capturemanager.interfaces;

public interface ISampleGrabberCallbackSinkFactory {
	ISampleGrabberCallback createOutputNode(
        String aStringMajorType,
        String aStringSubType);
}
