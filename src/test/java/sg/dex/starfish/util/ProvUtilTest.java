package sg.dex.starfish.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import java.io.FileOutputStream;
import java.io.File;
import java.util.UUID;

import java.util.List;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Map;
import java.util.HashMap;

public class ProvUtilTest {

    @Test
    public void testProvA() {
        JSONObject a=ProvUtil.getProvObject();
        try{
            File temp = new File(System.getProperty("java.io.tmpdir")+
                                 System.getProperty("file.separator")+
                                 "publish1.json");
            Writer w= new FileWriter(temp);
            String actId=UUID.randomUUID().toString();
            JSONObject act=ProvUtil.createActivity(actId,ProvUtil.ActivityType.PUBLISH);
            String agentId=UUID.randomUUID().toString();
            JSONObject agent=ProvUtil.createAgent(agentId,ProvUtil.AgentIdType.ACCOUNT);

            a.putAll(ProvUtil.createActivities(act));
            a.putAll(ProvUtil.createAgents(agent));
            a.putAll(ProvUtil.associatedWith(agentId,actId));
            a.putAll(ProvUtil.generatedBy("this",actId));

            String jsonstring=JSON.toPrettyString(a);
            try{
                JSON.validateJson(jsonstring);
            }catch (Exception e){
                fail("invalid json ");
            }
            w.write(jsonstring);
            w.flush();
            assertTrue(temp.length()>0);

        }catch(IOException ioe){
            fail("file has length = 0");
        }
    }
}
