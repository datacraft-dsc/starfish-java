package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of an operation which always fails
 * data asset
 *
 * @author Mike
 */

public class EpicFailOperation extends AMemoryOperation implements Operation {
    protected EpicFailOperation(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    /**
     * Creates a new instance of EpicFailOperation
     *
     * @param meta metadata
     * @return new instance of EpicFailOperation
     */
    public static EpicFailOperation create(String meta) {
        MemoryAgent memoryAgent = MemoryAgent.create();
        return new EpicFailOperation(meta, memoryAgent);
    }

    @Override
    public Map<String, Object> compute(Map<String, Object> params) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        res.put("description", "Unable to access asset did:op:4d517500da0acb0d65a716f61330969334630363ce4a6a9d39691026ac7908fa");
        res.put("status", Constant.FAILED);
        res.put("errorcode", "8004");
        result.put("result", res);

        return result;
    }

}
