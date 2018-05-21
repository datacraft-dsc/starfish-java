package com.oceanprotocol.service.Impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import com.mongodb.gridfs.GridFSDBFile;
import com.oceanprotocol.model.Assets;
import com.oceanprotocol.model.User;
import com.oceanprotocol.repository.AssetRepository;
import com.oceanprotocol.repository.UserRepository;
import com.oceanprotocol.service.AssetService;
import com.oceanprotocol.service.dto.CategoryDto;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	AssetRepository assetRepo;

	@Autowired
	GridFsTemplate gridFsTemplate;
	@Autowired
	UserRepository userRepository;

	@Value("${publishimage}")
	private String publishImage;
	@Value("${encodedata}")
	private String encodedata;
	@Value("${showurl}")
	private String showurl;

	/**
	 * To save an asset. Based on the parameter {@link JSONObject}. All the
	 * relevant details about the asset are collected and stored into an asset
	 * object with the parameter email.
	 * 
	 * @param json
	 * @param email
	 * @return {@link Assets}
	 *
	 */
	@Override
	public Assets save(Assets asset, String email, String imageName) {

		User user = userRepository.findByEmail(email);
		try {
			asset.setDateTime(new Date());
			asset.setUserEmail(email);
			asset.setImageName(imageName);
			asset.setSampleenabled(asset.isSampleenabled());
			asset.setSamples(asset.getSamples());
			if (user.getCountry() != null) {
				asset.setCountry(user.getCountry());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetRepo.save(asset);
	}

	/**
	 * update the status of the asset which will fetch from the db by using
	 * assetId
	 * 
	 * @param assetId
	 * @return
	 *
	 */
	@Override
	public Assets setAssetStaus(String assetId, int status) {

		Assets asset = assetRepo.findByAssetId(assetId);

		if (asset != null) {
			asset.setStatus(status);
		}

		return assetRepo.save(asset);
	}

	/**
	 * find the list of assets based on the status
	 * 
	 * @param status
	 * @return
	 *
	 */
	@Override
	public List<Assets> findByStatus(int status) {
		return assetRepo.findByStatus(status);

	}

	/**
	 * to find the downloaded and uploaded asset list based on the email. Based
	 * on the email assets being fetched and with the help of that list
	 * downloaded or uploaded assets
	 * 
	 * @param email
	 * @return {@link JSONObject}
	 *
	 */

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject findByEmail(String email) {
		List<Assets> assets = assetRepo.findByUserEmail(email); // find the list
																// of assets
																// based on
																// email
		List<Assets> uploadedAsset = new ArrayList<>(); // list of uploaded
														// assets
		List<Assets> downloadedAsset = new ArrayList<>();// list of downloaded
															// assets
		assets.stream().forEach(asset -> {

			if (asset.getStatus() == 1) {
				uploadedAsset.add(asset); // status 1 = upload
			} else if (asset.getStatus() == 2) {
				downloadedAsset.add(asset); // status 1 = download
			}

		});
		// create json object for adding upload and download assets
		JSONObject userAssets = new JSONObject();
		userAssets.put("uploadedAsset", uploadedAsset);
		userAssets.put("downloadedAsset", downloadedAsset);
		// return asset
		return userAssets;
	}

	/**
	 * Fuction used to find the assets according to the category and email
	 * parameter : category - eg.Healthcare ,email
	 * 
	 */
	@Override
	public List<CategoryDto> findByCategory(String category, String email) {
		List<Assets> totalAssets = assetRepo.findByCategoryAndUserEmail(category, email); // List
																							// total
																							// assets
																							// according
																							// to
																							// the
																							// category
																							// and
																							// email
		// return total assets
		return getCategoryDto(totalAssets);
	}

	/**
	 * Fuction used to find the assets according to the category parameter :
	 * category - eg.Healthcare ,email return total assets
	 */
	@Override
	public List<CategoryDto> findByCategory(String category) {
		// List total assets according to the category return total assets
		List<Assets> totalAssets = assetRepo.findByCategory(category);
		return getCategoryDto(totalAssets);
	}

	/**
	 * To return a list of categoryDto which include a list of sub-category
	 * assets and its category name
	 * 
	 * @param totalAssets
	 * @return
	 *
	 */
	public List<CategoryDto> getCategoryDto(List<Assets> totalAssets) {
		// used to stream the list of assets to get the types which not include
		// the null
		// value
		List<String> subCategory = totalAssets.stream().map(Assets::getType).filter(Objects::nonNull).distinct()
				.collect(Collectors.toList());
		List<CategoryDto> categoryDto = new ArrayList<>();

		for (String type : subCategory) {
			List<Assets> categoryAssets = new ArrayList<>();
			// checks the type are equal and if it is equal asset is added to
			// category list
			totalAssets.stream().forEach(asset -> {
				if (asset.getType() != null && asset.getType().equals(type)) {
					categoryAssets.add(asset);
				}
			});
			List<JSONObject> assetsWithImageUrl = getAssetsWithImageUrl(categoryAssets);
			CategoryDto singleCategory = new CategoryDto();
			singleCategory.setName(type);
			singleCategory.setAssets(assetsWithImageUrl);
			categoryDto.add(singleCategory);

		}

		return categoryDto;
	}

	/**
	 * Fuction used to find the assets according to the type eg . parameter
	 * :subCategory - eg .ECG,MRI return total assets
	 */
	@Override
	public List<Assets> findByType(String subCategory) {
		List<Assets> totalAssets = assetRepo.findByType(subCategory);// List
																		// total
																		// assets
																		// according
																		// to
																		// the
																		// subcategory
		// return totalasset
		return totalAssets;
	}

	/**
	 * Fuction used to find the assets according to the publisherId parameter
	 * passed : publisherId return total assets
	 */
	@Override
	public List<CategoryDto> findByuserEmail(String email) {
		List<Assets> totalAssets = assetRepo.findByUserEmail(email);
		// return total assets
		return getCategoryDto(totalAssets);
	}

	/**
	 * Fuction used to find the count of assets according to the email
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject findCount(String email) {
		JSONObject counterror = new JSONObject();
		int totalassetcount = assetRepo.countByUserEmail(email); // find the
																	// count of
																	// assets
																	// according
																	// to the
																	// email
		if (totalassetcount == 0) {
			counterror.put("error", "No value for total asset count");
		}
		int uploadedAssetcount = assetRepo.countByStatusAndUserEmail(1, email); // find
																				// the
																				// count
																				// of
																				// assets
																				// according
																				// to
																				// the
																				// status
		if (uploadedAssetcount == 0) {
			counterror.put("error", "No value for upload asset count");
		}
		int downloadedAssetcount = assetRepo.countByStatusAndUserEmail(2, email);
		if (downloadedAssetcount == 0) {
			counterror.put("error", "No value for download asset count");
		}

		JSONObject userAssetscount = new JSONObject();
		userAssetscount.put("assettotalcount", totalassetcount);
		userAssetscount.put("uploadedAssetcount", uploadedAssetcount);
		userAssetscount.put("downloadedAssetcount", downloadedAssetcount);
		return userAssetscount;
	}

	/**
	 * Fuction used to find total count of assets
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public JSONObject findCount() {
		JSONObject counterror = new JSONObject();
		long totalassetcount = assetRepo.count();
		if (totalassetcount == 0) {
			counterror.put("error", "No value for total asset count");
		}
		int uploadedAssetcount = assetRepo.countByStatus(1);
		if (uploadedAssetcount == 0) {
			counterror.put("error", "No value for upload asset count");
		}
		int downloadedAssetcount = assetRepo.countByStatus(2);
		if (downloadedAssetcount == 0) {
			counterror.put("error", "No value for download asset count");
		}

		JSONObject userAssetscount = new JSONObject();
		userAssetscount.put("assettotalcount", uploadedAssetcount);
		userAssetscount.put("uploadedAssetcount", uploadedAssetcount);
		userAssetscount.put("downloadedAssetcount", downloadedAssetcount);
		return userAssetscount;
	}

	@Override
	public Map<String, String> getEncodedData(GetMethod get, String assetId) throws IOException {
		Map<String, String> list = new HashMap<String, String>();
		List<GridFSDBFile> gridFsdbFile = gridFsTemplate
				.find(new Query(Criteria.where("metadata.assetId").is(assetId)));
		File filePath = new File(System.getProperty("user.dir") + encodedata + assetId);
		System.out.println(filePath.getPath());
		boolean filepathboolean = filePath.mkdirs();
		Assets asset = assetRepo.findByAssetId(assetId);
		try {
			for (int i = 0; i < gridFsdbFile.size(); i++) {
				String fileName = gridFsdbFile.get(i).getFilename();

				if (asset.getFiles().equals(fileName)) {
					InputStream ins = gridFsdbFile.get(i).getInputStream();
					OutputStream os = new FileOutputStream(filePath + "/" + fileName);
					byte[] buffer = new byte[1024];
					int bytesRead;
					// read from ins to buffer
					while ((bytesRead = ins.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
						// String encodedValue =
						// Base64.getEncoder().encodeToString(buffer);
						// list.put(fileName, encodedValue);
					}
					ins.close();
					os.flush();
					os.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String checkFile(String fileName, String assetId) {
		List<Assets> assets = assetRepo.findAll();

		List<String> fileNameList = new ArrayList<>();
		for (Assets asset : assets) {
			if (asset.getFiles() != null) {
				List<String> names = assets.stream().map(Assets::getFiles).filter(Objects::nonNull)
						.collect(Collectors.toList());
				fileNameList.addAll(names);
			}

		}

		if (!fileNameList.equals(null)) {
			for (String name : fileNameList) {

				if (name.equals(fileName)) {
					try {
						JSONObject fileexist = new JSONObject();
						fileexist.put("result", "File name already exist");
						return fileexist.toJSONString();
					} catch (Exception e) {
						e.printStackTrace();

					}

				}
			}
		}

		Assets asset = assetRepo.findByAssetId(assetId);

		if (asset == null) {
			JSONObject fileexist = new JSONObject();
			fileexist.put("result", "Asset not found");
			return fileexist.toJSONString();
		}
		if (asset.getFiles() != null) {
			JSONObject fileexist = new JSONObject();
			fileexist.put("result", "File already exist");
			return fileexist.toJSONString();

		}

		return null;
	}

	/**
	 * update the status of the asset which will fetch from the db by using
	 * assetId
	 * 
	 * @param assetId
	 * @return
	 *
	 */
	@Override
	public Assets setAssetStausAndFileName(String assetId, int status, String file) {

		Assets asset = assetRepo.findByAssetId(assetId);

		if (asset != null) {
			asset.setStatus(status);
			asset.setFiles(file);
		}

		return assetRepo.save(asset);
	}

	@Override
	public Assets findOne(String assetId) {
		return null;
	}

	@Override
	public Assets updateAsset(Assets updatedasset) {
		return null;
	}

	/**
	 * to get all asset with its publish image url.
	 * 
	 * @return
	 * @throws IOException
	 *
	 */
	@Override
	public List<JSONObject> findAllAssetswithImage(String category, String type) throws IOException {

		List<Assets> assets = assetRepo.findByCategoryAndType(category, type);// get
																				// all
																				// asset
																				// which
																				// is
																				// in
																				// that
																				// category
																				// and
																				// type
																				// from
																				// db
		return getAssetsWithImageUrl(assets);
	}

	public List<JSONObject> getAssetsWithImageUrl(List<Assets> assets) {
		List<JSONObject> results = new ArrayList<>();
		List<String> assetId = new ArrayList<>();
		assets.stream().forEach(asset -> {
			assetId.add(asset.getAssetId());
		});

		List<GridFSDBFile> gridFsdbFile = gridFsTemplate
				.find(new Query(Criteria.where("metadata.assetId").in(assetId)));// get
																					// all
																					// files
																					// from
																					// gridfs
																					// using
																					// list
																					// of
																					// asset
																					// id
		assets.stream().forEach(asset -> {
			JSONObject object = new JSONObject();

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			List<String> filePaths = new ArrayList<>();
			int a = 0;
			for (GridFSDBFile dbFile : gridFsdbFile) {

				if (dbFile.getMetaData().get("assetId").equals(asset.getAssetId())) {

					if (dbFile.getFilename().equals(asset.getImageName())) {
						// creates file with a path which is specified in
						// application.properties
						File filePath = new File(System.getProperty("user.dir") + publishImage + asset.getAssetId());
						filePath.mkdirs();
						File file = new File(filePath + "/" + dbFile.getFilename());
						// get all data in the form of stream from a file in
						// grid fs by using assetId
						bis = new BufferedInputStream(dbFile.getInputStream());

						try {

							FileUtils.copyInputStreamToFile(bis, file);
							filePaths.add(file.getPath());

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}

			object.put("asset", asset);
			object.put("url", showurl + "assetimage" + "/" + asset.getAssetId() + "/" + asset.getImageName());
			results.add(object);
		});

		return results;
	}

	@Override
	public Assets findByCategoryAndAssetId(String category, String assetId) {

		return assetRepo.findByCategoryAndAssetId(category, assetId);
	}

	@Override
	public Assets findAsseturl(String assetId) {
		return assetRepo.findByAssetId(assetId);
	}

	@Override
	public List<Assets> findAll() {

		return assetRepo.findAll();
	}

	@Override
	public List<Assets> findAllByCategory(String category) {
		// TODO Auto-generated method stub
		return assetRepo.findAllByCategory(category);
	}

	@Override
	public List<Assets> findAllByCategoryAndUserEmail(String category, String email) {
		// TODO Auto-generated method stub
		return assetRepo.findByCategoryAndUserEmail(category, email);
	}

	@Override
	public Assets findByAssetId(String assetId) {
		// TODO Auto-generated method stub
		return assetRepo.findByAssetId(assetId);
	}

	@Override
	public Assets save(Assets assetSave) {
		// TODO Auto-generated method stub
		return assetRepo.save(assetSave);
	}

}