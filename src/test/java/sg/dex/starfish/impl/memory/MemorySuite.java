package sg.dex.starfish.impl.memory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sg.dex.starfish.impl.operations.TestMemoryOperations;

@RunWith(Suite.class)				
@Suite.SuiteClasses({				
	  TestMemoryAgent.class,
	  TestMemoryAsset.class,
	  TestMemoryOperations.class
})		
public class MemorySuite {

}
