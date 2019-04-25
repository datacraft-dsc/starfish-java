package sg.dex.squid;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.squid.SquidAccount;
import sg.dex.starfish.impl.squid.SquidAgent;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidIntegrationTests {
	private static Ocean ocean = null;
	private static SquidAgent squid = null;
	private static SquidAccount publisherAccount = null;
	private static SquidAccount purchaserAccount = null;

	@Test
	public void dCreateAsset() {
		System.out.println("=== dCreateAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void eRegisterAsset() {
		System.out.println("=== eRegisterAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void gSearchListings() {
		System.out.println("=== gSearchListings ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void hPurchaseAsset() {
		System.out.println("=== hPurchaseAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void iDownloadAsset() {
		System.out.println("=== iDownloadAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

}
