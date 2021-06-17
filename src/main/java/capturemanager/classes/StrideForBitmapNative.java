package capturemanager.classes;

abstract class StrideForBitmapNative {
	native protected int getStrideForBitmap(
			long aPtr,
			String aStringMFVideoFormat,
			int aWidthInPixels);
}
