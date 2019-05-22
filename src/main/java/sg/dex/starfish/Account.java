package sg.dex.starfish;

import java.util.Map;

/**
 * Interface representing an Account with one or more agents in the Ocean ecosystem
 * 
 * @author Tom
 * @version 0.5
 *
 */
public interface Account {
	/**
	 * Gets the ID for an Account.
	 * 
	 * The account identifier used is defined by the agent implementation, however typically
	 * this would be a unique user name or ID for this agent.
	 *
	 * @return Account identifier
	 */
	public String getID();
	
	/**
	 * Gets the credentials stored for this Account.
	 * 
	 * Required credentials are defined by the agent implementation, but would typically include
	 * things like user name, password etc.
	 * 
	 * This map is immutable and can be used by Agent to get the credential
	 * 
	 * @return A credential map for this account
	 */
	public Map<String,Object> getCredentials();
}
