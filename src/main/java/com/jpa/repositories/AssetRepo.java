package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.entities.Asset;



public interface AssetRepo extends JpaRepository<Asset, Long>{

	@Query("from Asset a where a.siteId=:siteId and a.assetCode=:assetCode")
	public List<Asset> findBySiteIdAndCode(@Param(value="siteId") Long siteId, @Param(value="assetCode") String assetCode);

	@Query("from Asset a where a.siteId=:siteId")
	public List<Asset> findBySiteId(@Param(value="siteId") Long siteId);

	@Query("from Asset a where a.categoryId=:categoryId")
	public List<Asset> findByCategoryId(@Param(value="categoryId") Long categoryId);
	
	@Query("from Asset a where a.siteId in :siteId order by a.assetName")
	public List<Asset> findBySiteIdIn(@Param("siteId") List<Long> siteId);

}
