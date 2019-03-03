package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAgent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleIntegrationTests {
	private static boolean surferUp = RemoteAgent.isAgentUp("http://localhost:8080");
	private static String registerAssetID = null;

	@Test public void aTestInvokeSample() {
		System.out.println("=== aTestInvokeSample ===");
		InvokeSample.main();
	}

	@Test public void bTestIrisSample() {
		System.out.println("=== bTestIrisSample ===");
		if (surferUp) {
			IrisSample.main();
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

	@Test public void cTestRegisterSample() {
		System.out.println("=== cTestRegisterSample ===");
		if (surferUp) {
			registerAssetID = RegisterSample.test();
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

	@Test public void dTestMetadataSample() {
		System.out.println("=== dTestMetadataSample ===");
		if (surferUp) {
			MetadataSample.main(registerAssetID);
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

}
