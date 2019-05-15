package sg.dex.starfish.integration.developerTC;

import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.integration.connection_check.AssumingConnection;
import sg.dex.starfish.integration.connection_check.ConnectionChecker;
import sg.dex.starfish.samples.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleIntegrationTests {

    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    private static String registerAssetID = null;


    @Test
    public void aAuthSample() {
        System.out.println("=== aAuthSample ===");

        AuthSample.main();

    }

    @Test
    public void aTestInvokeSample() {
        System.out.println("=== aTestInvokeSample ===");
        InvokeSample.main();
    }

    @Test
    public void bTestIrisSample() {
        System.out.println("=== bTestIrisSample ===");

        IrisSample.main();

    }

    @Test
    public void cTestRegisterSample() {
        System.out.println("=== cTestRegisterSample ===");

        registerAssetID = RegisterSample.test();

    }

    @Test
    public void dTestMetadataSample() {
        System.out.println("=== dTestMetadataSample ===");

        MetadataSample.main(registerAssetID);

    }
}
