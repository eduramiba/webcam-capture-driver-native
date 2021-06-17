package capturemanager.classes;

abstract class FileSinkFactoryNative {

	native protected long[] createOutputNodes(
			long aPtr,
			long[] aArrayPtrCompressedMediaTypes, 
			String aPtrFileName);
}
