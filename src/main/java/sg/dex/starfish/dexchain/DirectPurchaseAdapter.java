package sg.dex.starfish.dexchain;

import com.oceanprotocol.keeper.contracts.OceanToken;
import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;
import sg.dex.starfish.dexchain.impl.DexTransactionManager;
import sg.dex.starfish.dexchain.impl.DirectPurchase;
import sg.dex.starfish.util.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

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
    private OceanToken oceanToken;

    /**
     * Create DirectPurchaseAdapter
     *
     * @param directPurchase contract
     * @param oceanToken   contract
     */
    private DirectPurchaseAdapter(DirectPurchase directPurchase, OceanToken oceanToken) {
        this.directPurchase = directPurchase;
        this.oceanToken = oceanToken;
    }

    /**
     * Creates a DirectPurchaseAdapter
     *
     * @param  DexConfig config
     * @return DirectPurchaseAdapter The newly created DirectPurchaseAdapter
     * @throws IOException
     */
    public static DirectPurchaseAdapter create(DexConfig dexConfig) throws IOException {
        // getting properties

        String directPurchaseAddress = dexConfig.getDirectPurchaseAddress();
        String oceanTokenAddress = dexConfig.getTokenAddress();

        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(dexConfig.getMainAccountPassword(), dexConfig.getMainAccountCredentialsFile());
        } catch (CipherException e) {
            System.err.println("Wrong credential file or its password");
            e.printStackTrace();
        }

        Web3j web3 = Web3j.build(new HttpService(dexConfig.getKeeperUrl()));
        TransactionManager txManager = new DexTransactionManager(web3, credentials, (int) dexConfig.getKeeperTxSleepDuration(), dexConfig.getKeeperTxAttempts());
        ContractGasProvider gasProvider = new StaticGasProvider(dexConfig.getKeeperGasPrice(), dexConfig.getKeeperGasLimit());

        // loading contract instances
        DirectPurchase directPurchase = DirectPurchase.load(directPurchaseAddress,  web3, txManager, gasProvider);
        OceanToken oceanToken = OceanToken.load(oceanTokenAddress,  web3, txManager, gasProvider);
        return new DirectPurchaseAdapter(directPurchase, oceanToken);
    }

    /**
     * Sends tokens and log this transaction in blockchain.
     *
     * @param to         The publisher address who is paid for asset
     * @param amount     The amount of tokens being transferred
     * @param reference1 32 byte identifier (agent id)
     * @param reference2 Unique 32 byte identifier of asset
     * @return TransactionReceipt Ethereum transaction receipt.
     */
    public TransactionReceipt sendTokenAndLog(String to, BigInteger amount, String reference1, String reference2) {
        TransactionReceipt receipt = null;
        boolean tokenApproved = tokenApprove(directPurchase.getContractAddress(), amount.toString());
        if (tokenApproved) {
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
     */
    public boolean tokenApprove(String spenderAddress, String price) {
        String checksumAddress = Keys.toChecksumAddress(spenderAddress);

        try {

            TransactionReceipt receipt = oceanToken.approve(
                    checksumAddress,
                    new BigInteger(price)
            ).send();

            if (!receipt.getStatus().equals("0x1")) {
                System.err.println("The Status received is not valid executing Token Approve: " + receipt.getStatus());
                return false;
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error executing Token Approve " + ": " + e.getMessage());
            return false;
        }
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
    public boolean checkIsPaid(String purchaser_address, String publisher_address, BigInteger amount, String reference) {
        String purchaser_padded = Hex.toZeroPaddedHex(purchaser_address);
        String publisher_padded = Hex.toZeroPaddedHex(publisher_address);
        String reference_padded = Hex.toZeroPaddedHex(reference);
        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(1)), DefaultBlockParameterName.LATEST, directPurchase.getContractAddress());

        filter.addSingleTopic(EventEncoder.encode(DirectPurchase.TOKENSENT_EVENT));
        // addOptionalTopic does not work for some reason.
        filter.addSingleTopic(purchaser_padded);
        filter.addSingleTopic(publisher_padded);
        filter.addSingleTopic(reference_padded);

        Flowable<DirectPurchase.TokenSentEventResponse> floable = directPurchase.tokenSentEventFlowable(filter);
        ArrayList<DirectPurchase.TokenSentEventResponse> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            outcome.add(log);
        });

        for (DirectPurchase.TokenSentEventResponse obj : outcome) {
            if (obj._amount.equals(amount))
                return true;
        }
        return false;
    }

}
