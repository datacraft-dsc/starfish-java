package sg.dex.starfish.integration.developerTC.remoteoperation;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;

import java.util.Map;

public class HashingRemoteOperation extends RemoteOperation {

    protected HashingRemoteOperation(RemoteAgent remoteAgent, String meta) {
        super(remoteAgent, meta,null);
    }

    public static HashingRemoteOperation create(RemoteAgent a, String meta) {
        return new HashingRemoteOperation(a,meta);
    }
    @Override
    public String getAssetID() {
        return "assethashing";
    }
    @Override
    public Map<String, Object> getParamSpec(){
        Map<String,Object> meta=getMetadata();
        Map<String, Object> operation= (Map<String, Object>) meta.get("operation");
        return operation;
    }
}
