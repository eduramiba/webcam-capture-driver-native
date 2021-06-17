package capturemanager.interfaces;

import java.util.*;

public interface ISessionControl {
	
    ISession createSession(
    		List<IStreamNode> aArrayPtrSourceNodesOfTopology);

}
