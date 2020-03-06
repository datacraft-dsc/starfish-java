package developerTC;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
//@Disabled
public class TestOperation_IT {

    private RemoteAgent remoteAgent;

    //input asset output asset
    private String increment_4="b158f208561d7da5d5e8d90b5e6a7b621cbd7a4f1b511554c6cd5d6b3dda3d07";
    //input json output json
    private String increment_1="2e80ae58bb837ec236535f2ef097d185d859222e00a7a910bbd1464a5ed50fec";

    //input asset output json
    private String increment_2="045b5fa81e06a904a5f8ae3bf7cc6c976d20a6caf5dd9acfda2a6dac5f7f7275";
    // input json output Asset
    private String increment_3="618b62eceba2641986fafa0da86167821f29ed947753a0c97a1317ae2d60b5b5";


    @BeforeEach
    void init() throws IOException, URISyntaxException {
        RemoteAccount remoteAccount = RemoteAccount.create("Aladdin", "OpenSesame");
        //remoteAgent = RemoteAgent.connect("http://localhost:3030", remoteAccount);
        remoteAgent = RemoteAgent.connect("http://52.230.82.125:3030", remoteAccount);
    }


    @Test
    public void testIncrement_Async()  {
        Operation remoteOperation = remoteAgent.getAsset(increment_1);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", 16);
        Job job = remoteOperation.invokeAsync(inputMap);
        Map<String, Object> remoteAsset = job.getResult(10000);

        Object resutl= remoteAsset.get("n");
        Assertions.assertEquals(resutl.toString(), "17");
    }
    @Test
    public void testIncrement_sync()  {
        Operation remoteOperation = remoteAgent.getAsset(increment_1);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", 16);
        Map<String, Object> result = remoteOperation.invokeResult(inputMap);
        Assertions.assertEquals(result.get("n").toString(), "17");
    }
    @Test
    public void testIncrement2_Async()  {

        String data = "10";
        Asset asset = MemoryAsset.create(data.getBytes());

        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", remoteDataAsset);

        Operation remoteOperation = remoteAgent.getAsset(increment_2);

        Job job = remoteOperation.invokeAsync(inputMap);
        Map<String, Object> resultMap = job.getResult(10000);

        Assertions.assertEquals(resultMap.get("n").toString(), "11");
    }
    @Test
    public void testIncrement2_sync()  {
        String data = "10";
        Asset asset = MemoryAsset.create(data.getBytes());

        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", remoteDataAsset);

        Operation remoteOperation = remoteAgent.getAsset(increment_2);

        Map<String, Object> result = remoteOperation.invokeResult(inputMap);
        Assertions.assertEquals(result.get("n").toString(), "11");
    }
    @Test
    public void testIncrement4_Async()  {
        String data = "4";
        Asset asset = MemoryAsset.create(data.getBytes());

        //Operation remoteOperation = remoteAgent.getAsset("7db5b843410b1bc54aa958dd1140154d83bfdc430965788228119a2d311a6c65");
        Operation remoteOperation = remoteAgent.getAsset(increment_4);

        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);
        RemoteDataAsset remoteDataAsset1 = remoteAgent.getAsset(remoteDataAsset.getAssetID());

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", remoteDataAsset1);
        Job job = remoteOperation.invokeAsync(inputMap);
        Map<String, Object> remoteAsset = job.getResult(10000);

        Asset assetRes = (Asset) remoteAsset.get("n");
        String result = Utils.stringFromStream(assetRes.getContentStream());
        Assertions.assertEquals(result, "5");


    }

    @Test
    public void testIncrement4_Sync() {
        String data = "4";

        Asset asset = MemoryAsset.create(data.getBytes());
        RemoteDataAsset remoteDataAsset = remoteAgent.uploadAsset(asset);

        Operation remoteOperation = remoteAgent.getAsset(increment_4);

        RemoteDataAsset remoteDataAsset1 = remoteAgent.getAsset(remoteDataAsset.getAssetID());

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", remoteDataAsset1);
        Map<String, Object> remoteAsset = remoteOperation.invokeResult(inputMap);

        Asset assetRes = (Asset) remoteAsset.get("n");
        String result = Utils.stringFromStream(assetRes.getContentStream());
        Assertions.assertEquals(result, "5");


    }

    @Test
    public void testIncrement3_Async()  {
        Operation remoteOperation = remoteAgent.getAsset(increment_3);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", 16);
        Job job = remoteOperation.invokeAsync(inputMap);
        Map<String, Object> remoteAsset = job.getResult(10000);

        Asset assetRes = (Asset) remoteAsset.get("n");
        String result = Utils.stringFromStream(assetRes.getContentStream());
        Assertions.assertEquals(result, "17");
    }
    @Test
    public void testIncrement3_sync()  {
        Operation remoteOperation = remoteAgent.getAsset(increment_3);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("n", 16);
        Map<String, Object> resultMap = remoteOperation.invokeResult(inputMap);
        Asset assetRes = (Asset) resultMap.get("n");
        String result = Utils.stringFromStream(assetRes.getContentStream());
        Assertions.assertEquals(result, "17");
    }

}
