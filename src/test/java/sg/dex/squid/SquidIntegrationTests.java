package sg.dex.squid;

import java.util.List;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.squid.SquidAgent;
import sg.dex.starfish.impl.squid.SquidAccount;
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.Utils;

import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.exceptions.EthereumException;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

// SQUID INTEROP START
import com.oceanprotocol.squid.api.OceanAPI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.models.DDO;
import com.oceanprotocol.squid.models.DID;
import com.oceanprotocol.squid.models.asset.AssetMetadata;
import com.oceanprotocol.squid.models.asset.OrderResult;
import com.oceanprotocol.squid.models.service.Service;
import com.oceanprotocol.squid.models.service.ServiceEndpoints;
import io.reactivex.Flowable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
// SQUID INTEROP END

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SquidIntegrationTests {
	private static final String DEFAULT_KEEPER_URL = "http://localhost:8545";
	private static Ocean ocean = null;
	private static SquidAgent squid = null;
	private static SquidAccount publisherAccount = null;
	private static SquidAccount purchaserAccount = null;

	// SQUID INTEROP START
	private static String METADATA_JSON_SAMPLE = "src/test/resources/examples/metadata.json";
	private static String METADATA_JSON_CONTENT;
	private static AssetMetadata metadataBase;
	private static ServiceEndpoints serviceEndpoints;
	private static OceanAPI oceanAPI;
	private static OceanAPI oceanAPIConsumer;
	private static DDO ddo;
	private static DID did;
	// SQUID INTEROP END

	@Test
	public void aCreateOcean() {
		System.out.println("=== aCreateOcean ===");
		if (Utils.checkURL(DEFAULT_KEEPER_URL)) {
			ocean = Ocean.connect();
		} else {
			System.out.println("WARNING: unable to reach keeper at " + DEFAULT_KEEPER_URL +
					   " (is barge running?)");
		}
	}

	@Test
	public void bConfigureSquidAgent() {
		System.out.println("=== bConfigureSquidAgent ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			try {
				squid = SquidBuilder.create(ocean);
				System.out.println("Accounts:");
				for (com.oceanprotocol.squid.models.Account account : squid.list()) {
					System.out.println(account);
				}
				// SQUID INTEROP
				oceanAPI = squid.getOceanAPI();
				assertNotNull(oceanAPI.getAssetsAPI());
				assertNotNull(oceanAPI.getMainAccount());
				METADATA_JSON_CONTENT =  new String(Files.readAllBytes(Paths.get(METADATA_JSON_SAMPLE)));
				metadataBase = DDO.fromJSON(new TypeReference<AssetMetadata>() {}, METADATA_JSON_CONTENT);
				Properties properties = new Properties();
				properties.put(OceanConfig.KEEPER_URL, squid.getConfigString("keeper.url"));
				properties.put(OceanConfig.KEEPER_GAS_LIMIT, squid.getConfigString("keeper.gasLimit"));
				properties.put(OceanConfig.KEEPER_GAS_PRICE, squid.getConfigString("keeper.gasPrice"));
				properties.put(OceanConfig.AQUARIUS_URL, squid.getConfigString("aquarius.url"));
				properties.put(OceanConfig.SECRETSTORE_URL, squid.getConfigString("secretstore.url"));
				properties.put(OceanConfig.CONSUME_BASE_PATH, squid.getConfigString("consume.basePath"));
				properties.put(OceanConfig.MAIN_ACCOUNT_ADDRESS, squid.getConfigString("account.parity.address2"));
				properties.put(OceanConfig.MAIN_ACCOUNT_PASSWORD,  squid.getConfigString("account.parity.password2"));
				properties.put(OceanConfig.MAIN_ACCOUNT_CREDENTIALS_FILE, squid.getConfigString("account.parity.file2"));
				properties.put(OceanConfig.DID_REGISTRY_ADDRESS, squid.getConfigString("contract.didRegistry.address"));
				properties.put(OceanConfig.SERVICE_EXECUTION_AGREEMENT_ADDRESS, squid.getConfigString("contract.serviceExecutionAgreement.address"));
				properties.put(OceanConfig.PAYMENT_CONDITIONS_ADDRESS,squid.getConfigString("contract.paymentConditions.address"));
				properties.put(OceanConfig.ACCESS_CONDITIONS_ADDRESS, squid.getConfigString("contract.accessConditions.address"));
				properties.put(OceanConfig.TOKEN_ADDRESS, squid.getConfigString("contract.token.address"));
				properties.put(OceanConfig.DISPENSER_ADDRESS, squid.getConfigString("contract.dispenser.address"));
				oceanAPIConsumer = OceanAPI.getInstance(properties);
			} catch (Exception e) {
				System.out.println("unable to build squid: " + e);
			}
		}
	}

	@Test
	public void cGetPublisherAccount() {
		System.out.println("=== cGetPublisherAccount ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			String publisherAddress = squid.getConfigString("account.parity.address");
			String publisherPassword = squid.getConfigString("account.parity.password");
			System.out.println("publisherAddress: " + publisherAddress);
			publisherAccount = SquidAccount.create(publisherAddress, publisherPassword, squid);
			try {
				System.out.println("publisherAddress: " + publisherAddress + " balance: " + publisherAccount.balance());
			} catch (EthereumException e) {
				System.out.println("publisherAddress: " + publisherAddress + " UNABLE to get balance");
			}
			try {
				publisherAccount.unlock();
				try {
					publisherAccount.requestTokens(20);
				} catch (AuthorizationException e) {
					System.out.println("unable to request publisher tokens: " + e);
				}
			} catch (AuthorizationException e) {
				System.out.println("unable to unlock publisher account: " + e);
			}
		}
	}

	@Test
	public void dRegisterSLATemplate () {
		System.out.println("=== dRegisterSLATemplate ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void eCreateAsset() {
		System.out.println("=== eCreateAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			try {
				String consumeUrl= "http://localhost:8030/api/v1/brizo/services/consume?consumerAddress=${consumerAddress}&serviceAgreementId=${serviceAgreementId}&url=${url}";
				String purchaseEndpoint= "http://localhost:8030/api/v1/brizo/services/access/initialize";
				String metadataUrl= "http://172.15.0.15:5000/api/v1/aquarius/assets/ddo/{did}";
				serviceEndpoints= new ServiceEndpoints(consumeUrl, purchaseEndpoint, metadataUrl);
				serviceEndpoints.setSecretStoreEndpoint(squid.getConfigString("secretstore.url"));
				ddo = oceanAPI.getAssetsAPI().create(metadataBase, serviceEndpoints);
				did = new DID(ddo.id);
				DDO resolvedDDO = oceanAPI.getAssetsAPI().resolve(did);
				assertEquals(ddo.id, resolvedDDO.id);
				assertTrue( resolvedDDO.services.size() == 2);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				fail("unable to create asset: " + e);
			}
		}
	}

	@Test
	public void fRegisterListing() {
		System.out.println("=== fRegisterListing ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
		}
	}

	@Test
	public void gGetPurchaserAccount() {
		System.out.println("=== fGetPurchaserAccount ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			String purchaserAddress = squid.getConfigString("account.parity.address2");
			String purchaserPassword = squid.getConfigString("account.parity.password2");
			purchaserAccount = SquidAccount.create(purchaserAddress, purchaserPassword, squid);
			try {
				System.out.println("BEFORE purchaserAddress: " + purchaserAddress + " balance: " + purchaserAccount.balance());
			} catch (EthereumException e) {
				System.out.println("BEFORE purchaserAddress: " + purchaserAddress + " UNABLE to get balance");
			}
			try {
				purchaserAccount.unlock();
				try {
					purchaserAccount.requestTokens(10);
				} catch (AuthorizationException e) {
					System.out.println("unable to request purchaser tokens: " + e);
				}
			} catch (AuthorizationException e) {
				System.out.println("unable to unlock purchaser account: " + e);
			}
			try {
				System.out.println("AFTER purchaserAddress: " + purchaserAddress + " balance: " + purchaserAccount.balance());
			} catch (EthereumException e) {
				System.out.println("AFTER purchaserAddress: " + purchaserAddress + " UNABLE to get balance");
			}
		}
	}

	@Test
	public void hSearchListings() {
		System.out.println("=== hSearchListings ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			try {

				String searchText = "Weather";
				List<DDO> results = oceanAPI.getAssetsAPI().search(searchText);
				assertNotNull(results);
				Map<String, Object> params = new HashMap<>();
				params.put("license", "CC-BY");
				results = oceanAPI.getAssetsAPI().query(params);
				assertNotNull(results);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				fail("unable to search for asset: " + e);
			}

		}
	}

	@Test
	public void iPurchaseAsset() {
		System.out.println("=== iPurchaseAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			// BrizoMock?
		}
	}

	@Test
	public void jDownloadAsset() {
		System.out.println("=== jDownloadAsset ===");
		if (ocean == null)  {
			System.out.println("WARNING: barge not running");
		} else {
			try {
				Flowable<OrderResult> response = oceanAPIConsumer.getAssetsAPI().order(did,  Service.DEFAULT_ACCESS_SERVICE_ID);
				OrderResult orderResult = response.blockingFirst();
				assertNotNull(orderResult.getServiceAgreementId());
				assertEquals(true, orderResult.isAccessGranted());
				boolean result = oceanAPIConsumer.getAssetsAPI().consume(orderResult.getServiceAgreementId(), did, Service.DEFAULT_ACCESS_SERVICE_ID, "/tmp");
				assertEquals(true, result);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				fail("unable to download asset: " + e);
			}

		}
	}

}
