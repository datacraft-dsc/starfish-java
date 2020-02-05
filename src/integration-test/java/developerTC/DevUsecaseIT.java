package developerTC;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        TestResolver_01.class,
        TestAssetIdentity_02.class,
        TestAgentIdentity_03.class,
        TestAgentRegistration_04.class,
        TestAgentEndpointUpdate_05.class,
        TestMetaDataAccess_07.class,
        TestAssetRegistration_08.class,
        TestUploadAsset_10.class,
        TestUploadAsset_11.class,
        TestUnPublishListing_12.class,
        TestSearchAssetListing_13.class,
        TestViewAssetListing_14.class,
        TestQueryProvenance_15.class,
        TestAccessBundleAsset_16.class,
        TestPurchaseAsset_17.class,
        TestConfirmPurchase_18.class,
        TestAssetDownload_19.class,
        TestInvokeServiceFree_20.class,
        TestInvokeServicePaid_21.class,
        TestRemoteAgentTest_26.class,
        TestAddNewAssetVersion_22.class


})
public class DevUsecaseIT {


}
