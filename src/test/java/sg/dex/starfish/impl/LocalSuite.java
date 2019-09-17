package sg.dex.starfish.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sg.dex.starfish.impl.file.TestFileAsset;

/**
 * Test suite for local resource implementations
 * @author Mike
 *
 */
@RunWith(Suite.class)				
@Suite.SuiteClasses({				
	  TestResources.class,
	  TestFileAsset.class
})		
public class LocalSuite {

}
