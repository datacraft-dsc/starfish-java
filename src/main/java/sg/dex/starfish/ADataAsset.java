package sg.dex.starfish;

public abstract class ADataAsset extends AAsset implements DataAsset {

	@Override public boolean isDataAsset() {
		return true;
	}

	public abstract long getSize();
}
