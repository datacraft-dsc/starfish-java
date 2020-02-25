package sg.dex.starfish.dexchain.impl;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.14.
 */
@SuppressWarnings("rawtypes")
public class Provenance extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b506102eb806100206000396000f3fe608060405234801561001057600080fd5b506004361061005e576000357c01000000000000000000000000000000000000000000000000000000009004806347378145146100635780638129fc1c146100a5578063ad1a47c8146100af575b600080fd5b61008f6004803603602081101561007957600080fd5b81019080803590602001909291905050506100dd565b6040518082815260200191505060405180910390f35b6100ad6100fa565b005b6100db600480360360208110156100c557600080fd5b81019080803590602001909291905050506101f9565b005b600060336000838152602001908152602001600020549050919050565b600060019054906101000a900460ff16806101195750610118610280565b5b8061013057506000809054906101000a900460ff16155b610185576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180610292602e913960400191505060405180910390fd5b60008060019054906101000a900460ff1615905080156101d5576001600060016101000a81548160ff02191690831515021790555060016000806101000a81548160ff0219169083151502179055505b80156101f65760008060016101000a81548160ff0219169083151502179055505b50565b60006033600083815260200190815260200160002054141561022e574360336000838152602001908152602001600020819055505b3373ffffffffffffffffffffffffffffffffffffffff16817f183dd7ee8d3db71edaccba8d77d2f4ba0c9ca60c557f9fd881798fcf62719d5f426040518082815260200191505060405180910390a350565b600080303b9050600081149150509056fe436f6e747261637420696e7374616e63652068617320616c7265616479206265656e20696e697469616c697a6564a165627a7a723058201c86a9bfd0c3bc40a5052db8c2b649c0d761d7313bbeb37ce2a5025ccae9a3570029";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_REGISTERDID = "registerDID";

    public static final String FUNC_GETBLOCKNUMBER = "getBlockNumber";

    public static final Event ASSETREGISTERED_EVENT = new Event("AssetRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected Provenance(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Provenance(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Provenance(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Provenance(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AssetRegisteredEventResponse> getAssetRegisteredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ASSETREGISTERED_EVENT, transactionReceipt);
        ArrayList<AssetRegisteredEventResponse> responses = new ArrayList<AssetRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AssetRegisteredEventResponse typedResponse = new AssetRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._assetID = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._user = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AssetRegisteredEventResponse> assetRegisteredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AssetRegisteredEventResponse>() {
            @Override
            public AssetRegisteredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ASSETREGISTERED_EVENT, log);
                AssetRegisteredEventResponse typedResponse = new AssetRegisteredEventResponse();
                typedResponse.log = log;
                typedResponse._assetID = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._user = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AssetRegisteredEventResponse> assetRegisteredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ASSETREGISTERED_EVENT));
        return assetRegisteredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALIZE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerDID(byte[] _assetId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTERDID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_assetId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getBlockNumber(byte[] _assetId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBLOCKNUMBER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_assetId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Provenance load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Provenance(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Provenance load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Provenance(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Provenance load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Provenance(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Provenance load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Provenance(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Provenance> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Provenance.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Provenance> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Provenance.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Provenance> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Provenance.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Provenance> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Provenance.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class AssetRegisteredEventResponse extends BaseEventResponse {
        public byte[] _assetID;

        public String _user;

        public BigInteger _timestamp;
    }
}
