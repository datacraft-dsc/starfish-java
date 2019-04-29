package sg.dex.starfish.impl.remote;

import sg.dex.starfish.impl.AAsset;

/**
 * This is an abstract class which have common code required 
 * for RemoteAsset/RemoteBundle/RemoteOperation
 */
public abstract class ARemoteAsset extends AAsset {

    private RemoteAgent remoteAgent;

    protected ARemoteAsset(String meta, RemoteAgent remoteAgent) {
        super(meta);
        this.remoteAgent = remoteAgent;
    }

    /**
     * Gets the remote agent associated with this asset
     * @return A RemoteAgent instance for this asset
     */
    public RemoteAgent getRemoteAgent() {
        return remoteAgent;
    }
}
