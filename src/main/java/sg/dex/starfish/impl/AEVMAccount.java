package sg.dex.starfish.impl;

/**
 * Class representing an Ethereum Account in the Ocean Ecosystem
 *
 * @author Tom
 *
 */
public abstract class AEVMAccount extends AAccount {

	protected String password;

	/**
	 * Create an account with the provided ID and password
	 *
	 * @param id The identifier for this account
	 * @param password The Account password
	 */
	protected AEVMAccount(String id, String password) {
		super(id);
		this.password=password;
	}

}
