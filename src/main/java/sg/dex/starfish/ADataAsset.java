package sg.dex.starfish;

public abstract class ADataAsset extends AAsset implements DataAsset {

	public ADataAsset(String meta) {
		super(meta);
	}

	@Override public boolean isDataAsset() {
		return true;
	}

	public abstract long getSize();
}
