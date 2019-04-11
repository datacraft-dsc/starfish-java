package sg.dex.starfish.impl;

import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.Account;


/**
 * Class representing an Account in the Ocean Ecosystem
 *
 * @author Tom
 *
 */
public abstract class AAccount implements Account {

	protected String id;
	private Map<String, Object> credentials;

	/**
	 * Create an AAccount with the provided ID
	 *
	 * @param id The identifier for this account
	 */
	protected AAccount(String id) {
		this.id=id;
		this.credentials = new HashMap<String,Object>();
	}

	/**
	 * Gets the ID for an Account
	 *
	 * @return Account identifier
	 */
	@Override
	public String getID() {
		return id;
	}

	/**
	 * Gets the credentials stored for this Account.
	 *
	 * Required credentials are defined by the agent implementation, but would typically include
	 * things like user name, password etc.
	 *
	 * @return
	 */
	public Map<String,Object> getCredentials() {
		return this.credentials;
	}

	public void setCredential(String key, Object value) {
		credentials.put(key, value);
	}

	public String toString() {
		final StringBuilder tmp = new StringBuilder();
		tmp.append(this.getClass().getName());
		tmp.append(":");
		tmp.append(this.id);
		tmp.append("{");
		for (Map.Entry<String, Object> entry : credentials.entrySet()) {
			tmp.append(entry.getKey());
			tmp.append("=");
			tmp.append(entry.getValue().toString());
			tmp.append(",");
		}
		tmp.append("}");
		return tmp.toString();
	}
}
