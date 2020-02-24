package sg.dex.starfish.dexchain;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.dexchain.impl.DIDRegistry;
import sg.dex.starfish.dexchain.impl.DexTransactionManager;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.util.DID;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

public class DexResolver implements Resolver {
    private DIDRegistry contract;
    private DexConfig config;

    /**
     * Create DexResolver
     *
     * @param contract     contract
     * @param config    config
     */
    private DexResolver(DIDRegistry contract, DexConfig config) {
        this.contract = contract;
        this.config = config;
    }

    /**
     * Creates default DexResolver
     *
     * @return DexResolver The newly created DexResolver
     */
    public static Resolver create() {
        DexConfig dexConfig = DexChain.getInstance().getDexConfig();
        DIDRegistry contract = DIDRegistry.load(
               dexConfig.getDidRegistryAddress(),
               DexChain.getInstance().getWeb3j(),
               DexChain.getInstance().getTransactionManager(),
               DexChain.getInstance().getContractGasProvider());
        return new DexResolver(contract, dexConfig);
    }

    /**
     * Creates a DexResolver
     *
     * @param configFile configFile. All information about account credentials will be taken from this file
     * @return DexResolver The newly created DexResolver
     * @throws IOException
     */
    public static DexResolver create(String configFile) throws IOException {
        DexConfig dexConfig = DexConfigFactory.getDexConfig(configFile);

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
        DIDRegistry contract = DIDRegistry.load(dexConfig.getDidRegistryAddress(), web3, txManager, gasProvider);
        return new DexResolver(contract, dexConfig);
    }

    @Override
    public String getDDOString(DID did) throws ResolverException {
        String didHash = did.getID();
        BigInteger blockNumber = BigInteger.valueOf(0);
        try {
            blockNumber = contract.getBlockNumberUpdated(Numeric.hexStringToByteArray(didHash)).send();
        } catch (UnsupportedEncodingException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(blockNumber), DefaultBlockParameter.valueOf(blockNumber), contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIDRegistry.DIDREGISTERED_EVENT));
        String didTopic = "0x" + didHash;
        filter.addOptionalTopics(didTopic);
        Flowable<DIDRegistry.DIDRegisteredEventResponse> floable = contract.dIDRegisteredEventFlowable(filter);
        ArrayList<DIDRegistry.DIDRegisteredEventResponse> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            outcome.add(log);
        });

        return outcome.size() == 1 ? outcome.get(0)._value : null;
    }

    @Override
    public void registerDID(DID did, String ddo) throws ResolverException {
        TransactionReceipt receipt = null;
        try {
            receipt = contract.registerDID(
                    Numeric.hexStringToByteArray(did.getID()), ddo).send();
        } catch (IOException e) {
            throw new ResolverException(e);
        } catch (CipherException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        if (!receipt.getStatus().equals("0x1"))
            throw new ResolverException();
    }
}
