package com.web.service;

import java.util.List;

import com.jpa.entities.AssetCategory;
import com.jpa.entities.AssetLocation;
import com.pmsapp.view.vo.AssetVO;
import com.pmsapp.view.vo.LoginUser;


public interface AssetService {

	public List<AssetVO> findAllAsset(LoginUser user) throws Exception;

	public List<AssetVO> findAssetsBySite(Long siteId) throws Exception;

	public AssetVO findAssetById(Long assetid);

	public AssetVO findAssetByModelNumber(String modelNumber) throws Exception; 

	public AssetVO saveOrUpdateAsset(AssetVO assetVO, LoginUser loginUser) throws Exception;

	public List<AssetCategory> getAllAssetCategories() throws Exception;

	public List<AssetLocation> getAllAssetLocations() throws Exception;

}
