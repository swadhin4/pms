package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.jpa.entities.AssetCategory;



public interface AssetCategoryRepo extends JpaRepository<AssetCategory, Long>{

	@Query("from AssetCategory ac order by ac.assetCategoryName")
	public List<AssetCategory> findAssetCategories();

}
