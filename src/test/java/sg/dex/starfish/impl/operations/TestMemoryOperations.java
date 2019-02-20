package sg.dex.starfish.impl.operations;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.MemoryAsset;

public class TestMemoryOperations {

	@Test public void testReverseBytes() {
		byte[] data=new byte[] {1,2,3};
		Operation op=ReverseBytesOperation.create();
		Asset a=MemoryAsset.create(data);
		Job job=op.invoke(a);
		Asset result=job.awaitResult(1000);
		assertArrayEquals(new byte[] {3,2,1}, result.getBytes());
		
	}
}
