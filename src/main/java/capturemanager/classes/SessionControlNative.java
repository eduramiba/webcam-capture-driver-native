package capturemanager.classes;

abstract class SessionControlNative {
    native protected long createSession(
    		long aPtr,
    		long[] aArrayPtrSourceNodesOfTopology,
    		String aStringIID);
}
