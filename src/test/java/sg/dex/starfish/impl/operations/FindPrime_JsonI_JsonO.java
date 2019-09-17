package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.resource.ResourceAsset;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a memory implementation of Invoke Service.
 * It calculate all prime number present before any given number.
 * It reads metadata from a file which has basic detail of the input and output type.
 */
public class FindPrime_JsonI_JsonO extends AMemoryOperation implements Operation {
    protected FindPrime_JsonI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static FindPrime_JsonI_JsonO create(MemoryAgent memoryAgent) {
        ResourceAsset resourceAsset = ResourceAsset.create("src/test/resources/assets/prime_asset_metadata.json");
        return new FindPrime_JsonI_JsonO(resourceAsset.getMetadataString(), memoryAgent);
    }


    private Map<String, Object> doCompute(final Object input) {
        Integer num = (Integer.parseInt(input.toString()));

        byte[] result = new byte[4];

        int count = 0;
        for (int i = 2; i < num; i++) {
            if (isPrime(i)) {
                result[count] = (byte) i;
                count++;
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", Constant.SUCCEEDED);
        Asset res = MemoryAsset.create(result);
        memoryAgent.registerAsset(res);
        resultMap.put("did", res.getAssetID());
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("result", resultMap);
        return returnMap;
    }

    @Override
    protected Map<String, Object> compute(Map<String, Object> params) {
        if (params == null || params.get("input") == null)
            throw new IllegalArgumentException("Missing parameter 'input'");
        return doCompute(params.get("input"));
    }

    private boolean isPrime(int n) {
        if (n <= 1)
            return false;

        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

}
