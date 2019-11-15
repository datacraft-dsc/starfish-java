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
    private List<String> validateOperationMode(Operation operation){

        Map<String,Object> operationData = JSON.toMap(operation.getMetadata().get(OPERATION).toString());
        //1. check if mode is present

        if(operationData.get(MODES)== null){
            return null;
        }
        List<String> modeLst= (List<String>)operationData.get(MODES);
        for(Object mode: modeLst){
            if(mode.toString().equals(SYNC)||
                    mode.toString().equals(ASYNC)){

            }
            else{
                throw new StarfishValidationException("Invalid mode of the given operation:"+operation.toString());
            }
        }

        return modeLst;



    }

    @Override
    public Job invoke(Object... params) {
        return remoteAgent.invoke(this, params);
    }

    @Override
    public Job invokeAsync(Map<String, Object> params) {
        if (validateOperationMode(this) != null &&
                !validateOperationMode(this).contains(ASYNC)) {
            throw new StarfishValidationException("Mode must be Async for this operation");
        }
        return remoteAgent.invokeAsync(this, params);
    }

    @Override
    public Map<String, Object> invokeResult(Map<String, Object> params) {
        if (validateOperationMode(this) != null &&
                !validateOperationMode(this).contains(SYNC)) {
            throw new StarfishValidationException("Mode must be Sync for this operation");
        }
        Map<String, Object> response = remoteAgent.invokeResult(this, params);
        return Params.formatResponse(this, response, remoteAgent);

    }

    @Override
    public Job invoke(Map<String, Object> params) {
        if (validateOperationMode(this) != null &&
                !validateOperationMode(this).contains(ASYNC)) {
            throw new StarfishValidationException("Mode must be Async for this operation");
        }
        return remoteAgent.invoke(this, params);
    }

}
