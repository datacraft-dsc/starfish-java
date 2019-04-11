package sg.dex.starfish.impl.remote;

import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.impl.AAccount;

/**
 * Class representing a RemoteAccount in the Ocean Ecosystem
 *
 * @author Tom
 *
 */
public class RemoteAccount extends AAccount {

	/**
	 * Create an RemoteAccount with the provided ID
	 *
	 * @param id The identifier for this account
	 */
	protected RemoteAccount(String id) {
		super(id);
	}
}
