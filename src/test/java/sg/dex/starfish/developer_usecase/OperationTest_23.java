package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
import sg.dex.starfish.impl.AOperation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.operations.ReverseBytesOperation;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class OperationTest_23 {
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    private RemoteAgent remoteAgent;


    @Before
    public void setUp(){
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        String meta = "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";

        Asset asset = RemoteOperation.create(remoteAgent,meta);

    }
    @Test
    public void testOperation() {

        Operation op=ReverseBytesOperation.create();
        byte data[] ={1,2,3};

        Asset a= MemoryAsset.create(data);

        Job job=op.invoke(a);

        Asset result=job.awaitResult(1000);

        assertArrayEquals(new byte[] {3,2,1}, result.getContent());
    }
//    Operation operation = new AOperation() {
//        @Override
//        public Job invoke(Map<String, Asset> params) {
//            return ;
//        }
//    }

}
