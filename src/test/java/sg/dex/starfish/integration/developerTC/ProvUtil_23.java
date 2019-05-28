package sg.dex.starfish.integration.developerTC;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import sg.dex.starfish.Asset;
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
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.util.ProvUtil;
import sg.dex.starfish.util.JSON;

import java.util.Map;
import java.util.HashMap;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProvUtil_23{

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }


    @Test
    public void testProvA() {
        Map<String,Object> a=ProvUtil.getBaseProvObject();
        try{
            File temp = new File(System.getProperty("java.io.tmpdir")+
                                 System.getProperty("file.separator")+
                                 "publish1.json");
            Writer w= new FileWriter(temp);
            String actId=UUID.randomUUID().toString();
            Map<String,Object> act=ProvUtil.createActivity(actId,ProvUtil.ActivityType.PUBLISH);
            String agentId=UUID.randomUUID().toString();
            Map<String,Object> agent=ProvUtil.createAgent(agentId,ProvUtil.AgentIdType.ACCOUNT);

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

    @Test
    public void testPublishProv() {
        try{
            File temp = new File(System.getProperty("java.io.tmpdir")+
                                 System.getProperty("file.separator")+
                                 "publishprov.json");
            Writer w= new FileWriter(temp);
            String actId=UUID.randomUUID().toString();
            String agentId=UUID.randomUUID().toString();
            String jsonstring=JSON.toPrettyString(ProvUtil.createPublishProvenance(actId,agentId));
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

    public Asset uploadAsset() {
        byte[] data = new byte[]{1, 2, 3};
        Asset asset1 = MemoryAsset.create(data);

        RemoteAsset remoteAsset = (RemoteAsset)remoteAgent.registerAsset(asset1);

        remoteAgent.uploadAsset(asset1);
        return asset1;
    }

    @Test
    public void testInvokeProv() {
        try{
            File temp = new File(System.getProperty("java.io.tmpdir")+
                                 System.getProperty("file.separator")+
                                 "invokeprov.json");
            Writer w= new FileWriter(temp);
            String actId=UUID.randomUUID().toString();
            String agentId=UUID.randomUUID().toString();
            Asset ast = uploadAsset();
            List<Asset> lst=Arrays.asList(ast);
            Map<String,Object> p =new HashMap<String,Object>();
            p.put("did",ast.getAssetID());

            Map<String,Object> p1 =new HashMap<String,Object>();
            p1.put("to-hash",p);

            String params = JSON.toString(p1);
            String jsonstring=
                JSON.toPrettyString(ProvUtil.createInvokeProvenance(actId,agentId, lst,
                                                                                  params,
                                                                                  "hash-val"));
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
