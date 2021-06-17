package capturemanager.interfaces;

public interface ILogPrintOutControl {	
    void addPrintOutDestination(int aLevelType, String aFilePath);
    void removePrintOutDestination(int aLevelType, String aFilePath);
    void setVerbose(int aLevelType, String aFilePath, Boolean aState);
    void release();
}
