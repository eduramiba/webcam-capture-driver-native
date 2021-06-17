package capturemanager.classes;

abstract class VersionControlNative {
	native protected String getXMLStringVersion(long aPtr);
}
