package capturemanager.interfaces;

public interface IStreamControl {

    String getCollectionOfStreamControlNodeFactories();

    ISpreaderNodeFactory createStreamControlNodeFactory();

    ISpreaderNodeFactory createStreamControlNodeFactory(
        String aStringIID);
}
