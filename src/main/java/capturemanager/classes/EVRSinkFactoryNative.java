package capturemanager.classes;

import java.awt.Component;

abstract class EVRSinkFactoryNative {

	native protected long createOutputNode(
			long aPtr,
			Component aGraphicComponent);
}
