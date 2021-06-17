package capturemanager.interfaces;

public interface IWebCamControl {

    String getCamParametrs();
    
    void setCamParametr(
        int aParametrIndex,
        int aNewValue,
        int aFlag);
}
