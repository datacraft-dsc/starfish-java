package sg.dex.starfish.impl.squid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import com.oceanprotocol.squid.models.DDO;
import com.oceanprotocol.squid.models.asset.AssetMetadata;
import com.oceanprotocol.squid.models.service.ProviderConfig;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing a Squid Agent
 *
 * @author Tom
 *
 */
public class SquidAgent extends AAgent {

	private final Ocean ocean;
	private final OceanAPI oceanAPI;
	private final Map<String,String> config;
	private final ProviderConfig providerConfig;


	/**
	 * Creates a SquidAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected SquidAgent(Map<String,String> config,
			     Ocean ocean, DID did,ProviderConfig providerConfig) {
		super(ocean, did);
		this.config = config;
		this.ocean=ocean;
		this.oceanAPI = ocean.getOceanAPI();
		this.providerConfig = providerConfig;
	}


	/**
	 * Creates a RemoteAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 * @return RemoteAgent
	 */
	public static SquidAgent create( Map<String,String> config, Ocean ocean, DID did,ProviderConfig providerConfig) {
		return new SquidAgent(config, ocean, did,providerConfig);
	}


	/**
	 * Registers an asset with this agent.
	 * The agent must support metadata storage.
	 *
	 * @param asset The asset to register
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if unable to register the SquidAsset
	 * @throws UnsupportedOperationException if the agent does not support metadata storage
	 * @return SquidAsset
	 */
	@Override
	public SquidAsset registerAsset(Asset asset) {

		try {
			return getSquidAsset((SquidAsset)asset);

		} catch (DIDFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DDOException e) {
			e.printStackTrace();
		}
		//Todo FIX IT, may throw exception
		return null;
	}

	/**
	 * API to get the Squid Asset after registering to Ocean Network
	 * @param squidAsset
	 * @return
	 * @throws IOException
	 * @throws DDOException
	 * @throws DIDFormatException
	 */
	private SquidAsset getSquidAsset(SquidAsset squidAsset) throws IOException, DDOException, DIDFormatException {

		Map<String, Object> squidMetaDAta = new HashMap<>();
		squidMetaDAta.put("base", squidAsset.getMetadata());

		AssetMetadata metadataBase = DDO.fromJSON(new TypeReference<AssetMetadata>() {
		}, JSON.toString(squidMetaDAta));

		DDO squidDDO = oceanAPI.getAssetsAPI().create(metadataBase, providerConfig);
		com.oceanprotocol.squid.models.DID squidDID = new com.oceanprotocol.squid.models.DID(squidDDO.id);
		DID surferDID = DID.parse(squidDID.toString());
		return  SquidAsset.create(squidAsset.getMetadataString(),surferDID,squidDDO,squidAsset.getOcean());

	}

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retrieving the SquidAsset
	 * @return SquidAsset The asset found
	 */
	@Override
	public SquidAsset getAsset(String id) {
		return (SquidAsset)ocean.getAsset(did);
	}
	
	@Override
	public SquidAsset getAsset(DID did) {
		return (SquidAsset)ocean.getAsset(did);
	}

	/**
	 * API to uploads an squid asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a SquidAsset to upload
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException if there is an error in uploading the SquidAsset
	 * @return SquidAsset An asset stored on the agent if the upload is successful
	 */
	@Override
	public SquidAsset uploadAsset(Asset a) {
		return (SquidAsset)a; // TODO: FIXME
	}


	/**
	 * Request some ocean tokens
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws EthereumException on error
	 * @return number of tokens requested
	 */
	public BigInteger requestTokens(BigInteger amount) throws EthereumException {
		TransactionReceipt receipt = oceanAPI.getAccountsAPI().requestTokens(amount);
		if (!receipt.isStatusOK()) {
			amount = BigInteger.ZERO;
		}
		return amount;
	}

	/**
	 * API to request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws EthereumException on error
	 * @return number of tokens requested
	 */
	public int requestTokens(int amount) throws EthereumException {
		return requestTokens(BigInteger.valueOf(amount)).intValue();
	}

	/**
	 * Returns the Balance of an account register in the Keeper
	 * @param account the account we want to get the balance
	 * @return the Balance of the account
	 * @throws EthereumException EthereumException
	 */
	public Balance balance(Account account) throws EthereumException {
		return oceanAPI.getAccountsAPI().balance(account);
	}

	/**
	 * API to get the list of accounts register in the Keeper
	 * @return a List of all Account registered in Keeper
	 * @throws EthereumException EthereumException
	 */
	public List<Account> list() throws EthereumException {
		return oceanAPI.getAccountsAPI().list();
	}

}
