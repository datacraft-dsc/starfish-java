package sg.dex.starfish.integration.squid;

import com.oceanprotocol.squid.api.OceanAPI;
import com.oceanprotocol.squid.exceptions.InitializationException;
import com.oceanprotocol.squid.exceptions.InvalidConfiguration;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.integration.developerTC.AgentService;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SquidHelperTest {

    private static Ocean ocean;
    private static OceanAPI oceanAPI;
    private static Map<String, String> propertiesMap = new HashMap<>();
    private static Properties properties = getProperties();

    static {

        initializeOcean();
        initializeConfigMap();
    }

    private static void initializeConfigMap() {
        for (final String name : properties.stringPropertyNames()) {
            propertiesMap.put(name, properties.getProperty(name));
        }
    }

    private static void initializeOcean() {
        try {
            oceanAPI = OceanAPI.getInstance(properties);
        } catch (InitializationException e) {
            e.printStackTrace();
        } catch (InvalidConfiguration invalidConfiguration) {
            invalidConfiguration.printStackTrace();
        }
        ocean = Ocean.connect(oceanAPI);
    }


    public static Ocean getOcean() {

        return ocean;
    }

    private static Properties getProperties() {
//
        String profile = System.getProperty("profiles.active");
        String propFileName = "application_test.properties";
        if (null != profile && !profile.equals("live")) {
            propFileName = "application_live.properties";
        }

        Properties properties = new Properties();
        try {
            try (InputStream is = AgentService.class.getClassLoader()
                    .getResourceAsStream(propFileName)) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Map<String, String> getPropertiesMap() {
        return propertiesMap;
    }


}
