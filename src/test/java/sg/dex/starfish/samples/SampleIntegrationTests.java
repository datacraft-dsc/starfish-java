package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleIntegrationTests {
	private static String registerAssetID = null;

	@Test public void aTestInvokeSample() {
		System.out.println("=== aTestInvokeSample ===");
		InvokeSample.main();
	}

	@Test public void bTestIrisSample() {
		System.out.println("=== bTestIrisSample ===");
		IrisSample.main();
	}

	@Test public void cTestRegisterSample() {
		System.out.println("=== cTestRegisterSample ===");
		registerAssetID = RegisterSample.test();
	}

	@Test public void dTestMetadataSample() {
		System.out.println("=== dTestMetadataSample ===");
		MetadataSample.main(registerAssetID);
	}

}
