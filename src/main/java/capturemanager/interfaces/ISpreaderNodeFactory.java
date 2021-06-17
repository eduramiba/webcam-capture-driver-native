package capturemanager.interfaces;

import java.util.List;

public interface ISpreaderNodeFactory {	
    IStreamNode createSpreaderNode(
            List<IStreamNode> aDownStreamTopologyNodelist);
}
