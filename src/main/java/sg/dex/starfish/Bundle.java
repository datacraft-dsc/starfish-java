package sg.dex.starfish;

import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.DID;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface representing an immutable asset bundle.
 * The sub-asset belongs to the bundle are defined in the asset
 * metadata contents.The contents of the bundle can be other named asset
 *
 * @author Mike
 * @version 0.5
 */
public interface Bundle extends Asset {

    /**
     * Setting the asset type as bundle
     *
     * @return true for bundle asset
     */
    @Override
    default boolean isBundle() {
        return true;
    }

    /**
     * API to add a new sub-asset in existing bundle.
     * It will add the sub-asset passed in the argument and
     * return a new Bundle asset
     *
     * @param name Name of the sub-asset
     * @param asset Asset the need to be added in existing bundle
     * @return An new bundle including the sub asset passed
     */
    public Bundle add(String name, Asset asset);

    /**
     * API to create a new bundle adding all named sub-assets passed as parameters
     *
     * @param assetMap sub Asset map that need to be bundled
     * @return An new bundle including the all thee given sub-assets passed as argument
     */
    public Bundle addAll(Map<String, Asset> assetMap);

    /**
     * API to get an specific asset from an asset Bundle by asset name
     *
     * @param name The name of the sub-asset
     * @return The sub-asset referenced by the given name, or null if not present
     */
    public Asset get(String name);

    /**
     * API to get an immutable all named  sub Asset belong to asset Bundle
     *
     * @return A map of all sub-assets within this bundle
     */
    public Map<String, Object> getAll();

    @Override
    public default DID getAssetDID() {
        throw new UnsupportedOperationException("Unable to obtain DID for asset of class: "+getClass());
    }

    /**
     *API to get the ParamValue
     * @return map of did and AssetID
     */
    @Override
    public default Map<String,Object> getParamValue() {
        Map<String,Object> o=new HashMap<>();
        o.put(Constant.DID, getAssetID());
        return o;
    }

}
