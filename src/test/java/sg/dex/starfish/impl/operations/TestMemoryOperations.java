package sg.dex.starfish.impl.operations;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryOperations {
    private MemoryAgent memoryAgent = MemoryAgent.create();

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

        Asset response = job.get("output");

        assertArrayEquals(new byte[]{3, 2, 1}, response.getContent());
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

        Map<String,Object> result = memoryOperation.invokeResult(test);
        Asset response= (Asset)result.get("output");
        assertArrayEquals(new byte[]{3, 2, 1}, response.getContent());
    }


    // ------JSON input and JSON output----------------
    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeAsync() throws IOException {

        Operation memoryOperation = FindPrime_JsonI_JsonO.create( memoryAgent);

        Map<String, Object> test = new HashMap<>();
        test.put("input", "10");

        Job job = memoryOperation.invokeAsync(test);
        assertEquals(Job.Status.running,job.getStatus());

        Map<String,Object> res = job.getResult(1000);
        assertEquals(Job.Status.succeeded,job.getStatus());
        String s=res.get("output").toString();
        assertTrue(s.contains("2"));
        assertTrue(s.contains("3"));
        assertTrue(s.contains("5"));
        assertTrue(s.contains("7"));
    }

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeSync() throws IOException {

        FindPrime_JsonI_JsonO memoryOperation = FindPrime_JsonI_JsonO.create( memoryAgent);
        Map<String, Object> test = new HashMap<>();
        test.put("input", "12");

        Map<String,Object> result = memoryOperation.invokeResult(test);

        assertTrue(result.get("output").toString().contains("2"));
        assertTrue(result.get("output").toString().contains("3"));
        assertTrue(result.get("output").toString().contains("5"));
        assertTrue(result.get("output").toString().contains("7"));
        assertTrue(result.get("output").toString().contains("11"));

    }

    @Test
    public void testHashAsync() throws IOException {

        byte[] data = new byte[]{1, 2, 3};
        CalculateHash_AssetI_JsonO hashOperation =
                CalculateHash_AssetI_JsonO.
                        create( memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = hashOperation.invokeAsync(test);
        assertEquals(Job.Status.running,job.getStatus());
        Map<String ,Object> response = job.getResult(1000);
        String result=(String)response.get("output");
        assertEquals(Job.Status.succeeded,job.getStatus());
        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(result,hash);
    }

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testHashSync() throws IOException {
        byte[] data = new byte[]{1, 2, 3};
        CalculateHash_AssetI_JsonO hashOperation =
                CalculateHash_AssetI_JsonO.
                        create( memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Map<String,Object> res = hashOperation.invokeResult(test);
        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(hash,res.get("output").toString());
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

        Asset result = (Asset) job.getResult(1000).get("output");

        assertArrayEquals(new byte[]{3, 2, 1}, result.getContent());

    }

    @Test
    public void testNamedParams() {
        byte[] data = new byte[]{1, 2, 3};
        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
        Asset a = MemoryAsset.create(data);
        Job job = op.invokeAsync(Utils.mapOf("input", a));
        Asset result = (Asset) job.getResult(1000).get("output");
        assertArrayEquals(new byte[]{3, 2, 1}, result.getContent());
    }

	@Test(expected = JobFailedException.class)
    public void testBadNamedParams() {
        byte[] data = new byte[]{1, 2, 3};
        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
        Asset a = MemoryAsset.create(data);
        Job badJob = op.invokeAsync(Utils.mapOf("nonsense", a)); // should not yet fail since this is async
            Object result2 = badJob.getResult(1000);
            fail("Should not succeed! Got: " + Utils.getClass(result2));
    }

    @Test(expected = Exception.class)
    public void testInsufficientParams() {
        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);
        	Map<String,Object> emptyParams=new HashMap<>();
            Job badJob = op.invokeAsync(emptyParams); // should not yet fail since this is async
            Object result2 = badJob.getResult(10);
            fail("Should not succeed! Got: " + Utils.getClass(result2));
            /* OK */
    }

}
