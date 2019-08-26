package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a sample implementation of Invoke Service.
 * This class can be refereed how to implement an Operation interface .
 * It just do calculation of prime number based on argument passed
 */
public class FindPrime_JsonI_JsonO extends AMemoryOperation implements Operation {
    protected FindPrime_JsonI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static FindPrime_JsonI_JsonO create(MemoryAgent memoryAgent) throws IOException {
        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/test_metadata.json")));
        return new FindPrime_JsonI_JsonO(asset_metaData, memoryAgent);
    }


    private Map<String,Object> doCompute(Object input) {
        Integer num = (Integer.parseInt( input.toString()));

        StringBuilder res = new StringBuilder();

        for (int i = 2; i <= num; i++) {
            if (isPrime(i))
                res.append(i + "  ");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("output", res);
        return response;
    }

    @Override
	protected Map<String,Object> compute(Map<String, Object> params) {
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
