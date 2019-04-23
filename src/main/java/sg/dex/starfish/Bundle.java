package sg.dex.starfish;

import sg.dex.starfish.util.DID;

import java.util.Map;

/**
 * Interface representing an immutable asset bundle. The contents of the bundle are defined in the asset
 * metadata.
 *
 * @author Mike
 */
public interface Bundle extends Asset {

    /**
     * Setting the asset type as bundle
     *
     * @return
     */
    @Override
    default boolean isBundle() {
        return true;
    }

    /**
     * API to add an asset to a existing Asset bundle
     * TODO: needs to return new Bundle
     *
     * @param name  : name of the asset
     * @param asset : Asset the need to be added
     */
    public void add(String name, Object asset);

    /**
     * API to all map of assests and their respective names.
     * TODO: needs to return new Bundle
     * All asset present in the map will be added to the Asset bundle
     *
     * @param assetMap
     */
    public void addAll(Map<String, Object> assetMap);

    /**
     * API to get an specific asset from an asset Bundle
     *
     * @param name
     * @return
     */
    public Object get(String name);

    /**
     * API to get all Asset belong to asset Bundle
     *
     * @return
     */

    public Map<String, Object> getAll();

    @Override
    public default DID getAssetDID() {
        throw new UnsupportedOperationException("Unable to obtain DID for asset of class: "+getClass());
    }


}
