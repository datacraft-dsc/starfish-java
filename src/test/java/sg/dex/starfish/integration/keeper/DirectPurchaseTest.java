package sg.dex.starfish.integration.keeper;

import org.junit.Test;

import com.oceanprotocol.squid.exceptions.EthereumException;
import org.web3j.crypto.CipherException;
import java.io.IOException;

import java.math.BigInteger;
import com.oceanprotocol.squid.models.Account;
import com.oceanprotocol.squid.models.Balance;
import sg.dex.starfish.integration.squid.SquidHelperTest;
import sg.dex.starfish.keeper.DirectPurchaseAdapter;
import sg.dex.starfish.Ocean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectPurchaseTest {
    private DirectPurchaseAdapter directPurchaseAdapter;
    private byte[] reference;
    private BigInteger tokenAmount;
    String receiverAddress;
    String senderAddress;

    public DirectPurchaseTest() throws IOException, CipherException {
        directPurchaseAdapter = new DirectPurchaseAdapter();
        reference = new byte[32];
        reference[0] = 10;
        tokenAmount = BigInteger.valueOf(10);
        receiverAddress = "0x068Ed00cF0441e4829D9784fCBe7b9e26D4BD8d0";
        senderAddress = "0x00bd138abd70e2f00903268f3db08f2d25677c9e";
    }

    @Test
    public void Purchase() throws EthereumException {
        String receiverPasswd = "secret";

        String senderPasswd = "node0";

        Account senderAccount = new Account(senderAddress, senderPasswd);
        Account receiverAccount = new Account(receiverAddress, receiverPasswd);

        Ocean ocean = SquidHelperTest.getOcean();

        BigInteger amount = ocean.requestTokens(tokenAmount);
        assertEquals(amount, tokenAmount);

        Balance balanceSenderBefore = ocean.getBalance(senderAccount);
        Balance balanceReceiverBefore = ocean.getBalance(receiverAccount);

        boolean transferred = directPurchaseAdapter.sendTokenAndLog(receiverAddress, tokenAmount, reference, reference);

        assertTrue(transferred);
        Balance balanceSenderAfter = ocean.getBalance(senderAccount);
        Balance balanceReceiverAfter = ocean.getBalance(receiverAccount);
        assertEquals(balanceSenderBefore.getDrops().subtract(balanceSenderAfter.getDrops()), tokenAmount);
        assertEquals(balanceReceiverAfter.getDrops().subtract(balanceReceiverBefore.getDrops()), tokenAmount);
    }

    @Test
    public void check_is_paid() {
        boolean paid = directPurchaseAdapter.checkIsPaid(senderAddress, receiverAddress, tokenAmount, reference);
        assertTrue(paid);
    }
}

