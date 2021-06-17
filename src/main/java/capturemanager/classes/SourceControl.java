package capturemanager.classes;

import capturemanager.interfaces.IMediaType;
import capturemanager.interfaces.ISourceControl;
import capturemanager.interfaces.IStreamNode;

final class SourceControl extends SourceControlNative implements ISourceControl {

    protected static final String IID = "{1276CC17-BCA8-4200-87BB-7180EF562447}";

    SourceControl(long aPtr) {
        mPtr = aPtr;
    }

    protected long mPtr = 0;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (mPtr != 0)
            CaptureManagerNativeProxy.getInstance().Release(mPtr);

        mPtr = 0;
    }


    @Override
    public IStreamNode createSourceNode(
            String aSymbolicLink,
            int aIndexStream,
            int aIndexMediaType,
            IStreamNode aPtrDownStreamTopologyNode) {

        IStreamNode lresult = null;

        do {

            if (mPtr == 0)
                break;

            if (aPtrDownStreamTopologyNode == null)
                break;

            StreamNode lPtrDownStreamTopologyNode = (StreamNode) aPtrDownStreamTopologyNode;

            long lPtr = createSourceNodeWithDownStreamConnection(
                    mPtr,
                    aSymbolicLink,
                    aIndexStream,
                    aIndexMediaType,
                    lPtrDownStreamTopologyNode.mPtr);

            if (lPtr == 0)
                break;

            lresult = new StreamNode(lPtr);
        }
        while (false);

        return lresult;
    }

    @Override
    public IStreamNode createSourceNode(
            String aSymbolicLink,
            int aIndexStream,
            int aIndexMediaType) {

        IStreamNode lresult = null;

        do {

            if (mPtr == 0)
                break;

            if (aSymbolicLink == null)
                break;

            long lPtr = createSourceNode(
                    mPtr,
                    aSymbolicLink,
                    aIndexStream,
                    aIndexMediaType);

            if (lPtr == 0)
                break;

            lresult = new StreamNode(lPtr);
        }
        while (false);

        return lresult;
    }

    @Override
    public String getCollectionOfSources() {

        String lresult = null;

        do {

            if (mPtr == 0)
                break;

            lresult = getCollectionOfSources(
                    mPtr);
        }
        while (false);

        return lresult;
    }


    @Override
    public IMediaType getSourceOutputMediaType(
            String aSymbolicLink,
            int aIndexStream,
            int aIndexMediaType) {

        IMediaType lresult = null;

        do {

            if (mPtr == 0)
                break;

            long lPtr = getSourceOutputMediaType(
                    mPtr,
                    aSymbolicLink,
                    aIndexStream,
                    aIndexMediaType);

            if (lPtr == 0)
                break;

            lresult = new MediaType(lPtr);
        }
        while (false);

        return lresult;
    }

}
