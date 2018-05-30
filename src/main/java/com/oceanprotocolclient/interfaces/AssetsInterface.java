/**
 * interface used to register asset,get an asset,
 * upload asset and download assets.
 */

package com.oceanprotocolclient.interfaces;

import java.io.File;
import java.net.URL;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.model.Asset;
import com.oceanprotocolclient.model.User;

public interface AssetsInterface {
	/**
	 * Register an asset to ocean network.
	 * 
	 * @param publisherId - Publisher Id to register asset
	 * @param name - Publisher name to register asset
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Asset assetsRegistration(URL url,String publisherId,String name);
	/**
	 * To get asset details from ocean network
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Asset getAnAsset(URL url);
	/**
	 * Update asset meta data
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return
	 */
	ResponseEntity<Object> updateAssets(URL url,Asset asset);
	/**
	 * Allow uploading a file for an already registered asset. 
	 * The upload is submitted to the provider.
	 * @param file  - File to be uploaded
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	JSONObject uploadAssest(URL url,File file);
	/**
	 * Allow downloading the asset file from the provider
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	JSONObject downloadAsset(URL url);
	
	/**
	 * Used to disbale the asset
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return 
	 */
	
	ResponseEntity<Object> disableAssets(URL url,Asset asset);
	
	/**
	 * used to get all assets 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	
	JSONObject getAllAssets(URL url);
	
	/**
	 * Used to asset provider
	 * @param targetUrl - Ocean network host and port 
	 * @param asset - Asset object with all asset details
	 * @param actorid - Actor Id 
	 * @return
	 */
	JSONObject addAssetProvider(URL url,String actorid ,Asset asset);
	
	/**
	 * This method is used to add the contract details
	 * @param targetUrl  - Ocean network host and port 
	 * @param asset - Asset object with contract details
	 * @return
	 */
	JSONObject addContract(URL url,Asset asset);
	
	/**
	 * This method is used to get the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param contractId -  contract Id
	 * @return
	 */
	
	JSONObject getContract(URL url,String contractId);
	
	/**
	 * This method is used to sign the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	ResponseEntity<?> signContract(URL url,String contractId, String signingActor);
	
	/**
	 * This method is used to authorize the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	ResponseEntity<?> authorizeContract(URL url,String contractId, String assetId);
	
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
	JSONObject accessContractAsset(URL url,String contractId);
	
	/**
	 * This method is used to settle contract
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	ResponseEntity<?> settleContract(URL url,String actorId);
	
	/**
	 * This method is used to add asset listing
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	
	JSONObject addAssetListing(URL url,String assetId,String publisherId);
		
	
}
