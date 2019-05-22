package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.Params;

import java.util.Map;

/**
 * Class representing a Invokable operation callable via the Invoke API
 *
 * @author Mike
 */
public class RemoteOperation extends ARemoteAsset implements Operation {

    protected RemoteOperation(RemoteAgent remoteAgent, String meta) {

        super(meta, remoteAgent);
    }

    /**
     * API to create the remote operation instance by passing the remote agent and the metadata
     *
     * @param a    agent on which this operation instance needs to be created
     * @param meta meta data for creating remote operation instance
     * @return new Remote operation instance
     */
    public static RemoteOperation create(RemoteAgent a, String meta) {

        return new RemoteOperation(a, meta);
    }


    @Override
    public Job invoke(Asset... params) {
        return remoteAgent.invoke(this, params);
    }

    @Override
    public Job invokeAsync(Map<String, Object> params) {
        return remoteAgent.invokeAsync(this, params);
    }

    @Override
    public Map<String, Object> invokeResult(Map<String, Object> params) {
        Map<String, Object> response = remoteAgent.invokeResult(this, params);
        return Params.formatResponse(this, response, remoteAgent);

    }

    @Override
    public Job invoke(Map<String, Object> params) {
        return remoteAgent.invoke(this, params);
    }

}
