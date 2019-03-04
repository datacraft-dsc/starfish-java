package sg.dex.starfish.impl.squid;

import java.math.BigInteger;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

import com.typesafe.config.Config;
import com.oceanprotocol.squid.api.OceanAPI;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import com.oceanprotocol.squid.exceptions.EthereumException;

/**
 * Class implementing a Squid Agent
 *
 * @author Tom
 *
 */
public class SquidAgent extends AAgent {

	private OceanAPI oceanAPI;
	private Config config;

	/**
	 * Creates a SquidAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param oceanapi Squid OceanAPI to use
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected SquidAgent(OceanAPI oceanAPI, Config config, Ocean ocean, DID did) {
		super(ocean, did);
		this.config = config;
		this.oceanAPI = oceanAPI;
	}

	/**
	 * Creates a RemoteAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param oceanapi Squid OceanAPI to use
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 * @return RemoteAgent
	 */
	public static SquidAgent create(OceanAPI oceanAPI, Config config, Ocean ocean, DID did) {
		return new SquidAgent(oceanAPI, config, ocean, did);
	}


	/**
	 * Registers an asset with this agent.
	 * The agent must support metadata storage.
	 *
	 * @param asset The asset to register
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if unable to register the Asset
	 * @throws UnsupportedOperationException if the agent does not support metadata storage
	 * @return Asset
	 */
	@Override
	public Asset registerAsset(Asset asset) {
		return asset;
	}

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retreiving the Asset
	 * @return Asset The asset found
	 */
	@Override
	public Asset getAsset(String id) {
		return null;
	}

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a Asset to upload
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException if there is an error in uploading the Asset
	 * @return Asset An asset stored on the agent if the upload is successful
	 */
	@Override
	public Asset uploadAsset(Asset a) {
		return a;
	}

	/**
	 * Returns a configuration String value for key
	 *
	 * @param key to find in the Config
	 * @return value corresponding to the key (or null if not found)
	 */
	public String getConfigString(String key) {
		String value = null;
		try {
			value = config.getString(key);
		} catch (Exception e) {
			// https://www.javadoc.io/doc/com.typesafe/config/1.3.3
		}
		return value;
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
	 * Request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws EthereumException on error
	 * @return number of tokens requested
	 */
	public int requestTokens(int amount) throws EthereumException {
		return requestTokens(BigInteger.valueOf(amount)).intValue();
	}

}
