package sg.dex.starfish.impl.remote;

import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;

/**
 * This is an abstract class which have common code required
 * for RemoteAsset/RemoteBundle/RemoteOperation.
 * This class used to initialize the agent passed as an argument.
 */
public abstract class ARemoteAsset extends AAsset {

    protected AAgent aAgent;

    protected ARemoteAsset(String meta, AAgent remoteAgent) {
        super(meta);
        this.aAgent = remoteAgent;
    }

    @Override
    public DID getDID() {
        // DID of a remote asset is the DID of the appropriate agent with the asset ID as a resource path
        DID agentDID = aAgent.getDID();
        return agentDID.withPath(getAssetID());
    }
}
