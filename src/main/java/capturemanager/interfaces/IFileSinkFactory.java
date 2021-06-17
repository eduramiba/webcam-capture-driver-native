package capturemanager.interfaces;

import java.util.List;

public interface IFileSinkFactory {
	List<IStreamNode> createOutputNodes(
			List<IMediaType> aArrayPtrCompressedMediaTypes, 
			String aPtrFileName);
}
