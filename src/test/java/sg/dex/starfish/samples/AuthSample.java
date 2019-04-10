package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.Surfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static junit.framework.TestCase.assertNotNull;

public class AuthSample {

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = SurferConfig.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    public static void main(String[] arg) {
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");

        // getting the Surfer Instance
        RemoteAgent surfer = Surfer.getSurfer(ip + ":" + port);
        // credential
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", "Aladdin");
        credentialMap.put("password", "6e29fef5d289293d");

        // creating remote Account
        RemoteAccount surferAccount = RemoteAccount.create("9671e2c4dabf1b0ea4f4db909b9df3814ca481e3d110072e0e7d776774a68e0d",
                credentialMap);

        // registering acc to Surfer
        surfer = surfer.connect(surferAccount);

        // get user Details
        Map<String, Object> userDetails = surfer.getUserDetails();

        System.out.println("ID : " + userDetails.get("id"));
        System.out.println("USer Name : " + userDetails.get("username"));
        System.out.println("MetaData : " + userDetails.get("metadata"));
        System.out.println("Status : " + userDetails.get("status"));
        System.out.println("ROLE : " + userDetails.get("roles"));

        //creating new Tokens

        String newToken = surfer.createToken(surferAccount);
        System.out.println("New Token :" + newToken);


        // get ALL Tokens
        String allTokensLst = surfer.getToken();
        assertNotNull(allTokensLst);


        String token = surferAccount.getUserDataMap().get("token").toString();

        // getting all data from account metadata mao
        String id = surferAccount.getUserDataMap().get("id").toString();
        String userName = surferAccount.getUserDataMap().get("username").toString();
        // String id =surferAccount.getCredentials().get("id").toString();
        Object metaData = surferAccount.getUserDataMap().get("metadata");
        String status = surferAccount.getUserDataMap().get("status").toString();

        List<String> role = (List<String>) surferAccount.getUserDataMap().get("roles");

        assertNotNull(id);
        assertNotNull(metaData);
        assertNotNull(status);
        assertNotNull(role);
        assertNotNull(userName);


    }


}

