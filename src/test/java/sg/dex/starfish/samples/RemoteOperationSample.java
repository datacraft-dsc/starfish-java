package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RemoteOperationSample {

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = Properties.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] arg) {

        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";

        Asset asset = RemoteOperation.create(surfer,meta);



    }
}
