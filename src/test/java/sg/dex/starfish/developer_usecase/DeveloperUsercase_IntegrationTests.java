package sg.dex.starfish.developer_usecase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConnectToOcean_01.class,
        AssetIdentity_02.class,
        AgentIdentity_03.class,
        AgentRegistration_04.class,
        AgentEndpointUpdate_05.class,
        AgentEndpointQuery_06.class,
        MetaDataAccess_07.class,
        AssetRegistration_08.class,
        ConstructServiceAgreemnt_09.class,
        UploadAsset_10.class,
        UploadAsset_11.class,
        UnPublishListing_12.class,
        SearchAssetListing_13.class,
        ViewAssetListing_14.class,
        QueryProvenance_15.class,
        AccessBundleAsset_16.class,
        PurchaseAsset_17.class,
        ConfirmPurchase_18.class,
        AssetDownload_19.class,
        InvokeServiceFree_20.class,
        InvokeServicePaid_21.class,
        AddNewAssetVersion_22.class


})
public class DeveloperUsercase_IntegrationTests {

}
