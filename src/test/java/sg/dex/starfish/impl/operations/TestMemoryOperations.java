package sg.dex.starfish.impl.operations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("javadoc")
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
        assertTrue(memoryOperation.isOperation());

        // should not have a content hash
        assertFalse(memoryOperation.getMetadata().containsKey(Constant.CONTENT_HASH));

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = memoryOperation.invokeAsync(test);

        Map<String, Object> res = job.getResult(10000);
        Asset resultAsset = (Asset) res.get("reverse_result");
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
        Asset resultAsset = (Asset) result.get("reverse_result");
        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }


    // ------JSON input and JSON output----------------

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeAsync() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/prime_asset_metadata.json")));


        Operation memoryOperation = FindSumOfPrime_JsonInput_AssetOutput.create(asset_metaData, memoryAgent);

        Map<String, Object> test = new HashMap<>();
        test.put("input", "10");

        Job job = memoryOperation.invokeAsync(test);
        assertTrue(jobStatus.contains(job.getStatus()));
        Map<String, Object> res = job.getResult(1000);
        int acutal = (Integer) res.get("sumOfPrime");

        int expected = 2 + 3 + 5 + 7;

        assertEquals(Constant.SUCCEEDED, job.getStatus());
        assertEquals(expected, acutal);
    }

    /**
     * This test is to test the Async Operation
     */
    @Test
    public void testPrimeSync() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/prime_asset_metadata.json")));

        FindSumOfPrime_JsonInput_AssetOutput memoryOperation = FindSumOfPrime_JsonInput_AssetOutput.create(asset_metaData, memoryAgent);
        Map<String, Object> test = new HashMap<>();
        test.put("input", "15");

        Map<String, Object> result = memoryOperation.invokeResult(test);

        int acutal = (Integer) result.get("sumOfPrime");

        int expected = 2 + 3 + 5 + 7 + 11 + 13;

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
    public void testHashAsyncFailed() {

        byte[] data = new byte[]{1, 2, 3};
        EpicFailOperation epicFailOperation =
                EpicFailOperation.
                        create("Fail operation");

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        Assertions.assertThrows(StarfishValidationException.class, () -> {
            epicFailOperation.invokeAsync(test);
        });

//        Job job;
//        synchronized (epicFailOperation) {
//            job = epicFailOperation.invokeAsync(test);
//            assertFalse(job.isDone()); // can't complete until out of synchronised block
//        }
//
//        try {
//            job.get();
//            fail("should not succeed!!");
//        } catch (Exception e) {
//            /* OK, Expected */
//        }
//        assertTrue(job.isDone());
//        assertEquals(Constant.FAILED, job.getStatus());
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
            assertEquals(Constant.SCHEDULED, job.getStatus());
            assertNull(job.pollResult());
        }
        Map<String, Object> response = job.getResult();
        assertEquals(Constant.SUCCEEDED, job.getStatus());

        String hash = Hex.toString(Hash.sha3_256(a.getContent()));
        assertEquals(response.get("hashed_value").toString(), hash);

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
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Byte Inc\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"sync\",\"async\"],\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";
        return meta;
    }

    /**
     * This test is to test the Async Operation but providing mode as Sync
     */
    @Test
    public void testReverseBytesAsyncWithDifferentMetadata() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"sync\",\"async\"],\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = memoryOperation.invokeAsync(test);

        Map<String, Object> result = job.getResult();
        Asset resultAsset = (Asset) result.get("reverse_result");

        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());

    }

    /**
     * This test is to test the  Operation is valid or not
     */
    @Test
    public void testInvalidMetadata() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "Invalid With No Operation";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        Assertions.assertThrows(StarfishValidationException.class, () -> {
            memoryOperation.invokeAsync(test);
        });


    }

    /**
     * This test is to test the  Operation metadata having no Mode
     */
    @Test
    public void testMetadataWithNoMode() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Job job = memoryOperation.invokeAsync(test);

        Map<String, Object> result = job.getResult();
        Asset resultAsset = (Asset) result.get("reverse_result");

        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());

    }

    /**
     * This test is to test the  Operation metadata having no Mode
     */
    @Test
    public void testMetadataWithNoMode_Sync() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        Map<String, Object> result = memoryOperation.invokeResult(test);

        Asset resultAsset = (Asset) result.get("reverse_result");

        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());

    }

    /**
     * This test is to test the  Operation metadata have invalid mode
     */
    @Test
    public void testMetadataWithInvalidMode() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"Invalid\",\"async\"],\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);


        try {
            Job job = memoryOperation.invokeAsync(test);
        } catch (StarfishValidationException e) {
            assertTrue(e.getMessage().contains("Invalid mode"));
        }

    }

    /**
     * This test is to test the  Operation mode Sync butt call Async operation
     */
    @Test
    public void testMetadataWitModeSyncForAsyncCall() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"sync\"],\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        try {
            Job job = memoryOperation.invokeAsync(test);
        } catch (StarfishValidationException e) {
            assertTrue(e.getMessage().contains("Mode must be Async for this"));
        }


    }

    /**
     * This test is to test the  Operation mode is Async but call is Sync
     */
    @Test
    public void testMetadataWitModeASyncForSyncCall() {
        byte[] data = new byte[]{1, 2, 3};

        String meta = "{\"dateCreated\":\"2019-05-07T08:17:31.521445Z\",\n" +
                "\t\"contentType\":\"application/octet-stream\",\n" +
                "\t\"tags\":[\"Reverse byte\"],\n" +
                "\t\"license\":\"CC-BY\",\n" +
                "\t\"author\":\"Reverse Different\",\n" +
                "\t\"name\":\"Reverse byte computation operation\",\n" +
                "\t\"description\":\"Reverse the give byte\",\n" +
                "\t\"inLanguage\":\"en\",\n" +
                "\t\"type\":\"operation\",\n" +
                " \"operation\":{\n" +
                "     \"modes\":[\"async\"],\n" +
                "\t\t\"inputs\":{\"input\":{\"type\":\"asset\"}},\n" +
                "\t\t\"outputs\":{\"output\":{\"type\":\"asset\"}}}}";


        ReverseByte_AssetI_AssetO memoryOperation = ReverseByte_AssetI_AssetO.create(meta, memoryAgent);

        Asset a = MemoryAsset.create(data);
        Map<String, Object> test = new HashMap<>();
        test.put("input", a);

        try {
            memoryOperation.invokeResult(test);
        } catch (StarfishValidationException e) {
            assertTrue(e.getMessage().contains("Mode must be Sync for this"));
        }


    }


    @Test
    public void testNamedParams() {
        byte[] data = new byte[]{1, 2, 3};
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);
        Asset a = MemoryAsset.create(data);
        Job job = op.invokeAsync(Utils.mapOf("input", a));
        Map<String, Object> result = job.getResult();
        Asset resultAsset = (Asset) result.get("reverse_result");

        assertArrayEquals(new byte[]{3, 2, 1}, resultAsset.getContent());
    }

    @Test
    public void testBadNamedParams() {
        byte[] data = new byte[]{1, 2, 3};
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);
        Asset a = MemoryAsset.create(data);


        Assertions.assertThrows(Exception.class, () -> {
            Job badJob = op.invokeAsync(Utils.mapOf("nonsense", a));
            badJob.getResult(10);
        });


    }

    @Test
    public void testInsufficientParams() {
        ReverseByte_AssetI_AssetO op = ReverseByte_AssetI_AssetO.create(getMetaDataForAssetI_AssetO(), memoryAgent);
        Map<String, Object> emptyParams = new HashMap<>();


        Assertions.assertThrows(Exception.class, () -> {
            Job badJob = op.invokeAsync(emptyParams); // should not yet fail since this is async
            badJob.getResult(1000);
        });


    }

}
