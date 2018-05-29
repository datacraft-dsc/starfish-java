/**
 * interface used to register asset,get an asset,
 * upload asset and download assets.
 */

package com.oceanprotocolclient.interfaces;

import java.io.File;

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
	Asset assetsRegistration(String publisherId,String name,String targetUrl);
	/**
	 * To get asset details from ocean network
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Asset getAnAssests(String targetUrl);
	/**
	 * Update asset meta data
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return
	 */
	ResponseEntity<Object> updateAssets(String targetUrl,Asset asset);
	/**
	 * Allow uploading a file for an already registered asset. 
	 * The upload is submitted to the provider.
	 * @param file  - File to be uploaded
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	JSONObject uploadAssest(File file,String targetUrl);
	/**
	 * Allow downloading the asset file from the provider
	 * 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	JSONObject downloadAssest(String targetUrl);
	
	/**
	 * Used to disbale the asset
	 * @param targetUrl - Ocean network host and port
	 * @param asset - Asset object with updated details
	 * @return 
	 */
	
	ResponseEntity<Object> disableAssets(String targetUrl,Asset asset);
	
	/**
	 * used to get all assets 
	 * @param targetUrl - Ocean network host and port
	 * @return
	 */
	
	JSONObject getAllAssets(String targetUrl);
	
	/**
	 * Used to asset provider
	 * @param targetUrl - Ocean network host and port 
	 * @param asset - Asset object with all asset details
	 * @return
	 */
	JSONObject addAssetProvider(String targetUrl,Asset asset);
	
	/**
	 * This method is used to add the contract details
	 * @param targetUrl  - Ocean network host and port 
	 * @param asset - Asset object with contract details
	 * @return
	 */
	JSONObject addContract(String targetUrl,Asset asset);
	
	/**
	 * This method is used to get the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param user -  User object with contract details
	 * @return
	 */
	
	JSONObject getContract(String targetUrl,Asset asset);
	
	/**
	 * This method is used to sign the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	User signContract(String targetUrl,User user);
	
	/**
	 * This method is used to authorize the contract details
	 * @param targetUrl - Ocean network host and port 
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	Asset authorizeContract(String targetUrl,Asset asset);
	
	/**
	 * This method is used to revoke the authorization
	 * @param targetUrl - Ocean network host and port
	 * @param asset -  Asset object with contract details
	 * @return
	 */
	
	Asset revokeContractAuthorization(String targetUrl,Asset asset);
	
	/**
	 * This method is used to access contract asset
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	Asset accessContractAsset(String targetUrl,Asset asset);
	
	/**
	 * This method is used to settle contract
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	Asset settleContract(String targetUrl,Asset asset);
	
	/**
	 * This method is used to add asset listing
	 * @param targetUrl
	 * @param asset
	 * @return
	 */
	
	Asset addAssetListing(String targetUrl,Asset asset);
		
	
}
