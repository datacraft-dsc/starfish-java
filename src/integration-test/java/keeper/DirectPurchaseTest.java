package keeper;

import org.junit.Ignore;
import org.junit.Test;

import com.oceanprotocol.squid.exceptions.EthereumException;
import org.web3j.crypto.CipherException;
import java.io.IOException;

import java.math.BigInteger;
import com.oceanprotocol.squid.models.Account;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.impl.squid.SquidService;
import sg.dex.starfish.keeper.DirectPurchaseAdapter;
import com.oceanprotocol.squid.api.OceanAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class DirectPurchaseTest {
    private DirectPurchaseAdapter directPurchaseAdapter;
    private static final String reference = "0x000000000000a46daef00000000000";
    private static final BigInteger tokenAmount = BigInteger.valueOf(10);
    private static final String receiverAddress= "0x068Ed00cF0441e4829D9784fCBe7b9e26D4BD8d0";
    private static final String senderAddress= "0x00bd138abd70e2f00903268f3db08f2d25677c9e";
    private SquidService squidService;

    public DirectPurchaseTest() throws IOException, CipherException {
        squidService =SquidService.create("application_test.properties");
        directPurchaseAdapter = DirectPurchaseAdapter.create(squidService);
    }

    @Test
    public void testPurchase() throws EthereumException {
        String receiverPasswd = "secret";
        String senderPasswd = "node0";

        Account senderAccount = new Account(senderAddress, senderPasswd);
        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        OceanAPI oceanAPI = squidService.getOceanAPI();
        TransactionReceipt transactionReceipt = oceanAPI.getAccountsAPI().requestTokens(tokenAmount);
        assertTrue(transactionReceipt.isStatusOK());

        BigInteger balanceSenderBefore = oceanAPI.getAccountsAPI().balance(senderAccount).getDrops();
        BigInteger balanceReceiverBefore = oceanAPI.getAccountsAPI().balance(receiverAccount).getDrops();

        TransactionReceipt receipt = directPurchaseAdapter.sendTokenAndLog(receiverAddress, tokenAmount, reference, reference);
        assertTrue(receipt.isStatusOK());

        BigInteger balanceSenderAfter = oceanAPI.getAccountsAPI().balance(senderAccount).getDrops();
        BigInteger balanceReceiverAfter = oceanAPI.getAccountsAPI().balance(receiverAccount).getDrops();
        assertEquals(balanceSenderBefore.subtract(balanceSenderAfter), tokenAmount);
        assertEquals(balanceReceiverAfter.subtract(balanceReceiverBefore), tokenAmount);
    }

    @Test
    public void testCheckIsPaid() {
        boolean paid = directPurchaseAdapter.checkIsPaid(senderAddress, receiverAddress, tokenAmount, reference);
        assertTrue(paid);
    }
}
