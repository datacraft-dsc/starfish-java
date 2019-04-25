package sg.dex.starfish;

import sg.dex.starfish.util.DID;

import java.util.Map;

/**
 * Interface representing an immutable asset bundle. The contents of the bundle are defined in the asset
 * metadata.The content of the bundle can be other named asset
 *
 * @author Mike
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
     * API to create a new bundle and add  named sub-asset passed as parameter
     *
     * @param name  : name of the asset
     * @param asset : Asset the need to be added
     */
    public Bundle add(String name, Asset asset);

    /**
     * API to create a new bundle and add all named sub-asset passed as parameter
     *
     * @param assetMap
     */
    public Bundle addAll(Map<String, Asset> assetMap);

    /**
     * API to get an specific asset from an asset Bundle
     *
     * @param name
     * @return
     */
    public Asset get(String name);

    /**
     * API to get an immutable all named  sub Asset belong to asset Bundle
     *
     * @return
     */

    public Map<String, Object> getAll();

    @Override
    public default DID getAssetDID() {
        throw new UnsupportedOperationException("Unable to obtain DID for asset of class: "+getClass());
    }



}
