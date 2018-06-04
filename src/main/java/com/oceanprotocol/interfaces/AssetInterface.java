/**
 * interface used to register asset,get an asset,
 * upload asset and download assets.
 */

package com.oceanprotocol.interfaces;
import java.io.File;
import java.net.URL;

import com.oceanprotocol.model.Asset;

public interface AssetInterface {
	/**
	 * Register an asset to ocean network.
	 * 
	 * @param publisherId - Publisher Id to register asset
	 * @param name - Publisher name to register asset
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Asset assetRegistration(URL url,String publisherId,String name);
	/**
	 * To get asset details from ocean network
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Asset getAsset(URL url,String assetId);
	/**
	 * Update asset meta data
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return
	 */
	Asset updateAsset(URL url,String actorId,String assetName);
	/**
	 * Allow uploading a file for an already registered asset. 
	 * The upload is submitted to the provider.
	 * @param file  - File to be uploaded
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	Asset uploadAsset(URL url,String assetId,File file);
	/**
	 * Allow downloading the asset file from the provider
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	Asset downloadAsset(URL url,String assetId);
	
	/**
	 * Used to disbale the asset
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return 
	 */
	
	Asset disableAsset(URL url,String assetId,String assetName,String actorId);
	
	/**
	 * used to get all assets 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	
	Asset getAssets(URL url,String assetId);
	
	/**
	 * Used to asset provider
	 * @param targetUrl - Ocean network host and port 
	 * @param asset - Asset object with all asset details
	 * @param actorid - Actor Id 
	 * @return
	 */
	Asset addAssetProvider(URL url,String actorid ,String assetId);
	
	/**
	 * This method is used to add the contract details
	 * @param targetUrl  - Ocean network host and port 
	 * @param asset - Asset object with contract details
	 * @return
	 */
	Asset addContract(URL url,String assetId);
	
	/**
	 * This method is used to get the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param contractId -  contract Id
	 * @return
	 */
	
	Asset getContract(URL url,String contractId);
	
	/**
	 * This method is used to sign the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	Asset signContract(URL url,String contractId, String actorId);
	
	/**
	 * This method is used to authorize the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	Asset authorizeContract(URL url,String contractId, String assetId);
	
	/**
	 * This method is used to revoke the authorization
	 * @param targetUrl - Ocean network host and port
	 * @param contractId -  contract Id
	 * @param assetId - asset Id
	 * @return
	 */
	
	Asset revokeContractAuthorization(URL url,Asset asset);
	
	/**
	 * This method is used to access contract asset
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	Asset accessContractAsset(URL url,String contractId);
	
	/**
	 * This method is used to settle contract
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	Asset settleContract(URL url,String actorId,String contractId);
	
	/**
	 * This method is used to add asset listing
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	
	Asset addAssetListing(URL url,String assetId,String publisherId);
		
	
}
