package keeper;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.impl.squid.SquidService;
import sg.dex.starfish.keeper.DexConfig;
import sg.dex.starfish.keeper.DexConfigFactory;
import sg.dex.starfish.keeper.DirectPurchaseAdapter;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectPurchaseTestIT {
    private static final String reference = "0x000000000000a46daef00000000000"; // must be any hardcoded value
    private static final BigInteger tokenAmount = BigInteger.valueOf(10);
    private DirectPurchaseAdapter directPurchaseAdapter;
    private Account senderAccount;
    private Account receiverAccount;

    public DirectPurchaseTestIT() throws IOException, CipherException {
        DexConfig dexConfig = DexConfigFactory.getDexConfig("application_test.properties");

        directPurchaseAdapter = DirectPurchaseAdapter.create(dexConfig);

        String senderAddress = dexConfig.getMainAccountAddress();
        String receiverAddress = dexConfig.getParityAccountAddress();
        String senderPasswd = dexConfig.getMainAccountPassword();
        String receiverPasswd = dexConfig.getParityAccountPassword();

        senderAccount = new Account(senderAddress, senderPasswd);
        receiverAccount = new Account(receiverAddress, receiverPasswd);
    }

    @Test
    public void testPurchase() throws EthereumException {

        SquidService squidService = SquidService.create("application_test.properties");
        OceanAPI oceanAPI = squidService.getOceanAPI();
        TransactionReceipt transactionReceipt = oceanAPI.getAccountsAPI().requestTokens(tokenAmount);
        assertTrue(transactionReceipt.isStatusOK());

        BigInteger balanceSenderBefore = oceanAPI.getAccountsAPI().balance(senderAccount).getDrops();
        BigInteger balanceReceiverBefore = oceanAPI.getAccountsAPI().balance(receiverAccount).getDrops();

        TransactionReceipt receipt = directPurchaseAdapter.sendTokenAndLog(receiverAccount.getAddress(), tokenAmount, reference, reference);
        assertTrue(receipt.isStatusOK());

        BigInteger balanceSenderAfter = oceanAPI.getAccountsAPI().balance(senderAccount).getDrops();
        BigInteger balanceReceiverAfter = oceanAPI.getAccountsAPI().balance(receiverAccount).getDrops();
        assertEquals(tokenAmount, balanceSenderBefore.subtract(balanceSenderAfter));
        assertEquals(tokenAmount, balanceReceiverAfter.subtract(balanceReceiverBefore));
    }

    @Test
    public void testCheckIsPaid() {
        boolean paid = directPurchaseAdapter.checkIsPaid(senderAccount.getAddress(), receiverAccount.getAddress(), tokenAmount, reference);
        assertTrue(paid);
    }
}

