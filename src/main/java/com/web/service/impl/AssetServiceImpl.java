package com.web.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jpa.entities.Asset;
import com.jpa.entities.AssetCategory;
import com.jpa.entities.AssetLocation;
import com.jpa.entities.ServiceProvider;
import com.jpa.entities.Site;
import com.jpa.entities.UserSiteAccess;
import com.jpa.repositories.AssetCategoryRepo;
import com.jpa.repositories.AssetLocationRepo;
import com.jpa.repositories.AssetRepo;
import com.jpa.repositories.ServiceProviderRepo;
import com.jpa.repositories.SiteRepo;
import com.jpa.repositories.UserSiteAccessRepo;
import com.pmsapp.view.vo.AssetVO;
import com.pmsapp.view.vo.LoginUser;
import com.web.service.AssetService;



@Service("assetService")
public class AssetServiceImpl implements AssetService{
	private final static Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private SiteRepo siteRepo;

	@Autowired
	private AssetCategoryRepo assetCategoryRepo;

	@Autowired
	private AssetLocationRepo assetLocationRepo;

	@Autowired
	private ServiceProviderRepo serviceProviderRepo;

	@Autowired
	private UserSiteAccessRepo userSiteAccessRepo;

	@Override
	@Transactional
	public List<AssetVO> findAllAsset(LoginUser user) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. findAllAsset");
		LOGGER.info("Getting Asset List for logged in user : "+  user.getFirstName() + "" + user.getLastName());
		List<UserSiteAccess> userSiteAccessList = userSiteAccessRepo.findSiteAssignedFor(user.getUserId());
		List<AssetVO> siteAssetVOList = new ArrayList<AssetVO>();
		if(userSiteAccessList.isEmpty()){
			LOGGER.info("User donot have any access to sites");
		}else{
			LOGGER.info("User is having access to "+userSiteAccessList.size()+" sites");
			List<Long> siteIdList = new ArrayList<Long>();
			for(UserSiteAccess userSiteAccess: userSiteAccessList){
				siteIdList.add(userSiteAccess.getSite().getSiteId());
			}
			LOGGER.info("Getting list of Asset for siteIds : "+siteIdList);

			List<Asset> siteAssetList =  assetRepo.findBySiteIdIn(siteIdList);

			LOGGER.info("Total Assets for user : "+  siteAssetList.size());


			if(!siteAssetList.isEmpty()){
				for(Asset asset : siteAssetList){
					AssetVO assetVO = new AssetVO();
					BeanUtils.copyProperties(asset, assetVO);
					if(asset.getCategoryId()!=null){
						AssetCategory assetCategory = assetCategoryRepo.findOne(asset.getCategoryId());
						assetVO.setCategoryId(asset.getCategoryId());
						assetVO.setAssetCategoryName(assetCategory.getAssetCategoryName());
						assetVO.setAssetType(assetCategory.getAssetType());
					}

					if(asset.getLocationId()!=null){
						AssetLocation assetLocation = assetLocationRepo.findOne(asset.getLocationId());
						assetVO.setLocationId(asset.getLocationId());
						assetVO.setLocationName(assetLocation.getLocationName());
					}

					if(asset.getServiceProviderId()!=null){
						ServiceProvider serviceProvider = serviceProviderRepo.findOne(asset.getServiceProviderId());
						assetVO.setServiceProviderId(asset.getServiceProviderId());
						assetVO.setServiceProviderName(serviceProvider.getName());
					}

					if(asset.getSiteId()!=null){
						Site site = siteRepo.findOne(asset.getSiteId());
						assetVO.setSiteId(site.getSiteId());
						assetVO.setSiteName(site.getSiteName());
					}

					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					if(asset.getDateCommissioned()!=null){
						Date commDate =  asset.getDateCommissioned();
						String comDateStr = formatter.format(commDate);
						assetVO.setCommisionedDate(comDateStr);
					}

					if(asset.getDateDeComissioned()!=null){
						Date deCommDate  = asset.getDateDeComissioned();
						String deComDateStr = formatter.format(deCommDate);
						assetVO.setDeCommissionedDate(deComDateStr);
					}

					if(!StringUtils.isEmpty(asset.getIsAssetElectrical())){
						assetVO.setIsAssetElectrical(asset.getIsAssetElectrical());
					}

					if(!StringUtils.isEmpty(asset.getIsPWSensorAttached())){
						assetVO.setIsPWSensorAttached(asset.getIsPWSensorAttached());
					}

					if(!StringUtils.isEmpty(asset.getPwSensorNumber())){
						assetVO.setPwSensorNumber(asset.getPwSensorNumber());
					}

					siteAssetVOList.add(assetVO);
				}
			}
		}

