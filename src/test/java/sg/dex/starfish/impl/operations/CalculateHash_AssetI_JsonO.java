package sg.dex.starfish.impl.operations;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.util.Hex;

import java.util.HashMap;
import java.util.Map;


public class CalculateHash_AssetI_JsonO extends AMemoryOperation implements Operation {

    protected CalculateHash_AssetI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static CalculateHash_AssetI_JsonO create(String meta, MemoryAgent memoryAgent) {
        return new CalculateHash_AssetI_JsonO(meta, memoryAgent);
    }


    private Map<String,Object> doCompute(Object input) {
        Asset a= (Asset)input;
        String hash =Hex.toString(Hash.sha3_256(a.getContent()));

        Map<String,Object> result = new HashMap<>();
        result.put("output", hash);
        return result;
    }

    @Override
	protected Map<String,Object> compute(Map<String, Object> params) {
        if (params == null || params.get("input") == null)
            throw new IllegalArgumentException("Missing parameter 'input'");
        return doCompute(params.get("input"));
    }
}
