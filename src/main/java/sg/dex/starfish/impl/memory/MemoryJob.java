package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

public class MemoryJob implements Job {

	private Asset result=null;
	
	@Override
	public boolean isComplete() {
		return result!=null;
	}

	@Override
	public Asset getResult() {
		return result;
	}

}
