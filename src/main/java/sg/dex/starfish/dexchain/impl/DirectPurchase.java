package sg.dex.starfish.dexchain.impl;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.*;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.0.
 */
public class DirectPurchase extends Contract {
    public static final String FUNC_INITIALIZE = "initialize";
    public static final String FUNC_SENDTOKENANDLOG = "sendTokenAndLog";
    public static final Event TOKENSENT_EVENT = new Event("TokenSent",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Bytes32>() {
            }, new TypeReference<Bytes32>(true) {
            }));
    protected static final HashMap<String, String> _addresses;
    private static final String BINARY = "0x608060405234801561001057600080fd5b5061041a806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063026a0ab21461003b578063c4d66de81461009d575b600080fd5b61009b6004803603608081101561005157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190803590602001909291905050506100e1565b005b6100df600480360360208110156100b357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061026e565b005b603360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3386866040518463ffffffff1660e01b8152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156101be57600080fd5b505af11580156101d2573d6000803e3d6000fd5b505050506040513d60208110156101e857600080fd5b810190808051906020019092919050505050808473ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f93534e11db56134f1ef4a5ce6e165fdd6d7b546f083c50967d27f870be7199658686604051808381526020018281526020019250505060405180910390a450505050565b600060019054906101000a900460ff168061028d575061028c6103af565b5b806102a457506000809054906101000a900460ff16155b6102f9576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e8152602001806103c1602e913960400191505060405180910390fd5b60008060019054906101000a900460ff161590508015610349576001600060016101000a81548160ff02191690831515021790555060016000806101000a81548160ff0219169083151502179055505b81603360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080156103ab5760008060016101000a81548160ff0219169083151502179055505b5050565b600080303b9050600081149150509056fe436f6e747261637420696e7374616e63652068617320616c7265616479206265656e20696e697469616c697a6564a165627a7a72305820a4e9621e552eae146f4ee2eeb0f991b9221439a55073e7d7584da7b6db9ba65b0029";

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("8996", "0xc0C528855f27aA3FbfE35CedE49AC37f3f881934");
    }

    @Deprecated
    protected DirectPurchase(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DirectPurchase(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DirectPurchase(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DirectPurchase(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Deprecated
    public static DirectPurchase load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DirectPurchase(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DirectPurchase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DirectPurchase(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DirectPurchase load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DirectPurchase(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DirectPurchase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DirectPurchase(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DirectPurchase> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DirectPurchase.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DirectPurchase> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DirectPurchase.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DirectPurchase> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DirectPurchase.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DirectPurchase> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DirectPurchase.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public List<TokenSentEventResponse> getTokenSentEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENSENT_EVENT, transactionReceipt);
        ArrayList<TokenSentEventResponse> responses = new ArrayList<TokenSentEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TokenSentEventResponse typedResponse = new TokenSentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._reference2 = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._reference1 = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TokenSentEventResponse> tokenSentEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TokenSentEventResponse>() {
            @Override
            public TokenSentEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENSENT_EVENT, log);
                TokenSentEventResponse typedResponse = new TokenSentEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._reference2 = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._reference1 = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TokenSentEventResponse> tokenSentEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENSENT_EVENT));
        return tokenSentEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> initialize(String _token) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INITIALIZE,
                Arrays.asList(new org.web3j.abi.datatypes.Address(_token)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> sendTokenAndLog(String to, BigInteger amount, byte[] reference1, byte[] reference2) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDTOKENANDLOG,
                Arrays.asList(new org.web3j.abi.datatypes.Address(to),
                        new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.generated.Bytes32(reference1),
                        new org.web3j.abi.datatypes.generated.Bytes32(reference2)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class TokenSentEventResponse extends BaseEventResponse {
        public String _from;

        public String _to;

        public byte[] _reference2;

        public BigInteger _amount;

        public byte[] _reference1;
    }
}
