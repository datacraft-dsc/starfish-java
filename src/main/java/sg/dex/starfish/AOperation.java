package sg.dex.starfish;

/**
 * Abstract base class representing invokable operation sin th eOCean ecosystem
 * 
 * @author Mike
 *
 */
public abstract class AOperation extends AAsset implements Operation {

	protected AOperation(String meta) {
		super(meta);
	}

	@Override
	public boolean isDataAsset() {
		return false;
	}
	



}
