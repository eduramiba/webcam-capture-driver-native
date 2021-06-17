package capturemanager.classes;

abstract class StreamControlNative {

    native protected long createStreamControlNodeFactory(
    		long aPtr,
    		String aStringIID);

    native protected String getCollectionOfStreamControlNodeFactories(
    		long aPtr);
}
