package sg.dex.starfish.impl.operations;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryOperations {

	@Test public void testReverseBytes() {
		byte[] data=new byte[] {1,2,3};
		String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		AMemoryOperation memoryOperation=ReverseBytesOperation.create(meta);

		Asset a=MemoryAsset.create(data);
		Map<String,Asset> test = new HashMap<>();
		test.put("input",a);

		Job job=memoryOperation.invoke(test);
		
		Asset result=job.awaitResult(1000);
		
		assertArrayEquals(new byte[] {3,2,1}, result.getContent());
	}
	
	@Test public void testNamedParams() {
		byte[] data=new byte[] {1,2,3};
		String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		Operation op=ReverseBytesOperation.create(meta);
		Asset a=MemoryAsset.create(data);
		Job job=op.invoke(Utils.mapOf("input",a));
		Asset result=job.awaitResult(1000);
		assertArrayEquals(new byte[] {3,2,1}, result.getContent());
	}

	@Test public void testBadNamedParams() {
		byte[] data=new byte[] {1,2,3};
		String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		Operation op=ReverseBytesOperation.create(meta);
		Asset a=MemoryAsset.create(data);
		Job badJob=op.invoke(Utils.mapOf("nonsense",a)); // should not yet fail since this is async
		try {
			Asset result2=badJob.awaitResult(1000);
			fail("Should not succeed! Got: "+Utils.getClass(result2));
		} catch (Exception ex) {
			/* OK */
		}
	}

	@Test public void testInsufficientParams() {
		String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		Operation op=ReverseBytesOperation.create(meta);
		try {
			Job badJob=op.invoke(); // should not yet fail since this is async
			Asset result2=badJob.awaitResult(10);
			fail("Should not succeed! Got: "+Utils.getClass(result2));
		} catch (IllegalArgumentException ex) {
			/* OK */
		}
	}

	@Test public void testFailingOperation() {
		byte[] data=new byte[] {1,2,3};
		Operation op=EpicFailOperation.create();
		Asset a=MemoryAsset.create(data);
		Job job=op.invoke(a);
		try {
			Asset result=job.awaitResult(1000);
			fail("Should not succeed! Got: "+Utils.getClass(result));
		} catch (Exception ex) {
			/* OK */
		}
	}

	@Test public void testReverseSyncString() {
		String data="i am going to be reversed";
		Map<String,Object>  param= new HashMap<>();
		param.put("to-reverse",data);

		// this meta data will expect to-hash
		String meta="{\n" +
				"  \"params\": {\n" +
				"    \"to-reverse\": {\n" +
				"      \"required\": \"true\",\n" +
				"      \"position\": 0,\n" +
				"      \"type\": \"asset\"\n" +
				"      \n" +
				"    },\n" +
				"    \"did\": \"hashing\"\n" +
				"  },\n" +
				"  \"mode\":\"Notsync\",\n" +
				"  \"result\": {\n" +
				"        \"hash-value\": {\n" +
				"           \"type\": \"asset\"\n" +
				"      \n" +
				"    }\n" +
				"  }\n" +
				"}";
		AMemoryOperation memoryOperation=ReverseBytesOperation.create(meta);
		Map<String,Object> result =memoryOperation.syncCallToReverse(param);
		assertNotNull(result);
		assertNotNull(result.get("hash-value"));
		assertNotNull(result.get("hash-value"));





	}
}