		LOGGER.info("Exit AssetServiceImpl .. findAllAsset");
		return siteAssetVOList == null?Collections.EMPTY_LIST:siteAssetVOList;
	}

	@Override
	@Transactional	
	public List<AssetVO> findAssetsBySite(Long siteId) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. findAssetsBySite");
		List<Asset> siteAssetList =  assetRepo.findBySiteId(siteId);
		List<AssetVO> siteAssetVOList = new ArrayList<AssetVO>();
		if(!siteAssetList.isEmpty()){
			for(Asset asset : siteAssetList){
				AssetVO assetVO = new AssetVO();
				BeanUtils.copyProperties(asset, assetVO);
				AssetCategory assetCategory = assetCategoryRepo.findOne(asset.getCategoryId());
				assetVO.setAssetCategoryName(assetCategory.getAssetCategoryName());
				assetVO.setAssetType(assetCategory.getAssetType());
				AssetLocation assetLocation = assetLocationRepo.findOne(asset.getLocationId());
				assetVO.setLocationName(assetLocation.getLocationName());

				ServiceProvider serviceProvider = serviceProviderRepo.findOne(asset.getServiceProviderId());
				assetVO.setServiceProviderName(serviceProvider.getName());
				siteAssetVOList.add(assetVO);
			}
		}

		LOGGER.info("Exit AssetServiceImpl .. findAssetsBySite");
		return siteAssetVOList == null?Collections.EMPTY_LIST:siteAssetVOList;
	}

	@Override
	@Transactional	
	public AssetVO findAssetById(Long assetid) {
		LOGGER.info("Inside AssetServiceImpl .. findAssetById");
		Asset savedAsset =  assetRepo.findOne(assetid);
		AssetVO assetVO = new AssetVO();
		BeanUtils.copyProperties(savedAsset, assetVO);
		AssetCategory assetCategory = assetCategoryRepo.findOne(savedAsset.getCategoryId());
		assetVO.setAssetCategoryName(assetCategory.getAssetCategoryName());

		AssetLocation assetLocation = assetLocationRepo.findOne(savedAsset.getLocationId());
		assetVO.setLocationName(assetLocation.getLocationName());

		ServiceProvider serviceProvider = serviceProviderRepo.findOne(savedAsset.getServiceProviderId());
		assetVO.setServiceProviderName(serviceProvider.getName());
		LOGGER.info("Exit AssetServiceImpl .. findAssetById");
		return assetVO;
	}

	@Override
	public AssetVO findAssetByModelNumber(String modelNumber) throws Exception {

		LOGGER.info("Inside AssetServiceImpl .. findAssetByModelNumber");

		LOGGER.info("Exit AssetServiceImpl .. findAssetByModelNumber");
		return null;
	}

	@Override
	@Transactional
	public AssetVO saveOrUpdateAsset(AssetVO assetVO, LoginUser user) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. saveOrUpdateAsset");
		Asset asset = null;
		if(assetVO.getAssetId() == null){
			asset = new Asset();
			BeanUtils.copyProperties(assetVO, asset);
			asset.setCreatedBy(user.getUsername());
		}else{
			asset = assetRepo.findOne(assetVO.getAssetId());
			BeanUtils.copyProperties(assetVO, asset);
			asset.setModifiedBy(user.getUsername());
			asset.setModifiedDate(new Date());
		}


		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if(!StringUtils.isEmpty(assetVO.getCommisionedDate())){
			Date commDate;
			try {
				commDate = formatter.parse(assetVO.getCommisionedDate());
				asset.setDateCommissioned(commDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if(!StringUtils.isEmpty(assetVO.getDeCommissionedDate())){
			Date deCommDate;
			try {
				deCommDate = formatter.parse(assetVO.getDeCommissionedDate());
				asset.setDateDeComissioned(deCommDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(assetVO.getAssetType().equalsIgnoreCase("E")){
			LOGGER.info("Validating  Asset Electrical and Power sensor attached");
			if(!StringUtils.isEmpty(assetVO.getIsAssetElectrical())){
				if(assetVO.getIsAssetElectrical().equalsIgnoreCase("YES")){
					LOGGER.info("Asset is electrical");
					asset.setIsAssetElectrical(assetVO.getIsAssetElectrical());
					if(assetVO.getIsPWSensorAttached().equalsIgnoreCase("YES")){
						LOGGER.info("Asset has power sensor attached");
						asset.setIsPWSensorAttached(assetVO.getIsPWSensorAttached());

						if(!StringUtils.isEmpty(assetVO.getPwSensorNumber())){
							LOGGER.info("Asset has power sensor number");
							asset.setPwSensorNumber(assetVO.getPwSensorNumber());
						}else{
							LOGGER.info("Asset power sensor must not be empty");
							throw new RuntimeException("Asset power sensor should not be empty.");
						}
					}else{
						LOGGER.info("Asset has no power sensor attached");
						asset.setIsPWSensorAttached("NO");
						asset.setPwSensorNumber("");
					}
				}else{
					LOGGER.info("Asset is not electical");
					asset.setIsAssetElectrical("NO");
					asset.setIsPWSensorAttached("NO");
					asset.setPwSensorNumber("");
				}

			}
		}

		asset = assetRepo.save(asset);

		if(asset.getAssetId()!=null){
			BeanUtils.copyProperties(asset, assetVO);
		}
		LOGGER.info("Exit AssetServiceImpl .. saveOrUpdateAsset");
		return assetVO;
	}

	@Override
	public List<AssetCategory> getAllAssetCategories() throws Exception {
		List<AssetCategory> assetCategories = assetCategoryRepo.findAll();
		return assetCategories ==  null?Collections.EMPTY_LIST:assetCategories;
	}

	@Override
	public List<AssetLocation> getAllAssetLocations() throws Exception {
		List<AssetLocation> assetLocations = assetLocationRepo.findAll();
		return assetLocations ==  null?Collections.EMPTY_LIST:assetLocations;
	}





}
