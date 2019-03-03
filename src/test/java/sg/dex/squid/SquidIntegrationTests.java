package sg.dex.squid;

import sg.dex.starfish.impl.remote.RemoteAgent;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidIntegrationTests {

	private static final Logger log = LogManager.getLogger(SquidIntegrationTests.class);
	private static RemoteAgent squid = null;

	@Test public void aConfigureSquidAgent() {
		System.out.println("=== aConfigureSquidAgent ===");
		try {
			squid = SquidConfig.getSquid();
		} catch (Exception e) {
			fail("unable to configure squid");
		}
	}

	@Test public void bConfigureSquidAgent() {
		System.out.println("=== bConfigureSquidAgent ===");
	}

	@Test public void cGetPublisherAccount() {
		System.out.println("=== cGetPublisherAccount ===");
	}

	@Test public void dCreateAsset() {
		System.out.println("=== dCreateAsset ===");
	}

	@Test public void eRegisterAsset() {
		System.out.println("=== eRegisterAsset ===");
	}

	@Test public void fGetPurchaserAccount() {
		System.out.println("=== fGetPurchaserAccount ===");
	}

	@Test public void gSearchListings() {
		System.out.println("=== gSearchListings ===");
	}

	@Test public void hPurchaseAsset() {
		System.out.println("=== hPurchaseAsset ===");
	}

	@Test public void iDownloadAsset() {
		System.out.println("=== iDownloadAsset ===");
	}

}
