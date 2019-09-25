package sg.dex.starfish.impl;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.JSONObjectCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for immutable asset implementations
 * <p>
 * Includes default handing of metadata
 *
 * @author Mike
 * @version 0.5
 */
public abstract class AAsset implements Asset {
    protected final String metadataString;
    protected final String id;

    protected AAsset(String meta) {
        //Utils.validateAssetMetaData(meta);
        this.metadataString = meta;
        this.id = Hash.sha3_256String(metadataString);
    }

    protected AAsset(Map<String, Object> meta) {
        //Utils.validateAssetMetaData(meta);
        this.metadataString = JSON.toString(meta);
        this.id = Hex.toString(Hash.sha3_256(JSON.toString(meta)));
    }

    @Override
    public String toString() {
        return getAssetID();
    }

    @Override
    public String getAssetID() {
        return id;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return JSONObjectCache.parse(metadataString);
    }

    @Override
    public DID getDID() {
        throw new UnsupportedOperationException("Unable to obtain DID for asset of class: " + getClass());
    }

    @Override
    public Map<String, Object> getParamValue() {
        Map<String, Object> o = new HashMap<>();
        // default is to pass the asset ID
        // check if DID is present:
        Object did = getMetadata().get(Constant.DID) != null ? getMetadata().get(Constant.DID) : getDID();
        o.put(Constant.DID, did);
        return o;
    }

    @Override
    public String getMetadataString() {
        return metadataString;
    }

}
