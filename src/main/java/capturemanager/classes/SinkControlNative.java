package capturemanager.classes;

abstract class SinkControlNative {

    native protected long createSinkFactory(
    		long aPtr,
    		String aStringContainerTypeGUID,
    		String aStringIID);

    native protected String getCollectionOfSinks(
    		long aPtr);
}
