package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.web.util.RestResponse;


public interface AssetService {

	public List<AssetVO> findAllAsset(LoginUser user) throws Exception;

	public List<AssetVO> findAssetsBySite(Long siteId) throws Exception;

	public AssetVO findAssetById(Long assetid);

	public AssetVO findAssetByModelNumber(String modelNumber) throws Exception; 

	public RestResponse saveOrUpdateAsset(AssetVO assetVO, LoginUser loginUser) throws Exception;

	public List<AssetCategory> getAllAssetCategories() throws Exception;

	public List<AssetLocation> getAllAssetLocations() throws Exception;

}
