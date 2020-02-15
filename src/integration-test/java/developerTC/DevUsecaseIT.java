package developerTC;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        TestResolver_IT.class,
        TestAssetIdentity_IT.class,
        TestAgentIdentity_IT.class,
        TestAgentRegistration_IT.class,
        TestAgentEndpointUpdate_IT.class,
        TestMetaDataAccess_IT.class,
        TestAssetRegistration_IT.class,
        TestUploadAsset_IT.class,
        TestUploadAsset_1_IT.class,
        TestUnPublishListing_IT.class,
        TestSearchAssetListing_IT.class,
        TestViewAssetListing_IT.class,
        TestQueryProvenance_IT.class,
        TestAccessBundleAsset_IT.class,
        TestPurchaseAsset_IT.class,
        TestConfirmPurchase_IT.class,
        TestAssetDownload_IT.class,
        TestInvokeServiceFree_IT.class,
        TestRemoteAgentTest_IT.class,
        TestAddNewAssetVersion_IT.class


})
public class DevUsecaseIT {


}
