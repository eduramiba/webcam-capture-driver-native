package capturemanager.classes;

abstract class SpreaderNodeFactoryNative {
	native protected long createSpreaderNode(
			long aPtr,
			long[] aArrayPtrDownStreamTopologyNodes);
}
