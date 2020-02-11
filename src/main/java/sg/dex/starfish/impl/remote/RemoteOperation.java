package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.util.Params;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.ASYNC;
import static sg.dex.starfish.constant.Constant.SYNC;

/**
 * Class representing a Invokable operation callable via the Invoke API
 * This method of this class include the invoke ,invokeAsync,invokeResult
 * for sync operation.
 *
 * @author Mike
 */
public class RemoteOperation extends ARemoteAsset implements Operation {

    protected RemoteOperation(RemoteAgent remoteAgent, String meta) {

        super(meta, remoteAgent);
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
        return agent.invoke(this, params);
    }

    @Override
    public Job invokeAsync(Map<String, Object> params) {
        List<String> modes = getOperationModes();
        if (!modes.contains(ASYNC)) {
            throw new IllegalArgumentException("This operation does not support async invoke.");
        }
        return agent.invokeAsync(this, params);
    }

    @Override
    public Map<String, Object> invokeResult(Map<String, Object> params) {
        List<String> modes = getOperationModes();
        if (!modes.contains(SYNC)) {
            throw new IllegalArgumentException("This operation does not support sync invoke.");
        }
        Map<String, Object> response = agent.invokeResult(this, params);
        try {
            return Params.formatResponse(this, response, agent.getAccount());
        } catch (IOException e) {
            throw new RemoteException("Error in creating the Result");
        }

    }

    @Override
    public Job invoke(Map<String, Object> params) {
        List<String> modes = getOperationModes();
        if (modes.contains(ASYNC)) {
            throw new IllegalArgumentException("This operation does not support async invoke.");
        }
        return agent.invoke(this, params);
    }

}
