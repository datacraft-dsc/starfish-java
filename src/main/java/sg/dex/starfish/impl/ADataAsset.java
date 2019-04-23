package sg.dex.starfish.impl;

import sg.dex.starfish.DataAsset;

public abstract class ADataAsset extends AAsset implements DataAsset {

	protected ADataAsset(String meta) {
		super(meta);
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public boolean isOperation() {
		return false;
	}


}
