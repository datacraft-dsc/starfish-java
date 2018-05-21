/**
 * call from frontend
 */

package com.oceanprotocol.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.opendevl.JFlat;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.oceanprotocol.model.Assets;
import com.oceanprotocol.service.AssetService;
import com.oceanprotocol.service.dto.CategoryDto;

import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;

@SuppressWarnings("deprecation")
@RestController
public class AssetRegistraion {
	@Value("${localhost}")
	private String localhost;

	@Value("${assetupdownUrl}")
	private String assetupdownUrl;
	@Autowired
	private AssetService assetService;
	@Autowired
	GridFsTemplate gridFsTemplate;

	@Value("${assetfileuploadurl}")
	private String assetfileuploadurl;

	@Value("${inputfile}")
	private String inputfile;

	@Value("${outputfile}")
	private String outputfile;
	@Value("${outputFilecsv}")
	private String outputFileCsv;

	@Value("${showurl}")
	private String showurl;
	JSONObject resultObject = null;
	JSONObject resultObjectfilecategory = null;

	/**
	 * This api used to recieve the multipart file
	 * 
	 * @param uploadfile
	 * @param assetId
	 * @return
	 */
	@SuppressWarnings({ "resource", "unchecked", "unused" })
	@PostMapping("/asset/uploadfile/{assetId}")
	public @ResponseBody ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile uploadfile,
			@PathVariable("assetId") String assetId) {
		System.out.println(uploadfile.getSize());
		Assets assetSave = assetService.findByAssetId(assetId);
		assetSave.setFilesize(uploadfile.getSize());
		Assets asset = assetService.save(assetSave);
		if(asset == null){
			resultObject.put("result", "No asset found");
			resultObject.put("reason", "No asset ");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		
		resultObject = new JSONObject();
		// Used for Checking whether the file is present
		if (uploadfile == null) {
			resultObject.put("result", "UnSuccessful Upload");
			resultObject.put("reason", "No file Choosen");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		String status = assetService.checkFile(uploadfile.getOriginalFilename(), assetId);
		if (status != null) {

			return new ResponseEntity<String>(status, HttpStatus.OK);

		}
		// Used for Parsing MultipartFile to File
		String filename = uploadfile.getOriginalFilename();
		if (uploadfile.getSize() == 0) {
			return new ResponseEntity<String>("selected file is empty!", HttpStatus.OK);
		}
		 File convFile = new File(System.getProperty("user.dir") +"opt/tomcat/webapps/" + filename); //server
//		File convFile = new File(uploadfile.getOriginalFilename());
		try {

			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(uploadfile.getBytes());

			fos.close();

		} catch (IllegalStateException e) {
			resultObject.put("result", "UnSuccessful Upload");
			resultObject.put("reason", "Error Occured During Uploading..Try Again..");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			resultObject.put("result", "UnSuccessful Upload");
			resultObject.put("reason", "Error Occured During File Handling..Try Again..");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		// Upload url
		String targetUrl = localhost + assetfileuploadurl + assetId;
		// Setting up connection with the given url
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(targetUrl);
		// Adding file as entity to the url
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", new FileBody(convFile));
		post.setEntity(entity);
		String[] success = null;
		// Send data to the specified url and save the response
		try {
			HttpResponse response = client.execute(post);
			String resp = response.getStatusLine().toString();

			success = resp.split(" ");
			// Delete the file after the operation from folder
			if (convFile.exists()) {
				convFile.delete();
			}
		} catch (ClientProtocolException e) {
			resultObject.put("result", "UnSuccessful Upload");
			resultObject.put("reason", "Error Occured During NetWork Connection..Try Again..");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			resultObject.put("result", "UnSuccessful Upload");
			resultObject.put("reason", "Error Occured During File Handling..Try Again..");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		// Checks the condition and give appropriate response to the user
		if (success[1].equalsIgnoreCase("200") && !convFile.exists()) {
			DBObject metaData = new BasicDBObject();
			metaData.put("assetId", assetId);

			try {
				gridFsTemplate.store(uploadfile.getInputStream(), uploadfile.getOriginalFilename(), metaData).getId()
						.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			assetService.setAssetStausAndFileName(assetId, 1, convFile.getName());
			resultObject.put("result", "Assets Successfuly Uploaded");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			resultObject.put("result", "Unsuccessful Upload");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * find asset by status(uploaded and downloaded assets
	 * 
	 * @param email
	 * @return assets
	 */
	@GetMapping("/asset/status")
	public ResponseEntity<?> getAssetByStatus(String email) {
		JSONObject assets = assetService.findByEmail(email);

		return new ResponseEntity<JSONObject>(assets, HttpStatus.OK);
	}

	/**
	 * find asset by category and email //myasset by category
	 * 
	 * @param category
	 * @param email
	 * @return
	 */
	@GetMapping("/asset/category")
	public ResponseEntity<?> getAssetByCategoryAndEmail(String category, String email) {
		// find asset by category and email
		List<CategoryDto> assets = assetService.findByCategory(category, email);
		return new ResponseEntity<List<CategoryDto>>(assets, HttpStatus.OK);
	}

	/**
	 * find asset by category //list assets intto the public home page //allassets by category
	 * 
	 * @param category
	 * @return
	 */
	@GetMapping("/asset/findbycategory")
	public ResponseEntity<?> getAssetByCategory(String category) {
		// find asset by category
		List<CategoryDto> assets = assetService.findByCategory(category);

		return new ResponseEntity<List<CategoryDto>>(assets, HttpStatus.OK);
	}

	@GetMapping("/asset/overview")
	public ResponseEntity<?> getAssetOverview(String category, @RequestParam("assetid") String assetId) {
		// find asset by category
		Assets assets = assetService.findByCategoryAndAssetId(category, assetId);

		return new ResponseEntity<Assets>(assets, HttpStatus.OK);
	}

	/**
	 * 
	 * @param category
	 * @param assetId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/asset/download")
	public ResponseEntity<?> getAssetDownload(String category, @RequestParam("assetid") String assetId) {
		// find asset by category
		resultObject = new JSONObject();
		Assets assetfiledownload = assetService.findAsseturl(assetId);
		if (assetfiledownload == null) {
			resultObject.put("result", "Asset not found");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		if(assetfiledownload.getFiles().split("\\.")[1].equals("csv")){
			String csv = showurl + "convertfile/" + assetId + "/" + assetfiledownload.getFiles();
			String json = showurl + "convertfile/" + assetId + "/"+"output.json";
			String xml = showurl + "convertfile/" + assetId + "/output.xml";
			resultObject.put("csv", csv);
			resultObject.put("json", json);
			resultObject.put("xml", xml);
	//		resultObject.put("Files",resultObjectfilecategory);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		}else if(assetfiledownload.getFiles().split("\\.")[1].equals("json")){
			String json = showurl + "convertfile/" + assetId + "/" + assetfiledownload.getFiles();
			String csv = showurl + "convertfile/" + assetId + "/"+"output.csv";
			String xml = showurl + "convertfile/" + assetId + "/output.xml";
			resultObject.put("csv", csv);
			resultObject.put("json", json);
			resultObject.put("xml", xml);
	//		resultObject.put("Files",resultObjectfilecategory);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			
		}else{
			String csv = showurl + "convertfile/" + assetId + "/" + assetfiledownload.getFiles();
			String json = showurl + "convertfile/" + assetId + "/"+"output.json";
			String xml = showurl + "convertfile/" + assetId + "/output.xml";
			resultObject.put("csv", csv);
			resultObject.put("json", json);
			resultObject.put("xml", xml);
	//		resultObject.put("Files",resultObjectfilecategory);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		}
	}

	/**
	 * find asset by sub category
	 * 
	 * @param subCategory
	 * @return
	 */
	@GetMapping("/asset/subcategory")
	public ResponseEntity<?> getAssetBySubCategory(@RequestParam("subcategory") String subCategory) {
		List<Assets> assets = assetService.findByType(subCategory);

		return new ResponseEntity<List<Assets>>(assets, HttpStatus.OK);
	}

	/**
	 * find asset using publisher
	 * 
	 * @param publisherId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/asset/publisher")
	public ResponseEntity<?> getAssetByPublisher(@RequestParam("email") String email) {
		// find asset by publisher id
		List<CategoryDto> assets = assetService.findByuserEmail(email);

		if (assets.size() == 0) {
			resultObject.put("result", "Asset Not found for this publisher");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<CategoryDto>>(assets, HttpStatus.OK);
	}
	



	/**
	 * find asset count of each user
	 * 
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/asset/userassetcount")
	public ResponseEntity<?> getuserAssetCount(@RequestParam("email") String email) {
		JSONObject assetcount = assetService.findCount(email); // find asset
																// count of each
																// user

		if (assetcount.equals(null)) {
			resultObject.put("result", "Asset Not found for this user");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(assetcount, HttpStatus.OK);
	}

	/**
	 * find total asset count of each user
	 * 
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/asset/totalpersonalassetcount")
	public ResponseEntity<?> gettotalAssetCount(@RequestParam("email") String email) {
		JSONObject assetcount = assetService.findCount(email);
		if (assetcount.equals(null)) {
			resultObject.put("result", "Asset Not found");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(assetcount, HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/asset/totalassetcount")
	public ResponseEntity<?> gettotalAssetCount() {
		JSONObject assetcount = assetService.findCount();
		if (assetcount.equals(null)) {
			resultObject.put("result", "Asset Not found");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JSONObject>(assetcount, HttpStatus.OK);
	}
	
	/**
	 * get all assets without email and category
	 */
	@GetMapping("asset/allassets")
	public ResponseEntity<?> getAllAssets() {
		// find all asset from database
		List<Assets> assetsaved = assetService.findAll();
		return new ResponseEntity<List<Assets>>(assetsaved, HttpStatus.OK);

	}


	@SuppressWarnings("unchecked")
	@GetMapping("assets/count")
	public ResponseEntity<?> getCountByCategory(@RequestParam("category") String category) {
		JSONArray dataArray = new JSONArray();
//		List<String> categories = new ArrayList<>();
//		categories.add("healthcare");
//		categories.add("enviornment");
//		categories.add("financial");
//		categories.add("utility");
//		categories.add("mobility");
//		categories.add("buildup environment");
		int upCount = 0, downCount = 0,publiccount=0,privatecount=0,paidcount=0;
	
			List<Assets> assets = assetService.findAllByCategory(category);
			for (Assets asset : assets) {
				if (asset.getStatus() == 1) {
					upCount++; // status 1 = upload
				}else if (asset.getStatus() == 2) {
					downCount++; // status 1 = download
				}
			}
			
			List<Assets> assets1 = assetService.findAllByCategory(category);
			for (Assets asset : assets1) {
				if (asset.getAccess().equals("public")) {
					publiccount++; // status 1 = upload
				}else if (asset.getAccess().equals("private")) {
					privatecount++; // status 1 = upload
				}else if (asset.getAccess().equals("paid")) {
					paidcount++; // status 1 = upload
				}
			}
			
			
			JSONObject data = new JSONObject();
			if (assets.size()>0) {
				data.put("Category Name", category);
				data.put("Upload Count", upCount);
				data.put("Download Count", downCount);
				data.put("TotalCount", assets.size());
				data.put("public",publiccount );
				data.put("private", privatecount);
				data.put("paid", paidcount);
			} else {
				data.put("Category", category);
				data.put(category, "No Entries");
			}
			upCount = 0;
			downCount = 0;
			publiccount = 0;
			privatecount =0;
			paidcount=0;
			dataArray.add(data);
		
		return new ResponseEntity<JSONArray>(dataArray, HttpStatus.OK);
	}
	
	
	/**
	 * It is used to find the category asset cound for signed in user
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("assets/publisher/count")
	public ResponseEntity<?> getCountByCategoryforuser(@RequestParam("email") String email) {
		JSONArray dataArray = new JSONArray();
		List<String> categories = new ArrayList<>();
		categories.add("healthcare");
		categories.add("enviornment");
		categories.add("financial");
		categories.add("utility");
		categories.add("mobility");
		categories.add("buildup environment");
		int upCount = 0, downCount = 0;
		for (String category : categories) {
			List<Assets> assets = assetService.findAllByCategoryAndUserEmail(category,email); //
			System.out.println(assets);
			for (Assets asset : assets) {
				if (asset.getStatus() == 1) {
					upCount++; // status 1 = upload
				} else if (asset.getStatus() == 2) {
					downCount++; // status 1 = download
				}
			}
			JSONObject data = new JSONObject();
			if (assets.size()>0) {
				data.put("Category Name", category);
				data.put("Upload Count", upCount);
				data.put("Download Count", downCount);
				data.put("TotalCount", assets.size());
				data.put("public", 0);
				data.put("private", 0);
				data.put("paid", 0);
			} else {
				data.put("Category", category);
				data.put(category, "No Entries");
			}
			upCount = 0;
			downCount = 0;
			dataArray.add(data);
		}
		return new ResponseEntity<JSONArray>(dataArray, HttpStatus.OK);
	}

	/**
	 * update the asset name,price,description,category,type
	 */
	@SuppressWarnings("unchecked")
	@PutMapping("asset/update")
	public ResponseEntity<?> updateAssets(@RequestBody Assets asset) {
		if (asset == null) {
			resultObject.put("result", "Asset Not found");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		Assets assetsaved = assetService.updateAsset(asset); // save asset to
																// database
		return new ResponseEntity<Assets>(assetsaved, HttpStatus.BAD_REQUEST);

	}

//	@SuppressWarnings({ "unchecked", "unused" })
//	@GetMapping("asset/convert/{assetId}")
//	public ResponseEntity<?> convertCSV(@PathVariable("assetId") String assetId) throws JSONException, ParseException {
//		resultObject = new JSONObject();
//		// Used for parsing mongodb file into Java file format
//		List<GridFSDBFile> gridFsdbFile = gridFsTemplate
//				.find(new Query(Criteria.where("metadata.assetId").is(assetId)));
//		int m = gridFsdbFile.size();
//		if (m == 0) {
//			resultObject.put("Result", "No Files are present");
//			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
//		}
//		String fileName = gridFsdbFile.get(m - 1).getFilename();
//		File filePath = new File(System.getProperty("user.dir") + inputfile);
//		filePath.mkdirs();
//		gridFsdbFile.forEach(file -> {
//			try {
//				file.writeTo(System.getProperty("user.dir") + inputfile + fileName);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		File inputFile = new File(System.getProperty("user.dir") + inputfile + fileName);
//		File outputFile = new File(System.getProperty("user.dir") + outputfile + "output.json");
//		File outputFilecsv = new File(System.getProperty("user.dir") + outputFileCsv + "outputcsv.csv");
//		MappingIterator<Map<?, ?>> mappingIterator = null;
//		List<Map<?, ?>> data = null;
//		ObjectMapper mapper = new ObjectMapper();
//		CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
//		CsvMapper csvMapper = new CsvMapper();
//		// Used for Initializing Arrays to store the X and ECG Values
//		String[] ext = fileName.split("\\.");
//		ArrayList<JSONObject> list = new ArrayList<>();
//		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//		Set<String> keys = Collections.emptySet();
//		if (ext[1].equals("csv")) {
//			// Used to convert CSV file to Json File
//			try {
//				mappingIterator = csvMapper.reader(Map.class).with(csvSchema).readValues(inputFile);
//				data = mappingIterator.readAll();
//				mapper.writeValue(outputFile, data);
//				// Used for Reading the Json File Content
//				JSONParser jsonParser = new JSONParser();
//				JSONArray jSONArray = (JSONArray) jsonParser
//						.parse(new FileReader(System.getProperty("user.dir") + outputfile + "output.json"));
//
//				if (!jSONArray.isEmpty())
//					keys = ((JSONObject) jSONArray.get(0)).keySet();
//				keys.stream().forEach(k -> {
//					jSONArray.stream().forEach(j -> {
//						JSONObject obj = (JSONObject) j;
//						map.add(k, (String) obj.get(k));
//					});
//				});
//				Iterator<String> it = keys.iterator();
//				while (it.hasNext()) {
//					JSONObject jObj = new JSONObject();
//					String k = it.next();
//					jObj.put("name", k);
//
//					jObj.put("value", map.get(k));
//					list.add(jObj);
//				}
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// Used for Validating and generating response accordingly
//			if (map != null) {
//				resultObject.put("Values", list);
//				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
//			} else {
//				resultObject.put("result", "Unsuccessful Conversion");
//				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
//			}
//
//		} else if (ext[1].equals("json")) {
//			try {
//
//				String str = new String(
//						Files.readAllBytes(Paths.get(System.getProperty("user.dir") + inputfile + fileName)));
//				JFlat flatMe = new JFlat(str);
//				flatMe.write2csv(System.getProperty("user.dir") + outputFileCsv + "outputcsv.csv");
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// Used for Validating and generating response accordingly
//			if (outputFilecsv.length() > 0) {
//				resultObject.put("CSV File", outputFilecsv);
//				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
//			} else {
//				resultObject.put("result", "Unsuccessful Conversion");
//				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
//			}
//		}
//		// Used for Deleting the file after the operation from source
//		if (inputFile.exists()) {
//			inputFile.delete();
//		}
//		// if (outputFile.exists()) {
//		// outputFile.delete();
//		// }
//		// if (outputFilecsv.exists()) {
//		// outputFilecsv.delete();
//		// }
//		return null;
//
//	}
	
	
	
	
	@SuppressWarnings({ "unchecked", "unused", "resource" })
	@GetMapping("asset/convert/{assetId}")
	public ResponseEntity<?> convertCSV(@PathVariable("assetId") String assetId)
			throws JSONException, ParseException, IOException {
		resultObject = new JSONObject();
		System.out.println("Inside convert file");
		// Used for parsing mongodb file into Java file format
		List<GridFSDBFile> gridFsdbFile = gridFsTemplate
				.find(new Query(Criteria.where("metadata.assetId").is(assetId)));
		int m = gridFsdbFile.size();
		if (m == 0) {
			resultObject.put("Result", "No Files are present");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		}
		String fileName = gridFsdbFile.get(m - 1).getFilename();
		File filePath = new File(System.getProperty("user.dir") + inputfile+assetId+ "/");
		filePath.mkdirs();
		gridFsdbFile.forEach(file -> {
			try {
				file.writeTo(System.getProperty("user.dir") + inputfile +assetId + "/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		File inputFile = new File(System.getProperty("user.dir") + inputfile + assetId + "/" + fileName);
		File outputFileJson = new File(System.getProperty("user.dir") + outputfile + assetId + "/" + "output.json");
		File outputFilecsv = new File(System.getProperty("user.dir") + outputfile + assetId + "/" + "output.csv");
		File outputFileXML = new File(System.getProperty("user.dir") + outputfile + assetId + "/" + "output.xml");
		MappingIterator<Map<?, ?>> mappingIterator = null;
		List<Map<?, ?>> data = null;
		ObjectMapper mapper = new ObjectMapper();
		CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		// Used for Initializing Arrays to store the X and ECG Values
		String[] ext = fileName.split("\\.");
		ArrayList<JSONObject> list = new ArrayList<>();
		LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		Set<String> keys = Collections.emptySet();
		System.out.println(ext[1]);
		System.out.println("Inside convert file befor if");
		
		if (ext[1].equals("csv")) {
			System.out.println("Inside convert file afetr if");
			// Used to convert CSV file to Json File
			try {
				mappingIterator = csvMapper.reader(Map.class).with(csvSchema).readValues(inputFile);
				data = mappingIterator.readAll();
				mapper.writeValue(outputFileJson, data);
				// Used to convert CSV file to XML File
				StringBuilder sb = new StringBuilder();
				InputStream in = new FileInputStream(outputFileJson);
				Charset encoding = Charset.defaultCharset();

				Reader reader = new InputStreamReader(in, encoding);

				int r = 0;
				while ((r = reader.read()) != -1) {
					char ch = (char) r;
					sb.append(ch);
				}

				in.close();
				reader.close();

				String convStr = sb.toString();
				String root = "root";
				convStr = convStr.replace('[', ' ');
				convStr = convStr.replace(']', ' ');
				convStr = convStr.trim();
				org.json.JSONObject jsonFileObject = new org.json.JSONObject(convStr);
				String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<" + root + ">"
						+ org.json.XML.toString(jsonFileObject) + "</" + root + ">";
				FileWriter ofstream = new FileWriter(outputFileXML);
				try (BufferedWriter out = new BufferedWriter(ofstream)) {
					out.write(xml);
				}
				// Used for Reading the Json File Content
				JSONParser jsonParser = new JSONParser();
				JSONArray jSONArray = (JSONArray) jsonParser
						.parse(new FileReader(System.getProperty("user.dir") + outputfile +assetId + "/" + "output.json"));

				if (!jSONArray.isEmpty())
					keys = ((JSONObject) jSONArray.get(0)).keySet();
				keys.stream().forEach(k -> {
					jSONArray.stream().forEach(j -> {
						JSONObject obj = (JSONObject) j;
						map.add(k, (String) obj.get(k));
					});
				});
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					JSONObject jObj = new JSONObject();
					String k = it.next();
					jObj.put("name", k);

					jObj.put("value", map.get(k));
					list.add(jObj);
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Inside convert file afetr if2");
			// Used for Validating and generating response accordingly
			if (map != null) {
				resultObject.put("CSV File length", Math.rint((double) inputFile.length() / 1024) + " kb");
				resultObject.put("CSV File", inputFile);
				resultObject.put("JSON File", outputFileJson);
				resultObject.put("Values", list);
//				resultObjectfilecategory.put("resultObject", resultObject);
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			} else {
				resultObject.put("result", "Unsuccessful Conversion");
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
			}

		} else if (ext[1].equals("json")) {
			System.out.println("Inside convert file afetr if");
			// Used to convert JSON file to CSV File
			try {
				String str = new String(
						Files.readAllBytes(Paths.get(System.getProperty("user.dir") + inputfile +assetId + "/" + fileName)));
				JFlat flatMe = new JFlat(str);
				flatMe.json2Sheet().write2csv(System.getProperty("user.dir") + outputfile + assetId + "/" +"output.csv");
				// Used to convert JSON file to XML File
				org.json.JSONObject json = new org.json.JSONObject(str);
				String xml = XML.toString(json);
				FileWriter fw = new FileWriter(outputFileXML);
				fw.write(xml);
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Used for Validating and generating response accordingly
			if (outputFilecsv.length() > 0 || outputFileXML.length() > 0) {
				resultObject.put("CSV File", outputFilecsv);
				resultObject.put("XML File", outputFileXML);
				resultObject.put("JSON File", inputFile);
//				resultObjectfilecategory.put("resultObject", resultObject);
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			} else {
				resultObject.put("result", "Unsuccessful Conversion");
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
			}
		} else if (ext[1].equals("xml")) {
			// Used to convert XML file to Json File
			try {
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				String line;
				String sb = "";

				while ((line = br.readLine()) != null) {
					sb += (line.trim());
				}
				org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb);
				String jsonString = xmlJSONObj.toString();
				mapper.writeValue(outputFileJson, jsonString);
				// Used to convert XML file to CSV File
				JFlat flatMe = new JFlat(jsonString);
				flatMe.json2Sheet().write2csv(System.getProperty("user.dir") + outputfile +assetId + "/" + "output.csv");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Used for Validating and generating response accordingly
			if (outputFilecsv.length() > 0 || outputFileJson.length() > 0) {
				resultObject.put("CSV File", outputFilecsv);
				resultObject.put("JSON File", outputFileJson);
				resultObject.put("XML File", inputFile);
//				resultObjectfilecategory.put("resultObject", resultObject);
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			} else {
				resultObject.put("result", "Unsuccessful Conversion");
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
			}
		}
		// Used for Deleting the file after the operation from source
		// if (inputFile.exists()) {
		// inputFile.delete();
		// }
		return null;

	}
	



	@SuppressWarnings("unchecked")
	@PostMapping("assets/updatefile")
	public ResponseEntity<?> updateFile(@RequestParam("fileName") String fileName,
			@RequestParam("headers") String headers) throws IOException {
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName)));
		try {
			System.out.println(gridFsdbFile.getFilename());
			gridFsdbFile.writeTo(System.getProperty("user.dir") + inputfile + gridFsdbFile.getFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File inputFile = new File(System.getProperty("user.dir") + inputfile + gridFsdbFile.getFilename());
		resultObject = new JSONObject();
		String[] ext = gridFsdbFile.getFilename().split("\\.");
		String[] header = headers.split(",");
		File outputFile = new File(System.getProperty("user.dir") + outputfile + "outputcsv.csv");
		if (ext[1].equalsIgnoreCase("csv")) {
			// Read existing file
			CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
			List<String[]> csvBody = reader.readAll();
			// get CSV row column and replace with by using row and column
			for (int i = 0; i < header.length; i++) {
				csvBody.get(0)[i] = header[i];
			}

			reader.close();

			// Write to CSV file which is open
			CSVWriter writer = new CSVWriter(new FileWriter(outputFile), ',');
			writer.writeAll(csvBody);
			writer.flush();
			writer.close();
		}

		if (outputFile.exists()) {
			resultObject.put("CSV File", outputFile);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			resultObject.put("Result", "Unsuccessful");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("assets/headers")
	public ResponseEntity<?> getHeaders(@RequestParam("fileName") String fileName) throws IOException {
		GridFSDBFile gridFsdbFile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName)));
		try {
			System.out.println(gridFsdbFile.getFilename());
			gridFsdbFile.writeTo(System.getProperty("user.dir") + inputfile + gridFsdbFile.getFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File inputFile = new File(System.getProperty("user.dir") + inputfile + gridFsdbFile.getFilename());
		resultObject = new JSONObject();
		File outputFile = new File(System.getProperty("user.dir") + outputfile + "output.json");
		MappingIterator<Map<?, ?>> mappingIterator = null;
		List<Map<?, ?>> data = null;
		ObjectMapper mapper = new ObjectMapper();
		CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		// Used for Initializing Arrays to store the X and ECG Values
		String[] ext = gridFsdbFile.getFilename().split("\\.");
		ArrayList<JSONObject> list = new ArrayList<>();
		List<String> l = new ArrayList<>();
		Set<String> keys = Collections.emptySet();
		Integer count = 0;
		if (ext[1].equalsIgnoreCase("csv")) {
			// Used to convert CSV file to Json File
			try {
				mappingIterator = csvMapper.reader(Map.class).with(csvSchema).readValues(inputFile);
				data = mappingIterator.readAll();
				mapper.writeValue(outputFile, data);
				// Used for Reading the Json File Content
				JSONParser jsonParser = new JSONParser();
				JSONArray jSONArray = (JSONArray) jsonParser
						.parse(new FileReader(System.getProperty("user.dir") + outputfile + "output.json"));

				if (!jSONArray.isEmpty())
					keys = ((JSONObject) jSONArray.get(0)).keySet();
				keys.stream().limit(100).forEach(k -> {
					l.add(k);
				});
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// Used for Validating and generating response accordingly
			if (l != null) {
				resultObject.put("Values", l);
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			} else {
				resultObject.put("result", "Unsuccessful Conversion");
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
			}
		}
		return null;

	}

}
