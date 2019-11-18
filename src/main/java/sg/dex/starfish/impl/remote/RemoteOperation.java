package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Params;

import java.util.List;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;
import static sg.dex.starfish.constant.Constant.ASYNC;

/**
 * Class representing a Invokable operation callable via the Invoke API
 * This method of this class include the invoke ,invokeAsync,invokeResult
 * for sync operation.
 *
 * @author Mike
 */
public class RemoteOperation extends ARemoteAsset implements Operation {

    private RemoteAgent remoteAgent;
    protected RemoteOperation(RemoteAgent remoteAgent, String meta) {

        super(meta, remoteAgent);
         this.remoteAgent=remoteAgent;
    }

    /**
     * This method is to create the remote operation instance by passing the remote agent and the metadata
     *
     * @param a    agent on which this operation instance needs to be created
     * @param meta meta data for creating remote operation instance
     * @return new Remote operation instance
     */
    public static RemoteOperation create(RemoteAgent a, String meta) {

        return new RemoteOperation(a, meta);
    }
    
 

    @Override
    public Job invoke(Object... params) {
        return remoteAgent.invoke(this, params);
    }

    @Override
    public Job invokeAsync(Map<String, Object> params) {
    	List<String> modes=getOperationModes();
        if (!modes.contains(ASYNC)) {
            throw new IllegalArgumentException("This operation does not support async invoke.");
        }
        return remoteAgent.invokeAsync(this, params);
    }

    @Override
    public Map<String, Object> invokeResult(Map<String, Object> params) {
       	List<String> modes=getOperationModes();
        if (!modes.contains(SYNC)) {
            throw new IllegalArgumentException("This operation does not support sync invoke.");
        }
        Map<String, Object> response = remoteAgent.invokeResult(this, params);
        return Params.formatResponse(this, response, remoteAgent);

    }

    @Override
    public Job invoke(Map<String, Object> params) {
       	List<String> modes=getOperationModes();
        if (modes.contains(ASYNC)) {
            throw new IllegalArgumentException("This operation does not support async invoke.");
        }
        return remoteAgent.invoke(this, params);
    }

}
