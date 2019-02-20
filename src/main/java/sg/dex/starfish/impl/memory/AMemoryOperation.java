package sg.dex.starfish.impl.memory;

import sg.dex.starfish.AOperation;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

/**
 * Abstract base class for operations executed in-memory
 * @author Mike
 *
 */
public abstract class AMemoryOperation extends AOperation {

	protected AMemoryOperation(String meta) {
		super(meta);
	}

	@Override
	public abstract Job invoke(Asset... params);
}
