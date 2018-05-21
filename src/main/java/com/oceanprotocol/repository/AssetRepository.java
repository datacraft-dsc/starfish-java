package com.oceanprotocol.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oceanprotocol.model.Assets;
import com.oceanprotocol.service.dto.CategoryDto;

public interface AssetRepository extends MongoRepository<Assets, String>{

		public Assets findByAssetId(String assetId); //find asset details using assetid
		public List<Assets> findByStatus(int status);//find list of assets using status
		public List<Assets> findByUserEmail(String email);//find list of assets using user email
		public List<Assets> findByCategory(String category); //find list of assets using category
		public List<Assets> findByCategoryAndType(String category,String type); //find list of assets using category and type

		public Assets findByCategoryAndAssetId(String category,String assetId);//find list of assets using category and assetId
		public List<Assets> findByTypeIn(List<String> type); //find list of assets using type
		public List<Assets> findByCategoryAndUserEmail(String category, String email);//find list of assets using  category and email
		public List<Assets> findByType(String subCategory); //find list of assets using sub category and email
		public List<Assets> findByPublisherId(String publisherId); //find list of assets using publisherId
		Integer countByUserEmail(String email); //find count of asset according to user
		Integer countByStatusAndUserEmail(int status,String email); //find upload count and download count  of assets according to user
		Integer countByStatus(int status); //find total upload count and download count  of assets
		long count();//find total count of assets
		
		public List <Assets> findAllByCategory(String category); 
		public List <Assets> findAllByCategoryAndUserEmail(String category,String email);
	
		public List<Assets> findAll();
		

}
