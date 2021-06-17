package capturemanager.classes;

abstract class WebCamControlNative {
    native protected String getCamParametrs(
    		long aPtr);
    native protected void setCamParametr(
    		long aPtr,
    		int aParametrIndex,
    		int aNewValue,
    		int aFlag);
}
