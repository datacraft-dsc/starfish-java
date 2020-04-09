package sg.dex.starfish.dexchain.impl;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.utils.Numeric;
import org.web3j.utils.TxHashVerifier;

import java.io.IOException;
import java.math.BigInteger;


public class DexTransactionManager extends TransactionManager {
    private final Web3j web3j;
    private final Credentials credentials;
    protected TxHashVerifier txHashVerifier;

    public DexTransactionManager(Web3j web3j, Credentials credentials, int attempts, long sleepDuration) {
        super(new DexTransactionReceiptProcessor(web3j, sleepDuration, attempts), credentials.getAddress());
        this.txHashVerifier = new TxHashVerifier();
        this.web3j = web3j;
        this.credentials = credentials;
    }

    protected BigInteger getNonce() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = this.web3j.ethGetTransactionCount(this.credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    protected BigInteger getEstimatedGas(String to, String data) throws IOException {
        BigInteger gas = this.web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();
        return gas;
    }

    public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
        BigInteger nonce = this.getNonce();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, this.getEstimatedGas(to, data), gasLimit, to, value, data);
        return this.signAndSend(rawTransaction);
    }

    @Override
    public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value, boolean constructor) throws IOException {
        //todo need to implement
        return null;
    }

    @Override
    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter) throws IOException {
        //todo need to implement
        return null;
    }

    public String sign(RawTransaction rawTransaction) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, this.credentials);
        return Numeric.toHexString(signedMessage);
    }

    public EthSendTransaction signAndSend(RawTransaction rawTransaction) throws IOException {
        String hexValue = this.sign(rawTransaction);
        EthSendTransaction ethSendTransaction = this.web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!this.txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return ethSendTransaction;
    }
}
