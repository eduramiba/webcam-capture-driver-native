package capturemanager.interfaces;

import java.awt.Component;

public interface IEVRSinkFactory {
    IStreamNode createOutputNode(Component aGraphicComponent);
}
