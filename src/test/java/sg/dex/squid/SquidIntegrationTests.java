package sg.dex.squid;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.Account;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAccount;
import sg.dex.starfish.util.AuthorizationException;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidIntegrationTests {

	private static Ocean ocean = null;
	private static SquidAgent squid = null;
	private static SquidAccount publisherAccount = null;
	private static SquidAccount purchaserAccount = null;

	@Test public void aCreateOcean() {
		System.out.println("=== aCreateOcean ===");
		ocean = Ocean.connect();
	}

	@Test public void bConfigureSquidAgent() {
		System.out.println("=== bConfigureSquidAgent ===");
		try {
			squid = SquidBuilder.create(ocean);
		} catch (Exception e) {
			fail("unable to build squid: " + e);
		}
	}

	@Test public void cGetPublisherAccount() {
		System.out.println("=== cGetPublisherAccount ===");
		String publisherAddress = squid.getConfigString("account.parity.address");
		String publisherPassword = squid.getConfigString("account.parity.password");
		publisherAccount = SquidAccount.create(publisherAddress, publisherPassword, squid);
		try {
			publisherAccount.unlock();
p			try {
				publisherAccount.requestTokens(20);
			} catch (AuthorizationException e) {
				fail("unable to request publisher tokens: " + e);
			}
		} catch (AuthorizationException e) {
			fail("unable to unlock publisher account: " + e);
		}
	}

	@Test public void dCreateAsset() {
		System.out.println("=== dCreateAsset ===");
	}

	@Test public void eRegisterAsset() {
		System.out.println("=== eRegisterAsset ===");
	}

	@Test public void fGetPurchaserAccount() {
		System.out.println("=== fGetPurchaserAccount ===");
		String purchaserAddress = squid.getConfigString("account.parity.address2");
		String purchaserPassword = squid.getConfigString("account.parity.password2");
		purchaserAccount = SquidAccount.create(purchaserAddress, purchaserPassword, squid);
		try {
			purchaserAccount.unlock();
			try {
				purchaserAccount.requestTokens(10);
			} catch (AuthorizationException e) {
				fail("unable to request purchaser tokens: " + e);
			}
		} catch (AuthorizationException e) {
			fail("unable to unlock purchaser account: " + e);
		}
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
