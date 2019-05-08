//package sg.dex.starfish.impl.operations;
//
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import sg.dex.starfish.Asset;
//import sg.dex.starfish.Job;
//import sg.dex.starfish.impl.memory.MemoryAgent;
//import sg.dex.starfish.impl.memory.MemoryAsset;
//import sg.dex.starfish.util.Utils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.TestCase.fail;
//import static org.junit.Assert.assertArrayEquals;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class TestMemoryOperations {
//    private MemoryAgent memoryAgent = MemoryAgent.create();
//
//    /**
//     * This test is to test the Async Operation
//     */
//    @Test
//    public void testReverseBytes() {
//        byte[] data = new byte[]{1, 2, 3};
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseBytesOperation memoryOperation = ReverseBytesOperation.create(meta, memoryAgent);
//
//        Asset a = MemoryAsset.create(data);
//        Map<String, Object> test = new HashMap<>();
//        test.put("input", a);
//
//        Job job = memoryOperation.invokeAsync(test);
//
//        Asset result = job.awaitResult(1000);
//
//        assertArrayEquals(new byte[]{3, 2, 1}, result.getContent());
//    }
//
//    /**
//     * This test is to test the Async Operation but providing mode as Sync
//     */
//    @Test
//    public void testReverseBytesAsyncWithDifferentMetadata() {
//        byte[] data = new byte[]{1, 2, 3};
//
//        String meta = "{\n" +
//                "  \"params\": {\n" +
//                "    \"input\": {\n" +
//                "      \"required\": \"true\",\n" +
//                "      \"position\": 0,\n" +
//                "      \"type\": \"Object\"\n" +
//                "      \n" +
//                "    },\n" +
//                "    \"did\": \"hashing\"\n" +
//                "  },\n" +
//                "  \"mode\":\"Async\",\n" +
//                "  \"result\": {\n" +
//                "        \"hash-value\": {\n" +
//                "           \"type\": \"Object\"\n" +
//                "      \n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//
//
//        ReverseBytesOperation memoryOperation = ReverseBytesOperation.create(meta, memoryAgent);
//
//        Asset a = MemoryAsset.create(data);
//        Map<String, Object> test = new HashMap<>();
//        test.put("input", a);
//
//        Job job = memoryOperation.invokeAsync(test);
//
//        Asset result = job.awaitResult(1000);
//
//        assertArrayEquals(new byte[]{3, 2, 1}, result.getContent());
//
//    }
//
//    @Test
//    public void testNamedParams() {
//        byte[] data = new byte[]{1, 2, 3};
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseBytesOperation op = ReverseBytesOperation.create(meta, memoryAgent);
//        Asset a = MemoryAsset.create(data);
//        Job job = op.invokeAsync(Utils.mapOf("input", a));
//        Asset result = job.awaitResult(1000);
//        assertArrayEquals(new byte[]{3, 2, 1}, result.getContent());
//    }
//
//    @Test
//    public void testBadNamedParams() {
//        byte[] data = new byte[]{1, 2, 3};
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseBytesOperation op = ReverseBytesOperation.create(meta, memoryAgent);
//        Asset a = MemoryAsset.create(data);
//        Job badJob = op.invokeAsync(Utils.mapOf("nonsense", a)); // should not yet fail since this is async
//        try {
//            Asset result2 = badJob.awaitResult(1000);
//            fail("Should not succeed! Got: " + Utils.getClass(result2));
//        } catch (Exception ex) {
//            /* OK */
//        }
//    }
//
//    @Test(expected = Exception.class)
//    public void testInsufficientParams() {
//        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
//        ReverseBytesOperation op = ReverseBytesOperation.create(meta, memoryAgent);
//        try {
//            Job badJob = op.invokeAsync(null); // should not yet fail since this is async
//            Asset result2 = badJob.awaitResult(10);
//            fail("Should not succeed! Got: " + Utils.getClass(result2));
//        } catch (IllegalArgumentException ex) {
//            /* OK */
//        }
//    }
//
////    @Test
////    public void testFailingOperation() {
////        byte[] data = new byte[]{1, 2, 3};
////        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
////        EpicFailOperation op = EpicFailOperation.create(null);
////        Asset a = MemoryAsset.create(data);
////        Job job = op.invokeAsync(Utils.mapOf("test-fail", a));
////        try {
////            Asset result = job.awaitResult(1000);
////            fail("Should not succeed! Got: " + Utils.getClass(result));
////        } catch (Exception ex) {
////            /* OK */
////        }
////    }
//
//
//}
