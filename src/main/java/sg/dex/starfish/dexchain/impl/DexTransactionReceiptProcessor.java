package sg.dex.starfish.dexchain.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.util.Optional;

public class DexTransactionReceiptProcessor extends TransactionReceiptProcessor {
    private static final Logger log = LogManager.getLogger(DexTransactionReceiptProcessor.class);
    private final long sleepDuration;
    private final int attempts;
    private final Web3j web3j;

    public DexTransactionReceiptProcessor(Web3j web3j, long sleepDuration, int attempts) {
        super(web3j);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
        this.web3j = web3j;
    }

    public TransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        return this.getTransactionReceipt(transactionHash, this.sleepDuration, this.attempts);
    }

    Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException {
        EthGetTransactionReceipt transactionReceipt = this.web3j.ethGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: " + transactionReceipt.getError().getMessage());
        } else {
            return transactionReceipt.getTransactionReceipt();
        }
    }

    private Boolean keepWaiting(Optional<TransactionReceipt> receiptOptional) {
        if (!receiptOptional.isPresent()) {
            return true;
        } else {
            TransactionReceipt receipt = receiptOptional.get();
            Optional<Log> optionalLog = receipt.getLogs().stream().filter((log) -> {
                return !log.getType().equalsIgnoreCase("mined");
            }).findFirst();
            if (optionalLog.isPresent()) {
                log.debug("Not mined transaction receipt. Waiting until transaction get mined...");
                return true;
            } else {
                return false;
            }
        }
    }

    private TransactionReceipt getTransactionReceipt(String transactionHash, long sleepDuration, int attempts) throws IOException, TransactionException {
        Optional<TransactionReceipt> receiptOptional = this.sendTransactionReceiptRequest(transactionHash);

        for (int i = 0; i < attempts; ++i) {
            if (!this.keepWaiting(receiptOptional)) {
                return receiptOptional.get();
            }

            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException var8) {
                throw new TransactionException(var8);
            }

            receiptOptional = this.sendTransactionReceiptRequest(transactionHash);
        }

        throw new TransactionException("Transaction receipt was not generated after " + sleepDuration * (long) attempts / 1000L + " seconds for transaction: " + transactionHash, transactionHash);
    }
}
