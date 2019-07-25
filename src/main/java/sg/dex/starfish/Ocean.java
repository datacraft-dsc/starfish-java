package sg.dex.starfish;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.api.AssetsAPI;
import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.JSONObjectCache;
import sg.dex.starfish.util.Utils;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry point for Ocean ecosystem capabilities.
 * <p>
 * An instance of the Ocean class is used to represent a connection to the Ocean network
 * and supports functionality related to accessing the on-chain state of the Ocean network.
 *
 * @author Mike
 */
public class Ocean {
    private static final Ocean DEFAULT_OCEAN = new Ocean(null);

    private final Map<DID, String> ddoCache = new HashMap<DID, String>();

    private final OceanAPI oceanAPI;

    private Ocean(OceanAPI oceanAPI) {
        this.oceanAPI = oceanAPI;
    }

    /**
     * Gets an instance of an Ocean object with the default configuration
     *
     * @return An Ocean instance with default configuration
     */
    public static Ocean connect() {
        return DEFAULT_OCEAN;
    }

    /**
     * Gets an instance of an Ocean object with the given OceanAPI instance.
     *
     * @param oceanAPI OceanAPI instance to create a connection with
     * @return An Ocean instance with default configuration
     */
    public static Ocean connect(OceanAPI oceanAPI) {
        return new Ocean(oceanAPI);
    }

    /**
     * Gets a DDO for a specified DID via the Universal resolver
     *
     * @param did DID to resolve
     * @return The DDO as a JSON map
     */
    public Map<String, Object> getDDO(String did) {
        return getDDO(DID.parse(did));
    }

    /**
     * Registers a DID with a DDO in the context of this Ocean connection on the local machine.
     * <p>
     * This registration is intended for testing purposes.
     *
     * @param did A did to register
     * @param ddo A string containing a valid Ocean DDO
     */
    public void registerLocalDID(DID did, String ddo) {
        ddoCache.put(did, ddo);
    }


    /**
     * Gets a DDO for a specified DID via the Universal Resolver.
     * Returns null if the DDO cannot be found.
     *
     * @param did DID to resolve
     * @return The DDO as a JSON map
     * @throws UnsupportedOperationException not yet implemented
     */
    public Map<String, Object> getDDO(DID did) {
        String localDDO = ddoCache.get(did);
        if (localDDO != null) {
            return JSONObjectCache.parse(localDDO);
        }
        // it is squid did
        else {
            DID didSquid = DID.parse(did.toString());
            return getAsset(didSquid).getMetadata();
        }
//		// TODO universal resolver
//		throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * Gets the Squid OceanAPI for this Ocean connection
     *
     * @return OceanAPI instance
     */
    public OceanAPI getOceanAPI() {
        return oceanAPI;
    }

    /**
     * Gets the Squid AssetsAPI for this Ocean connection
     *
     * @return AssetsAPI instance
     */
    public AssetsAPI getAssetsAPI() {
        return oceanAPI.getAssetsAPI();
    }

    /**
     * Gets the Squid AccountsAPI for this Ocean connection
     *
     * @return AccountsAPI instance
     */
    public AccountsAPI getAccountsAPI() {
        return oceanAPI.getAccountsAPI();
    }

    /**
     * Gets the agent for a given DID
     *
     * @param did The DID for the agent to resolve
     * @return Agent instance, or null if not able to resolve the DID
     */
    public Agent getAgent(DID did) {
        // TODO: resolve DDO for squid
        return RemoteAgent.create(this, did);
    }

    /**
     * Attempts to resolve an asset for a given DID
     *
     * @param did The DID
     * @return The Asset for the given DID, or null if not found
     */
    public Asset getAsset(DID did) {
        if (did.getPath() == null) {
            // resolve using Squid
            return SquidAsset.create(this, did);
        } else {
            // resolve using DEP protocol
            Agent ag = getAgent(did);
            return ag.getAsset(did);
        }
    }

    /**
     * Returns the Balance of an account register in the Keeper
     *
     * @param account the account we want to get the balance
     * @return the Balance of the account
     * @throws EthereumException EthereumException
     */
    public Balance getBalance(Account account) throws EthereumException {
        return oceanAPI.getAccountsAPI().balance(account);
    }

    /**
     * API to get the list of accounts register in the Keeper
     *
     * @return a List of all Account registered in Keeper
     * @throws EthereumException EthereumException
     */
    public List<Account> list() throws EthereumException {
        return oceanAPI.getAccountsAPI().list();
    }

    /**
     * Request some ocean tokens
     *
     * @param amount The amount of ocean tokens to transfer
     * @return number of tokens requested
     * @throws EthereumException on error
     */
    public BigInteger requestTokens(BigInteger amount) throws EthereumException {
        TransactionReceipt receipt = oceanAPI.getAccountsAPI().requestTokens(amount);
        if (!receipt.isStatusOK()) {
            amount = BigInteger.ZERO;
        }
        return amount;
    }

    /**
     * Transfer tokens from one account to the receiver address
     *
     * @param receiverAddress Address of the transfer receiver
     * @param amount          Amount of tokens to transfer
     * @return TransactionReceipt indicating success/failure of the transfer
     * @throws EthereumException EVM error
     */
    public TransactionReceipt transfer(String receiverAddress, BigInteger amount) throws EthereumException {
        return getOceanAPI().getTokensAPI().transfer(receiverAddress, amount);
    }

    /**
     * API to get the Transaction details from Submarine based on account and the submarine url
     *
     * @param account account number
     * @param url url
     * @return Map of all transaction
     * @throws URISyntaxException URI Syntax Exception will be thrown
     */

    public Map<String,Object> getTransaction(String url,String account) throws URISyntaxException {

        URI uri = new URI(url);

         uri = new URIBuilder(uri).addParameter("module",
                "account").addParameter("action", "txlist").addParameter("address", account).build();

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse closeableHttpResponse = HTTP.execute(httpGet);

        try {
            StatusLine statusLine = closeableHttpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 404) {
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(closeableHttpResponse));
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> transactionDetailsMap = mapper.readValue(body, Map.class);
                return transactionDetailsMap;

            }
        } catch (Exception e) {
           throw new RemoteException("Error in parsing response for "+ uri);

        }
        return Collections.emptyMap();

    }


}
