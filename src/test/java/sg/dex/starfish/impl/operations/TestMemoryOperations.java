package sg.dex.starfish.impl.operations;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryOperations {
    private MemoryAgent memoryAgent = MemoryAgent.create();
    private List<String> jobStatus = Arrays.asList("scheduled", "running", "succeeded", "failed", "unknown");

    /**
     * This test is to test the Asset input Asset output Async
     */
    @Test
    public void testReverseBytesAsync() {
        byte[] data = new byte[]{1, 2, 3};
        Operation memoryOperation = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = memoryOperation.invokeAsync(test);

        Map<String, Object> res = job.getResult(1000);
        String id = res.get("did").toString();
        Asset resultAsset = memoryAgent.getAsset(id);
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }

    /**
     * This test is to test the Asset input Asset output Async
     */
    @Test
    public void testReverseBytesSync() {
        byte[] data = new byte[]{1, 2, 3};
        Operation memoryOperation =
                ReverseByte_AssetI_AssetO.
                        create(getMetaDataForAssetI_AssetO(), memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Map<String, Object> result = memoryOperation.invokeResult(test);
        String id = result.get("did").toString();
        Asset resultAsset = memoryAgent.getAsset(id);
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }


    // ------JSON input and JSON output----------------

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeAsync()  {

        Operation memoryOperation = FindSumOfPrime_JsonInput_AssetOutput.create(memoryAgent);

        Map<String, Object> test = new HashMap<>();
        test.put("input", "10");

        Job job = memoryOperation.invokeAsync(test);
        assertTrue(jobStatus.contains(job.getStatus()));
        Map<String, Object> res = job.getResult(1000);
        int acutal = (Integer)res.get("sumOfPrime");

        int expected = 2+ 3+ 5+ 7;

        assertEquals(Constant.SUCCEEDED, job.getStatus());
        assertEquals(expected, acutal);
    }

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeSync()  {

        FindSumOfPrime_JsonInput_AssetOutput memoryOperation = FindSumOfPrime_JsonInput_AssetOutput.create(memoryAgent);
        Map<String, Object> test = new HashMap<>();
        test.put("input", "15");

        Map<String, Object> result = memoryOperation.invokeResult(test);

        int acutal = (Integer)result.get("sumOfPrime");

        int expected = 2+ 3+ 5+ 7+11+13;

        assertEquals(expected, acutal);

    }

    @Test
    public void testHashAsyncSuccess() throws IOException {

        byte[] data = new byte[]{1, 2, 3};
        CalculateHash_AssetI_JsonO hashOperation =
                CalculateHash_AssetI_JsonO.
                        create(memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = hashOperation.invokeAsync(test);
        Map<String, Object> response = job.getResult(1000);
        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(response.get("hashed_value").toString(), hash);
        assertEquals(Constant.SUCCEEDED, job.getStatus());
    }

    @Test
    public void testHashAsyncFailed() throws IOException {

        byte[] data = new byte[]{1, 2, 3};
        EpicFailOperation epicFailOperation =
                EpicFailOperation.
                        create("Fail operation");

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = epicFailOperation.invokeAsync(test);
        try {
        	job.get();
        	fail("should not succeed!!");
        } catch (Exception e) {
        	/* OK, Expected */
        }
        assertEquals(Constant.FAILED, job.getStatus());
    }

    @Test
    public void testHashAsyncRunning() throws IOException {

        byte[] data = new byte[]{1, 2, 3};
        CalculateHash_AssetI_JsonO hashOperation =
                CalculateHash_AssetI_JsonO.
                        create(memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job;
        synchronized (hashOperation) {
        	// run synchronised to prevent completion until end of this code block
        	 job = hashOperation.invokeAsync(test);
        	 assertEquals(Constant.RUNNING,job.getStatus());
        	 assertNull(job.pollResult());
        }
        Map<String, Object> response = job.getResult(1000);
        System.out.println(JSON.toPrettyString(response));
        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(response.get("hashed_value").toString(), hash);
        assertEquals(Constant.SUCCEEDED, job.getStatus());
    }


    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testHashSync() throws IOException {
        byte[] data = new byte[]{1, 2, 3};
        CalculateHash_AssetI_JsonO hashOperation =
                CalculateHash_AssetI_JsonO.
                        create(memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Map<String, Object> response = hashOperation.invokeResult(test);
        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(response.get("hashed_value").toString(), hash);
    }

    private String getMetaDataForAssetI_AssetO() {
        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"size\":\"3\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"contentHash\":\"4e03657aea45a94fc7d47ba826c8d667c0d1e6e33a64a036ec44f58fa12d6c45\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Byte Inc\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"sync\",\"async\"],\n" +
                "\t\t\"params\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"results\":{\"output\":{\"type\":\"asset\"}}}}";
        return meta;
    }

    /**
     * This test is to test the Async Operation but providing mode as Sync
     */
    @Test
    public void testReverseBytesAsyncWithDifferentMetadata() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\n" +
                "  \"params\": {\n" +
                "    \"input\": {\n" +
                "      \"required\": \"true\",\n" +
                "      \"position\": 0,\n" +
                "      \"type\": \"Object\"\n" +
                "      \n" +
                "    },\n" +
                "    \"did\": \"hashing\"\n" +
                "  },\n" +
                "  \"mode\":\"Async\",\n" +
                "  \"result\": {\n" +
                "        \"hash-value\": {\n" +
                "           \"type\": \"Object\"\n" +
                "      \n" +
                "    }\n" +
                "  }\n" +
                "}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = memoryOperation.invokeAsync(test);

        Map<String, Object> res = job.getResult(1000);
        String id = res.get("did").toString();
        Asset resultAsset = memoryAgent.getAsset(id);
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());

    }

    @Test
    public void testNamedParams() {
        byte[] data = new byte[]{1, 2, 3};
        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
        Asset a = MemoryAsset.create(data);
        Job job = op.invokeAsync(Utils.mapOf("input", a));
        Map<String, Object> res = job.getResult(1000);
        String id = res.get("did").toString();
        Asset resultAsset = memoryAgent.getAsset(id);
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }

//	@Test(expected = JobFailedException.class)
//    public void testBadNamedParams() {
//        byte[] data = new byte[]{1, 2, 3};
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
//        Asset a = MemoryAsset.create(data);
//        Job badJob = op.invokeAsync(Utils.mapOf("nonsense", a));
//        System.out.println(badJob.getStatus());
//            Object result2 = badJob.getResult(1000);
//            System.out.println(badJob.getStatus());
//            fail("Should not succeed! Got: " + Utils.getClass(result2));
//    }

//    @Test(expected = Exception.class)
//    public void testInsufficientParams() {
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
//        	Map<String,Object> emptyParams=new HashMap<>();
//            Job badJob = op.invokeAsync(emptyParams); // should not yet fail since this is async
//            Object result2 = badJob.getResult(10);
//            fail("Should not succeed! Got: " + Utils.getClass(result2));
//            /* OK */
//    }

}
