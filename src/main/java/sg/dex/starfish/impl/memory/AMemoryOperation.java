package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;

/**
 * Abstract base class for operations executed in-memory
 * @author Mike
 *
 */
public abstract class AMemoryOperation implements Operation {

	public abstract Job invoke(Asset... params);
}
