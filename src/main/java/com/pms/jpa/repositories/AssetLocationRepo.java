package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.jpa.entities.AssetLocation;



public interface AssetLocationRepo extends JpaRepository<AssetLocation, Long>{

	@Query("from AssetLocation ac order by ac.locationName")
	public List<AssetLocation> findAssetLocations();


}
