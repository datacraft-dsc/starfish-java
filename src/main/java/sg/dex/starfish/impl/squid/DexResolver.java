package sg.dex.starfish.impl.squid;

import com.oceanprotocol.keeper.contracts.DIDRegistry;
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
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.keeper.DexConfig;
import sg.dex.starfish.keeper.DexConfigFactory;
import sg.dex.starfish.keeper.DexTransactionManager;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class DexResolver implements Resolver {
    private DIDRegistry contract;
    private DexConfig config;

    /**
     * Create DexResolver
     *
     * @param contract     contract
     * @param DexConfig    config
     */
    private DexResolver(DIDRegistry contract, DexConfig config) {
        this.contract = contract;
        this.config = config;
    }

    /**
     * Creates default DexResolver
     *
     * @return DexResolver The newly created DexResolver
     * @throws IOException
     */
    public static Resolver create() throws IOException {
        // todo need to remote as create is failing now may be due to Nile network is issue
        return new LocalResolverImpl();
//         return create("resolver_default.properties");
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
        filter.addSingleTopic(EventEncoder.encode(DIDRegistry.DIDATTRIBUTEREGISTERED_EVENT));
        String didTopic = "0x" + didHash;
        filter.addOptionalTopics(didTopic);

        Flowable<DIDRegistry.DIDAttributeRegisteredEventResponse> floable = contract.dIDAttributeRegisteredEventFlowable(filter);
        ArrayList<DIDRegistry.DIDAttributeRegisteredEventResponse> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            outcome.add(log);
        });

        return outcome.size() == 1 ? outcome.get(0)._value : null;
    }

    @Override
    public void registerDID(DID did, String ddo) throws ResolverException {
        String checksum = "0x0";

        TransactionReceipt receipt = null;
        try {
            receipt = contract.registerAttribute(
                    Numeric.hexStringToByteArray(did.getID()),
                    Numeric.hexStringToByteArray(Hex.toZeroPaddedHexNoPrefix(checksum)),
                    Arrays.asList(config.getMainAccountAddress()), ddo).send();
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
