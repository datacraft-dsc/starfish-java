package sg.dex.starfish.dexchain;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import sg.dex.starfish.dexchain.impl.Provenance;
import sg.dex.starfish.exception.DexChainException;
import sg.dex.starfish.util.Hex;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Dex Provenance
 * <p>
 * It provides interface for registering assets on chain and getting their provenance.
 * </p>
 *
 * @author Ilya Bukalov
 */
public class DexProvenance {
    private Provenance contract;
    private DexConfig config;

    public static class DexProvenanceResult {
        public String user;
        java.util.Date timeStamp;
    }

    /**
     * Creates default DexProvenance
     * @return DexProvenance The newly created DexProvenance
     */
    public static DexProvenance create() {
        DexConfig dexConfig = DexChain.getInstance().getDexConfig();
        Provenance contract = Provenance.load(
                dexConfig.getProvenanceAddress(),
                DexChain.getInstance().getWeb3j(),
                DexChain.getInstance().getTransactionManager(),
                DexChain.getInstance().getContractGasProvider());
        return new DexProvenance(contract, dexConfig);
    }

    /**
     * Create DexProvenance
     *
     * @param contract contract
     * @param config   config
     */
    private DexProvenance(Provenance contract, DexConfig config) {
        this.contract = contract;
        this.config = config;
    }

    /**
     * Register asset on chain.
     *
     * @param String assetId
     * @throws DexChainException
     */
    public void registerAsset(String assetId) throws DexChainException{
        TransactionReceipt receipt = null;

        try {
            receipt = contract.registerAsset(
                    Numeric.hexStringToByteArray(assetId)).send();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DexChainException(e);
        }

        if (!receipt.getStatus().equals("0x1"))
            throw new DexChainException();
    }

    /**
     * Get asset provenance from chain.
     *
     * @param String assetId
     * @return Array of DexProvenanceResult. Results of all records related to given asset
     * @throws DexChainException
     */
    public ArrayList<DexProvenanceResult> getAssetProvenance(String assetId) throws DexChainException {
        BigInteger blockNumber;
        try {
            blockNumber = contract.getBlockNumber(Numeric.hexStringToByteArray(assetId)).send();
        } catch (Exception e) {
            throw new DexChainException(e);
        }

        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(blockNumber), DefaultBlockParameterName.LATEST, contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Provenance.ASSETREGISTERED_EVENT));
        String didTopic = "0x" + assetId;
        filter.addOptionalTopics(didTopic);
        Flowable<Provenance.AssetRegisteredEventResponse> floable = contract.assetRegisteredEventFlowable(filter);
        ArrayList<DexProvenanceResult> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            DexProvenanceResult newResult = new DexProvenanceResult();
            newResult.timeStamp = new java.util.Date(log._timestamp.longValue() * 1000);
            newResult.user = log._user;
            outcome.add(newResult);
        });
        return  outcome;
    }

    /**
     * Search asset provenance from chain by user and assetId
     *
     * @param String user
     * @param String assetId
     * @return Array of DexProvenanceResult. Results of all records related to given asset
     * @throws DexChainException
     */
    public ArrayList<DexProvenanceResult> getAssetProvenance(String assetId, String user) throws DexChainException {
        BigInteger blockNumber;
        try {
            blockNumber = contract.getBlockNumber(Numeric.hexStringToByteArray(assetId)).send();
        } catch (Exception e) {
            throw new DexChainException(e);
        }

        String user_padded = Hex.toZeroPaddedHex(user);
        String assetId_padded = Hex.toZeroPaddedHex(assetId);

        EthFilter filter = new EthFilter(DefaultBlockParameter.valueOf(blockNumber), DefaultBlockParameterName.LATEST, contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Provenance.ASSETREGISTERED_EVENT));
        filter.addSingleTopic(assetId_padded);
        filter.addSingleTopic(user_padded);
        Flowable<Provenance.AssetRegisteredEventResponse> floable = contract.assetRegisteredEventFlowable(filter);
        ArrayList<DexProvenanceResult> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            DexProvenanceResult newResult = new DexProvenanceResult();
            newResult.timeStamp = new java.util.Date(log._timestamp.longValue() * 1000);
            newResult.user = log._user;
            outcome.add(newResult);
        });
        return  outcome;
    }

    /**
     * Search asset provenance from chain by user
     *
     * @param String user
     * @return Array of DexProvenanceResult. Results of all records related to given asset
     */
    public ArrayList<DexProvenanceResult> getAssetProvenanceByUser(String user) {
        String user_padded = Hex.toZeroPaddedHex(user);
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contract.getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(Provenance.ASSETREGISTERED_EVENT));
        filter.addNullTopic();
        filter.addSingleTopic(user_padded);
        Flowable<Provenance.AssetRegisteredEventResponse> floable = contract.assetRegisteredEventFlowable(filter);
        ArrayList<DexProvenanceResult> outcome = new ArrayList<>();
        floable.subscribe(log -> {
            DexProvenanceResult newResult = new DexProvenanceResult();
            newResult.timeStamp = new java.util.Date(log._timestamp.longValue() * 1000);
            newResult.user = log._user;
            outcome.add(newResult);
        });
        return  outcome;
    }
}