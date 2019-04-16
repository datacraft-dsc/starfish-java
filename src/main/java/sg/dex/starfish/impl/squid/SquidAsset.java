package sg.dex.starfish.impl.squid;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanprotocol.squid.exceptions.DDOException;
import com.oceanprotocol.squid.exceptions.DIDFormatException;
import com.oceanprotocol.squid.exceptions.EthereumException;
import com.oceanprotocol.squid.models.DDO;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;

/**
  * Class representing a SquidAsset on the Ocean Network.
  *
  * @author Tom
  *
  */
public class SquidAsset extends AAsset {
	private final Ocean ocean;
	private final DID did;
	private final DDO ddo;

	private SquidAsset(String meta, DID did, DDO ddo, Ocean ocean) {
		super(meta);
		this.did = did;
		this.ddo=ddo;
		this.ocean=ocean;
	}
	
	private static Asset create(String meta, DID did, DDO ddo, Ocean ocean) {
		return new SquidAsset(meta,did,ddo,ocean);
	}

	/**
	 * Gets the DID for this SquidAsset
	 *
	 * @return DID
	 */
	@Override
	public DID getAssetDID() {
		return did;
	}

	@Override
	public boolean isDataAsset() {
		//TODO change if non-data assets need squid support
		return true;
	}

	/**
	 * Returns true if this asset is an operation, i.e. can be invoked on an
	 * appropriate agent
	 *
	 * @return true if this asset is an operation, false otherwise
	 */
	@Override
	public boolean isOperation() {
		return false;
	}

	public static Asset create(Ocean ocean, DID did) {
		com.oceanprotocol.squid.models.DID squidDID;
		try {
			squidDID = com.oceanprotocol.squid.models.DID.builder().setDid(did.toString());
			DDO ddo=ocean.getAssetsAPI().resolve(squidDID);
			
			String metaString=wrapDDOMeta(ddo);
			return create(metaString,did,ddo,ocean);
		}
		catch (DIDFormatException e) {
			throw new Error(e);
		}
		catch (EthereumException e) {
			throw new Error(e);
		}
		catch (DDOException e) {
			throw new Error(e);
		}
	}

	private static String wrapDDOMeta(DDO ddo) {
		HashMap<String,Object> info=new HashMap<>();
		try {
			info.put("ddoString", ddo.toJson());
		}
		catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
		
		HashMap<String,Object> meta=new HashMap<>();
		meta.put("type", "dataset");
		meta.put("additionalInformation", info);
		
		String result=JSON.toPrettyString(meta);
		return result;
	}

}
