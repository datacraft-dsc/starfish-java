package sg.dex.starfish.integration.keeper;

import org.junit.Test;

import com.oceanprotocol.squid.exceptions.EthereumException;
import org.web3j.crypto.CipherException;
import java.io.IOException;

import java.math.BigInteger;
import com.oceanprotocol.squid.models.Account;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import sg.dex.starfish.impl.squid.SquidAccount;
import sg.dex.starfish.impl.squid.SquidService;
import sg.dex.starfish.keeper.DirectPurchaseAdapter;
import com.oceanprotocol.squid.api.OceanAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectPurchaseTest {
    private DirectPurchaseAdapter directPurchaseAdapter;
    private static final String reference = "0x000000000000a46daef00000000000";
    private static final BigInteger tokenAmount = BigInteger.valueOf(10);
    private static final String receiverAddress= "0x068Ed00cF0441e4829D9784fCBe7b9e26D4BD8d0";
    private static final String senderAddress= "0x00bd138abd70e2f00903268f3db08f2d25677c9e";

    public DirectPurchaseTest() throws IOException, CipherException {
        directPurchaseAdapter = DirectPurchaseAdapter.create();
    }

    @Test
    public void testPurchase() throws EthereumException {
        String receiverPasswd = "secret";
        String senderPasswd = "node0";

        Account senderAccount = new Account(senderAddress, senderPasswd);
        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        OceanAPI oceanAPI = SquidService.getOceanAPI();
        TransactionReceipt transactionReceipt = oceanAPI.getAccountsAPI().requestTokens(tokenAmount);
        assertTrue(transactionReceipt.isStatusOK());

        BigInteger balanceSenderBefore = SquidAccount.create(senderAccount).getOceanBalance();
        BigInteger balanceReceiverBefore = SquidAccount.create(receiverAccount).getOceanBalance();

        TransactionReceipt receipt = directPurchaseAdapter.sendTokenAndLog(receiverAddress, tokenAmount, reference, reference);
        assertTrue(receipt.isStatusOK());

        BigInteger balanceSenderAfter = SquidAccount.create(senderAccount).getOceanBalance();
        BigInteger balanceReceiverAfter = SquidAccount.create(receiverAccount).getOceanBalance();
        assertEquals(balanceSenderBefore.subtract(balanceSenderAfter), tokenAmount);
        assertEquals(balanceReceiverAfter.subtract(balanceReceiverBefore), tokenAmount);
    }

    @Test
    public void testCheckIsPaid() {
        boolean paid = directPurchaseAdapter.checkIsPaid(senderAddress, receiverAddress, tokenAmount, reference);
        assertTrue(paid);
    }
}

