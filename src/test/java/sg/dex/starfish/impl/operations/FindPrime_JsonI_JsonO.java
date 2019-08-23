package sg.dex.starfish.impl.operations;

import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;


public class FindPrime_JsonI_JsonO extends AMemoryOperation implements Operation {
    protected FindPrime_JsonI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static FindPrime_JsonI_JsonO create(String meta, MemoryAgent memoryAgent) {
        return new FindPrime_JsonI_JsonO(meta, memoryAgent);
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
