package sg.dex.starfish.dexchain;


import java.math.BigInteger;

/**
 * Class that keeps all the configurations to initialize the API
 */
public class DexConfig {

    public static final String KEEPER_URL = "keeper.url";
    public static final String KEEPER_GAS_LIMIT = "keeper.gasLimit";
    public static final String KEEPER_GAS_PRICE = "keeper.gasPrice";
    public static final String KEEPER_TX_ATTEMPTS = "keeper.tx.attempts";
    public static final String KEEPER_TX_SLEEPDURATION = "keeper.tx.sleepDuration";
    public static final String MAIN_ACCOUNT_ADDRESS = "account.main.address";
    public static final String MAIN_ACCOUNT_PASSWORD = "account.main.password";
    public static final String PARITY_ACCOUNT_ADDRESS = "account.parity.address";
    public static final String PARITY_ACCOUNT_PASSWORD = "account.parity.password";
    public static final String MAIN_ACCOUNT_CREDENTIALS_FILE = "account.main.credentialsFile";
    public static final String DID_REGISTRY_ADDRESS = "contract.DIDRegistry.address";
    public static final String PROVENANCE_ADDRESS = "contract.Provenance.address";
    public static final String DIRECT_PURCHASE_ADDRESS = "contract.DirectPurchase.address";
    public static final String TOKEN_ADDRESS = "contract.OceanToken.address";

    private String keeperUrl;
    private BigInteger keeperGasLimit;
    private BigInteger keeperGasPrice;
    private int keeperTxAttempts;
    private long keeperTxSleepDuration;
    private String mainAccountAddress;
    private String parityAccountAddress;
    private String mainAccountPassword;
    private String parityAccountPassword;
    private String mainAccountCredentialsFile;
    private String didRegistryAddress;
    private String provenanceAddress;
    private String directPurchaseAddress;
    private String tokenAddress;

    public String getKeeperUrl() {
        return keeperUrl;
    }

    public DexConfig setKeeperUrl(String keeperUrl) {
        this.keeperUrl = keeperUrl;
        return this;
    }

    public BigInteger getKeeperGasLimit() {
        return keeperGasLimit;
    }

    public DexConfig setKeeperGasLimit(BigInteger keeperGasLimit) {
        this.keeperGasLimit = keeperGasLimit;
        return this;
    }

    public BigInteger getKeeperGasPrice() {
        return keeperGasPrice;
    }

    public DexConfig setKeeperGasPrice(BigInteger keeperGasPrice) {
        this.keeperGasPrice = keeperGasPrice;
        return this;
    }

    public int getKeeperTxAttempts() {
        return keeperTxAttempts;
    }

    public DexConfig setKeeperTxAttempts(int keeperTxAttempts) {
        this.keeperTxAttempts = keeperTxAttempts;
        return this;
    }

    public long getKeeperTxSleepDuration() {
        return keeperTxSleepDuration;
    }

    public DexConfig setKeeperTxSleepDuration(long keeperTxSleepDuration) {
        this.keeperTxSleepDuration = keeperTxSleepDuration;
        return this;
    }

    public String getDidRegistryAddress() {
        return didRegistryAddress;
    }

    public DexConfig setDidRegistryAddress(String address) {
        this.didRegistryAddress = address;
        return this;
    }

    public String getDirectPurchaseAddress() {
        return directPurchaseAddress;
    }

    public DexConfig setDirectPurchaseAddress(String address) {
        this.directPurchaseAddress = address;
        return this;
    }

    public String getProvenanceAddress() {
        return provenanceAddress;
    }

    public DexConfig setProvenanceAddress(String address) {
        this.provenanceAddress = address;
        return this;
    }

    public String getMainAccountAddress() {
        return mainAccountAddress;
    }

    public DexConfig setMainAccountAddress(String mainAccountAddress) {
        this.mainAccountAddress = mainAccountAddress;
        return this;
    }

    public String getParityAccountAddress() {
        return parityAccountAddress;
    }

    public DexConfig setParityAccountAddress(String parityAccountAddress) {
        this.parityAccountAddress = parityAccountAddress;
        return this;
    }

    public String getMainAccountPassword() {
        return mainAccountPassword;
    }

    public DexConfig setMainAccountPassword(String mainAccountPassword) {
        this.mainAccountPassword = mainAccountPassword;
        return this;
    }

    public String getParityAccountPassword() {
        return parityAccountPassword;
    }

    public DexConfig setParityAccountPassword(String parityAccountPassword) {
        this.parityAccountPassword = parityAccountPassword;
        return this;
    }

    public String getMainAccountCredentialsFile() {
        return mainAccountCredentialsFile;
    }

    public DexConfig setMainAccountCredentialsFile(String mainAccountCredentialsFile) {
        this.mainAccountCredentialsFile = mainAccountCredentialsFile;
        if (null == mainAccountCredentialsFile) {
            this.mainAccountCredentialsFile = DexConfigFactory.getDefaultCredential().getAbsolutePath();
        }
        return this;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public DexConfig setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
        return this;
    }
}