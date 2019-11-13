package sg.dex.starfish.keeper;

import sg.dex.starfish.impl.squid.SquidService;
import com.oceanprotocol.common.web3.KeeperService;

import com.oceanprotocol.squid.exceptions.TokenApproveException;
import java.io.IOException;
import org.web3j.crypto.CipherException;

import org.web3j.utils.Numeric;
import sg.dex.starfish.util.Hex;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Properties;
import java.io.InputStream;
import com.oceanprotocol.keeper.contracts.OceanToken;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import org.web3j.protocol.core.methods.request.EthFilter;
import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;

/**
 * Direct purchase adapter.
 * <p>
 * It provides direct purchase functionality
 * </p>
 *
 * @author Ilya Bukalov
 */
public final class DirectPurchaseAdapter {
    private DirectPurchase directPurchase;
    private TokenManager tokenManager;

    /**
     * Create DirectPurchaseAdapter
     *
     * @param directPurchase  object
     * @param tokenManager    object
     */
    private DirectPurchaseAdapter(DirectPurchase directPurchase, TokenManager tokenManager)  {
        this.directPurchase = directPurchase;
        this.tokenManager = tokenManager;
    }

    /**
     * Creates a DirectPurchaseAdapter
     *
     * @throws IOException, CipherException
     * @return DirectPurchaseAdapter The newly created DirectPurchaseAdapter
     */
    public static DirectPurchaseAdapter create(SquidService squidService) throws IOException, CipherException{
        // getting properties
        String directPurchaseAddress = squidService.getProperties().getProperty("contract.DirectPurchase.address", "");
        String oceanTokenAddress = squidService.getProperties().getProperty("contract.OceanToken.address", "");

        // getting keeper
        KeeperService keeper = squidService.getKeeperService();
        // loading contract instances
        DirectPurchase directPurchase = DirectPurchase.load(directPurchaseAddress, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        OceanToken oceanToken = OceanToken.load(oceanTokenAddress, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        // initializing token manager
        TokenManager tokenManager = TokenManager.getInstance(keeper);
        tokenManager.setTokenContract(oceanToken);
        return new DirectPurchaseAdapter(directPurchase, tokenManager);
    }

    /**
     * Sends tokens and log this transaction in blockchain.
     *
     * @param to                  The publisher address who is paid for asset
     * @param amount              The amount of tokens being transferred
     * @param reference1          32 byte identifier (agent id)
     * @param reference2          Unique 32 byte identifier of asset
     * @return TransactionReceipt Ethereum transaction receipt.
     */
    public TransactionReceipt sendTokenAndLog(String to, BigInteger amount, String reference1, String reference2) {
        boolean tokenApproved = false;
        TransactionReceipt receipt = null;
        try {
            tokenApproved = tokenApprove(directPurchase.getContractAddress(), amount.toString());
        } catch (TokenApproveException e) {
            e.printStackTrace();
            return receipt;
        }

        if(tokenApproved) {
            try {
                byte[] ref1 = Numeric.hexStringToByteArray(Hex.toZeroPaddedHex(reference1));
                byte[] ref2 = Numeric.hexStringToByteArray(Hex.toZeroPaddedHex(reference2));
                receipt = directPurchase.sendTokenAndLog(to, amount, ref1, ref2).send();
            } catch (Exception e) {
                e.printStackTrace();
                return receipt;
            }
            return receipt;
        }
        return receipt;
    }

    /**
     * Returns whether the transaction is successful
     *
     * @param spenderAddress The address who will be allowed to spend tokens of signer
     * @param price          Amount of tokens to spend
     * @return boolean
     * @throws TokenApproveException
     */
    public boolean tokenApprove(String spenderAddress, String price) throws TokenApproveException {
        return tokenManager.tokenApprove(spenderAddress, price);
    }

    /**
     * Check whether purchase is done
     *
     * @param purchaser_address The address who paid for asset
     * @param publisher_address The address who was paid for asset
     * @param amount            Token amount to check
     * @param reference         Unique 32 byte identifier of asset
     * @return boolean          Result
     */
    public boolean checkIsPaid(String purchaser_address, String publisher_address, BigInteger amount, String reference)
    {
        String purchaser_padded = Hex.toZeroPaddedHex(purchaser_address);
        String publisher_padded = Hex.toZeroPaddedHex(publisher_address);
        String reference_padded = Hex.toZeroPaddedHex(reference);
        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(1)), DefaultBlockParameterName.LATEST, directPurchase.getContractAddress());

        filter.addSingleTopic(EventEncoder.encode(directPurchase.TOKENSENT_EVENT));
        // addOptionalTopic does not work for some reason.
        filter.addSingleTopic(purchaser_padded);
        filter.addSingleTopic(publisher_padded);
        filter.addSingleTopic(reference_padded);

        Flowable<DirectPurchase.TokenSentEventResponse> floable = directPurchase.tokenSentEventFlowable(filter);
        ArrayList<DirectPurchase.TokenSentEventResponse> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            outcome.add(log);
        });

        for (DirectPurchase.TokenSentEventResponse obj:outcome) {
            if (obj._amount.equals(amount))
                return true;
        }
        return false;
    }

    private static Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream input = DirectPurchaseAdapter.class.getClassLoader().getResourceAsStream("ded.properties")) {

            if (input == null) {
                throw new IOException("properties files is missing");
            }

            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
