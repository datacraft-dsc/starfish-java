package sg.dex.starfish.samples;

import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.operations.ReverseBytesOperation;

import static junit.framework.TestCase.assertNotNull;

public class InvokeSample {


	public static void main(String... args) {

		Operation op = ReverseBytesOperation.create("test");
		assertNotNull(op.getMetadata());
		//System.out.println(JSON.toPrettyString(op.getMetadata()));
	}

}
