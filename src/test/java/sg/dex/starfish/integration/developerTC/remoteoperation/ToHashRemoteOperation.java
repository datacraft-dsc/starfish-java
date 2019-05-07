package sg.dex.starfish.integration.developerTC.remoteoperation;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;

import java.util.Map;

public class ToHashRemoteOperation extends RemoteOperation {
    protected ToHashRemoteOperation(RemoteAgent remoteAgent, String meta) {
        super(remoteAgent, meta);
    }

    public static ToHashRemoteOperation create(RemoteAgent a, String meta) {
        return new ToHashRemoteOperation(a,meta);
    }
    @Override
    public String getAssetID() {
        return "hashing";
    }
    @Override
    public Map<String, Object> getParamSpec(){
        Map<String,Object> meta=getMetadata();
        Map<String, Object> operation= (Map<String, Object>) meta.get("operation");
        return operation;
    }
}
