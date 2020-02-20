package sg.dex.starfish.dexchain;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import sg.dex.starfish.dexchain.impl.DexTransactionManager;

import java.io.IOException;

public class DexChain {
    private static DexChain instance = null;

    private Web3j web3;
    private TransactionManager txManager;
    private ContractGasProvider gasProvider;
    private DexConfig dexConfig;
    
    private DexChain()
    {
        dexConfig = DexConfigFactory.getDexConfig("dex_chain.properties");

        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(dexConfig.getMainAccountPassword(), dexConfig.getMainAccountCredentialsFile());
        } catch (CipherException | IOException e) {
            System.err.println("Wrong credential file or its password");
            e.printStackTrace();
        }

        web3 = Web3j.build(new HttpService(dexConfig.getKeeperUrl()));
        txManager = new DexTransactionManager(web3, credentials, (int) dexConfig.getKeeperTxSleepDuration(), dexConfig.getKeeperTxAttempts());
        gasProvider = new StaticGasProvider(dexConfig.getKeeperGasPrice(), dexConfig.getKeeperGasLimit());
    }

    public static DexChain getInstance()
    {
        if (instance == null)
            instance = new DexChain();

        return instance;
    }

    public Web3j getWeb3j() {
        return web3;
    }

    public TransactionManager getTransactionManager() {
        return txManager;
    }

    public ContractGasProvider getContractGasProvider() {
        return gasProvider;
    }

    public DexConfig getDexConfig() {
        return dexConfig;
    }
}
