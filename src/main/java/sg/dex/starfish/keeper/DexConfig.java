package sg.dex.starfish.keeper;

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
    public static final String AQUARIUS_URL = "aquarius.url";
    public static final String SECRETSTORE_URL = "secretstore.url";
    public static final String PROVIDER_ADDRESS = "provider.address";
    public static final String MAIN_ACCOUNT_ADDRESS = "account.main.address";
    public static final String MAIN_ACCOUNT_PASSWORD = "account.main.password";
    public static final String MAIN_ACCOUNT_CREDENTIALS_FILE = "account.main.credentialsFile";
    public static final String DID_REGISTRY_ADDRESS = "contract.DIDRegistry.address";
    public static final String AGREEMENT_STORE_MANAGER_ADDRESS = "contract.AgreementStoreManager.address";
    public static final String CONDITION_STORE_MANAGER_ADDRESS = "contract.ConditionStoreManager.address";
    public static final String LOCKREWARD_CONDITIONS_ADDRESS = "contract.LockRewardCondition.address";
    public static final String ESCROWREWARD_CONDITIONS_ADDRESS = "contract.EscrowReward.address";
    public static final String ESCROW_ACCESS_SS_CONDITIONS_ADDRESS = "contract.EscrowAccessSecretStoreTemplate.address";
    public static final String TEMPLATE_STORE_MANAGER_ADDRESS = "contract.TemplateStoreManager.address";
    public static final String ACCESS_SS_CONDITIONS_ADDRESS = "contract.AccessSecretStoreCondition.address";
    public static final String TOKEN_ADDRESS = "contract.OceanToken.address";
    public static final String DISPENSER_ADDRESS = "contract.Dispenser.address";
    public static final String CONSUME_BASE_PATH = "consume.basePath";


    private String keeperUrl;
    private BigInteger keeperGasLimit;
    private BigInteger keeperGasPrice;
    private int keeperTxAttempts;
    private long keeperTxSleepDuration;
    private String aquariusUrl;
    private String secretStoreUrl;
    private String providerAddress;
    private String mainAccountAddress;
    private String mainAccountPassword;
    private String mainAccountCredentialsFile;
    private String didRegistryAddress;
    private String agreementStoreManagerAddress;
    private String conditionStoreManagerAddress;
    private String escrowRewardAddress;
    private String escrowAccessSecretStoreTemplateAddress;
    private String lockRewardAddress;
    private String accessSsConditionsAddress;
    private String tokenAddress;
    private String templateStoreManagerAddress;
    private String dispenserAddress;
    private String consumeBasePath;

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

    public String getAquariusUrl() {
        return aquariusUrl;
    }

    public DexConfig setAquariusUrl(String address) {
        this.aquariusUrl = address;
        return this;
    }

    public String getSecretStoreUrl() {
        return secretStoreUrl;
    }

    public DexConfig setSecretStoreUrl(String secretStoreUrl) {
        this.secretStoreUrl = secretStoreUrl;
        return this;
    }

    public String getDidRegistryAddress() {
        return didRegistryAddress;
    }

    public DexConfig setDidRegistryAddress(String address) {
        this.didRegistryAddress = address;
        return this;
    }

    public String getEscrowRewardConditionsAddress() {
        return escrowRewardAddress;
    }

    public DexConfig setEscrowRewardConditionsAddress(String address) {
        this.escrowRewardAddress = address;
        return this;
    }

    public String getAgreementStoreManagerAddress() {
        return agreementStoreManagerAddress;
    }

    public DexConfig setAgreementStoreManagerAddress(String address) {
        this.agreementStoreManagerAddress = address;
        return this;
    }

    public String getConditionStoreManagerAddress() {
        return conditionStoreManagerAddress;
    }

    public DexConfig setConditionStoreManagerAddress(String address) {
        this.conditionStoreManagerAddress = address;
        return this;
    }

    public String getLockrewardConditionsAddress() {
        return lockRewardAddress;
    }

    public DexConfig setLockrewardConditionsAddress(String address) {
        this.lockRewardAddress = address;
        return this;
    }

    public String getAccessSsConditionsAddress() {
        return accessSsConditionsAddress;
    }

    public DexConfig setAccessSsConditionsAddress(String address) {
        this.accessSsConditionsAddress = address;
        return this;
    }

    public String getConsumeBasePath() {
        return consumeBasePath;
    }

    public DexConfig setConsumeBasePath(String consumeBasePath) {
        this.consumeBasePath = consumeBasePath;
        return this;
    }

    public String getMainAccountAddress() {
        return mainAccountAddress;
    }

    public DexConfig setMainAccountAddress(String mainAccountAddress) {
        this.mainAccountAddress = mainAccountAddress;
        return this;
    }

    public String getMainAccountPassword() {
        return mainAccountPassword;
    }

    public DexConfig setMainAccountPassword(String mainAccountPassword) {
        this.mainAccountPassword = mainAccountPassword;
        return this;
    }

    public String getMainAccountCredentialsFile() {
        return mainAccountCredentialsFile;
    }

    public DexConfig setMainAccountCredentialsFile(String mainAccountCredentialsFile) {
        this.mainAccountCredentialsFile = mainAccountCredentialsFile;
        return this;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public DexConfig setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
        return this;
    }

    public String getTemplateStoreManagerAddress() {
        return templateStoreManagerAddress;
    }

    public DexConfig setTemplateStoreManagerAddress(String templateStoreManagerAddress) {
        this.templateStoreManagerAddress = templateStoreManagerAddress;
        return this;
    }


    public String getDispenserAddress() {
        return dispenserAddress;
    }

    public DexConfig setDispenserAddress(String dispenserAddress) {
        this.dispenserAddress = dispenserAddress;
        return this;
    }

    public String getEscrowAccessSecretStoreTemplateAddress() {
        return escrowAccessSecretStoreTemplateAddress;
    }

    public void setEscrowAccessSecretStoreTemplateAddress(String escrowAccessSecretStoreTemplateAddress) {
        this.escrowAccessSecretStoreTemplateAddress = escrowAccessSecretStoreTemplateAddress;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }
}
