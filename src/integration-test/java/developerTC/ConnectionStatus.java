package developerTC;

import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ConnectionStatus {

    private static String surferUrl;
    private static Properties properties;

    static {
        properties = initializeConfig();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        surferUrl = (StringUtils.isBlank(port)) ? ip : (ip + ":" + port);
    }

    public static String getSurferUrl() {
        return surferUrl;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static boolean checkAgentStatus() {
        return getAgentStatus(surferUrl);
    }

    private static boolean getAgentStatus(String url) {

        int code = 200;
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();

            code = connection.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            return false;

        }
    }

    private static Properties initializeConfig() {
        Properties properties = new Properties();
        try {
            try (InputStream is = AgentService.class.getClassLoader()
                    .getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
