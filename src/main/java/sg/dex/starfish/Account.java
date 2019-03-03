package sg.dex.starfish;

import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.DID;

/**
 * Interface representing an Account in the Ocean ecosystem
 * @author Tom
 *
 */
public interface Account {

	/**
	 * Gets the DID for an Agent
	 *
	 * @return DID The DID that can be used to address this agent in the Ocean Ecosystem
	 */
	public DID getDID();

	/**
	 * Unlocks this Account (prevents any value transfer)
	 *
	 * @throws AuthorizationException if the password is invalid
	 */
	public void unlock();

	/**
	 * Locks this Account (enables value transfer)
	 *
	 */
	public void lock();

	/**
	 * Request some ocean tokens to be transfer to this Account
	 *
	 * @param amount The amount of ocean tokens to transfer
	 * @throws AuthorizationException if account is locekd
	 * @return number of tokens requestd
	 */
	public int requestTokens(int amount);
}
