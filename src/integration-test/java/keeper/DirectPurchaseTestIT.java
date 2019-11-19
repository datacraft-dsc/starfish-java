package keeper;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.Account;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.impl.squid.SquidService;
import sg.dex.starfish.keeper.DirectPurchaseAdapter;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@Ignore
public class DirectPurchaseTestIT {
    private DirectPurchaseAdapter directPurchaseAdapter;
    private static final String reference = "0x000000000000a46daef00000000000"; // must be any hardcoded value
    private static final BigInteger tokenAmount = BigInteger.valueOf(10);
    private Account senderAccount;
    private Account receiverAccount;

    private SquidService squidService;

    public DirectPurchaseTestIT() throws IOException, CipherException {
        squidService = SquidService.create("application_test.properties");
        directPurchaseAdapter = DirectPurchaseAdapter.create(squidService);

        String senderAddress = squidService.getProperties().getProperty("account.main.address", "");
        String receiverAddress = squidService.getProperties().getProperty("account.parity.address", "");
        String senderPasswd = squidService.getProperties().getProperty("account.main.password", "");
        String receiverPasswd = squidService.getProperties().getProperty("account.parity.password", "");

        senderAccount = new Account(senderAddress, senderPasswd);
        receiverAccount = new Account(receiverAddress, receiverPasswd);
    }

    @Test
    public void testPurchase() throws EthereumException {

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

