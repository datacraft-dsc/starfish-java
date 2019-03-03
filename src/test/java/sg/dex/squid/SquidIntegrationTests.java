package sg.dex.squid;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.Account;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.SquidAccount;

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
	private static Ocean ocean = null;
	private static RemoteAgent squid = null;
	private static Account publisherAccount = null;
	private static Account purchaserAccount = null;

	@Test public void aCreateOcean() {
		System.out.println("=== aCreateOcean ===");
		ocean = Ocean.connect();
	}

	@Test public void bConfigureSquidAgent() {
		System.out.println("=== bConfigureSquidAgent ===");
		try {
			squid = SquidConfig.getSquid(ocean);
		} catch (Exception e) {
			fail("unable to configure squid");
		}
	}

	@Test public void cGetPublisherAccount() {
		System.out.println("=== cGetPublisherAccount ===");
		publisherAccount = SquidAccount.create(ocean, "0x00bd138abd70e2f00903268f3db08f2d25677c9e", "node0");
	}

	@Test public void dCreateAsset() {
		System.out.println("=== dCreateAsset ===");
	}

	@Test public void eRegisterAsset() {
		System.out.println("=== eRegisterAsset ===");
	}

	@Test public void fGetPurchaserAccount() {
		System.out.println("=== fGetPurchaserAccount ===");
		purchaserAccount = SquidAccount.create(ocean, "0x068Ed00cF0441e4829D9784fCBe7b9e26D4BD8d0","secret");
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
