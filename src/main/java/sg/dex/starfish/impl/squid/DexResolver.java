package sg.dex.starfish.impl.squid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.web3j.abi.EventEncoder;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.oceanprotocol.common.helpers.EncodingHelper;
import com.oceanprotocol.common.web3.KeeperService;
import com.oceanprotocol.keeper.contracts.DIDRegistry;
import com.oceanprotocol.squid.api.config.OceanConfig;
import com.oceanprotocol.squid.api.config.OceanConfigFactory;
import com.oceanprotocol.squid.exceptions.DIDFormatException;

import io.reactivex.Flowable;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

public class DexResolver implements Resolver {
    private DIDRegistry contract;
    private SquidService squidService;

    /**
     * Create DexResolver
     *
     * @param DIDRegistry contract
     * @param SquidService squidService
     */
    private DexResolver(DIDRegistry contract, SquidService squidService)  {
        this.contract = contract;
        this.squidService = squidService;
    }

    /**
     * Creates a DexResolver
     *
     * @param squidService SquidService with set Squid configuration
     * @param addressFrom owner of DID record in the ledger. Only this address is allowed to update it
     * @param password its password
     * @param credentialFile its parity credential file
     * @throws IOException, CipherException
     * @return DexResolver The newly created DexResolver
     */
    public static DexResolver create(SquidService squidService, String addressFrom, String password, String credentialFile) throws IOException, CipherException{
        Properties properties = squidService.getProperties();
        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        oceanConfig.setMainAccountAddress(addressFrom);
        oceanConfig.setMainAccountPassword(password);
        oceanConfig.setMainAccountCredentialsFile(credentialFile);
        String address = (String)properties.getOrDefault("contract.DIDRegistry.address", "");
        KeeperService keeper = squidService.getKeeperService(oceanConfig);
        DIDRegistry contract = DIDRegistry.load(address, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        return new DexResolver(contract, squidService);
    }

    /**
     * Creates a DexResolver
     *
     * @param configFile  All information about account credentials will be taken from this file
     * @throws IOException, CipherException
     * @return DexResolver The newly created DexResolver
     */
    public static DexResolver create(String configFile) throws IOException, CipherException{
        SquidService squidService = SquidService.create(configFile);
        Properties properties = squidService.getProperties();
        String address = (String)properties.getOrDefault("contract.DIDRegistry.address", "");
        OceanConfig oceanConfig = OceanConfigFactory.getOceanConfig(properties);
        KeeperService keeper = squidService.getKeeperService(oceanConfig);
        DIDRegistry contract = DIDRegistry.load(address, keeper.getWeb3(), keeper.getTxManager(), keeper.getContractGasProvider());
        return new DexResolver(contract, squidService);
    }

    @Override
    public String getDDOString(DID did) throws ResolverException {
        com.oceanprotocol.squid.models.DID squidDID = null;
        try {
            squidDID = new com.oceanprotocol.squid.models.DID(did.toString());
        } catch (DIDFormatException e) {
            throw new ResolverException(e);
        }

        String didHash = squidDID.getHash();
        BigInteger blockNumber = BigInteger.valueOf(0);
        try {
            blockNumber = (BigInteger) contract.getBlockNumberUpdated(EncodingHelper.hexStringToBytes(didHash)).send();
        } catch (UnsupportedEncodingException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(blockNumber), DefaultBlockParameter.valueOf(blockNumber), contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DIDRegistry.DIDATTRIBUTEREGISTERED_EVENT));
        String didTopic = "0x" + didHash;
        filter.addOptionalTopics(new String[]{didTopic});

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

        com.oceanprotocol.squid.models.DID didSquid = null;
        try {
            didSquid = new com.oceanprotocol.squid.models.DID(did.toString());
        } catch (DIDFormatException e) {
            throw new ResolverException(e);
        }

        TransactionReceipt receipt = null;
        try {
            receipt = (TransactionReceipt)contract.registerAttribute(
                    EncodingHelper.hexStringToBytes(didSquid.getHash()),
                    EncodingHelper.hexStringToBytes(Hex.toZeroPaddedHexNoPrefix(checksum)),
                    Arrays.asList(squidService.getProvider()), ddo).send();
        } catch (IOException e) {
            throw new ResolverException(e);
        } catch (CipherException e) {
            throw new ResolverException(e);
        } catch (Exception e) {
            throw new ResolverException(e);
        }

        if(!receipt.getStatus().equals("0x1"))
            throw new ResolverException();
    }
}
