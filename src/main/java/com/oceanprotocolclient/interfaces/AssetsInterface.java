/**
 * interface used to register asset,get an asset,
 * upload asset and download assets.
 */

package com.oceanprotocolclient.interfaces;

import java.io.File;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.model.Asset;

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
	 * @param targetUrl
	 * @return
	 */
	
	JSONObject getAllAssets(String targetUrl);
	
	
	
}
