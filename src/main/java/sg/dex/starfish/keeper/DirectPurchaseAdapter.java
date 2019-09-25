package sg.dex.starfish.keeper;

import sg.dex.starfish.impl.squid.SquidService;
import com.oceanprotocol.common.web3.KeeperService;

import com.oceanprotocol.squid.exceptions.TokenApproveException;
import java.io.IOException;
import org.web3j.crypto.CipherException;

import org.web3j.utils.Numeric;
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

public final class DirectPurchaseAdapter {
    private DirectPurchase directPurchase;
    private TokenManager tokenManager;
    private String directPurchaseAddress;

    public DirectPurchaseAdapter() throws IOException, CipherException {
        // getting properties
        Properties properties = getProperties();
        directPurchaseAddress = (String)properties.getOrDefault("contract.DirectPurchase.address", "");
        String oceanTokenAddress = (String)properties.getOrDefault("contract.OceanToken.address", "");
        // getting keeper
        KeeperService keeper = SquidService.getKeeperService(properties);
        // loading contract instances
        directPurchase = DirectPurchase.load(directPurchaseAddress, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        OceanToken oceanToken = OceanToken.load(oceanTokenAddress, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        // initializing token manager
        tokenManager = TokenManager.getInstance(keeper);
        tokenManager.setTokenContract(oceanToken);
    }

    public boolean sendTokenAndLog(String to, BigInteger amount, byte[] reference1, byte[] reference2) {
        boolean tokenApproved = false;
        try {
            tokenApproved = tokenApprove(this.directPurchaseAddress, amount.toString());
        } catch (TokenApproveException e) {
            e.printStackTrace();
            return false;
        }

        if(tokenApproved) {
            try {
                TransactionReceipt receipt = directPurchase.sendTokenAndLog(to, amount, reference1, reference2).send();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean tokenApprove(String spenderAddress, String price) throws TokenApproveException {
        return tokenManager.tokenApprove(spenderAddress, price);
    }

    public boolean checkIsPaid(String purchaser_address, String publisher_address, BigInteger amount, byte[] reference)
    {
        String purchaser_padded = Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(purchaser_address), 64);
        String publisher_padded = Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(publisher_address), 64);
        String reference_padded = Numeric.toHexStringWithPrefixZeroPadded(Numeric.toBigInt(reference), 64);
        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(1)), DefaultBlockParameterName.LATEST, directPurchase.getContractAddress());

        filter.addSingleTopic(EventEncoder.encode(directPurchase.TOKENSENT_EVENT));
        // addOtionalTopic does not work for some reason.
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

    private Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {

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
