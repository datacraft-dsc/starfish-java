package sg.dex.starfish.samples;

import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.operations.ReverseBytesOperation;
import sg.dex.starfish.util.JSON;

public class InvokeSample {


	public static void main(String... args) {

		Operation op = ReverseBytesOperation.create();
		System.out.println(JSON.toPrettyString(op.getMetadata()));
	}

}
