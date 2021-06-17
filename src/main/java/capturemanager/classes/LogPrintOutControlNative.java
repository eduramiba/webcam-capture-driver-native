package capturemanager.classes;

abstract class LogPrintOutControlNative 
{      
    
	native protected void addPrintOutDestinationNative(
			long aPtr,
			int aLevelType,
			String aFilePath);

	native protected void removePrintOutDestinationNative(
			long aPtr,
			int aLevelType, 
			String aFilePath);

	native protected void setVerboseNative(
			long aPtr,
			int aLevelType, 
			String aFilePath,
			boolean aState);
}
