package sg.dex.starfish.impl.operations;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.util.Hex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class CalculateHash_AssetI_JsonO extends AMemoryOperation implements Operation {

    protected CalculateHash_AssetI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static CalculateHash_AssetI_JsonO create(MemoryAgent memoryAgent) throws IOException {
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/hashing_metadata.json")));
        return new CalculateHash_AssetI_JsonO(asset_metaData, memoryAgent);
    }


    private synchronized Map<String, Object> doCompute(Object input) {
        Asset a = (Asset) input;

        String hash = Hex.toString(Hash.sha3_256(a.getContent()));

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        response.put("hashed_value", hash);
        result.put("results",response);
        return result;
    }

    @Override
    protected Map<String, Object> compute(Map<String, Object> params) {
        if (params == null || params.get("input") == null)
            throw new IllegalArgumentException("Missing parameter 'input'");
        return doCompute(params.get("input"));
    }
}
