package sg.dex.starfish.samples;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sg.dex.starfish.util.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleIntegrationTests {
	public static final String DEFAULT_SURFER_URL = "http://localhost:8080";

	private static boolean surferUp = Utils.checkURL(DEFAULT_SURFER_URL);
	private static String registerAssetID = null;


	@Test
	public void aAuthSample() {
		System.out.println("=== aAuthSample ===");
		if (surferUp) {
			AuthSample.main();
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

	@Test
	public void aTestInvokeSample() {
		System.out.println("=== aTestInvokeSample ===");
		InvokeSample.main();
	}

	@Test
	public void bTestIrisSample() {
		System.out.println("=== bTestIrisSample ===");
		if (surferUp) {
			IrisSample.main();
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

	@Test
	public void cTestRegisterSample() {
		System.out.println("=== cTestRegisterSample ===");
		if (surferUp) {
			registerAssetID = RegisterSample.test();
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}

	@Test
	public void dTestMetadataSample() {
		System.out.println("=== dTestMetadataSample ===");
		if (surferUp) {
			MetadataSample.main(registerAssetID);
		} else {
			System.out.println("WARNING: not tested as surfer is not up.");
		}
	}
}
