package capturemanager.interfaces;

public interface ISampleGrabberCallSinkFactory {

	ISampleGrabberCall createOutputNode(
        String aStringMajorType,
        String aStringSubType,
        int aSampleByteSize);
}
