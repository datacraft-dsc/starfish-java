package sg.dex.starfish.integration.developerTC.remoteoperation;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;

import java.util.Map;

public class PrimeRemoteOperation extends RemoteOperation {

    protected PrimeRemoteOperation(RemoteAgent remoteAgent, String meta) {
        super(remoteAgent, meta);
    }

    public static PrimeRemoteOperation create(RemoteAgent a, String meta) {
        return new PrimeRemoteOperation(a,meta);
    }
    @Override
    public String getAssetID() {
        return "primes";
    }
    @Override
    public Map<String, Object> getParamSpec(){
    Map<String,Object> meta=getMetadata();
        Map<String, Object> operation= (Map<String, Object>) meta.get("operation");
        return operation;
    }


}
