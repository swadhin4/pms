package com.web.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.exception.RequiredFieldException;
import com.app.exception.Validator;
import com.jpa.entities.Area;
import com.jpa.entities.Cluster;
import com.jpa.entities.Company;
import com.jpa.entities.District;
import com.jpa.entities.Site;
import com.jpa.entities.SiteDeliveryOperation;
import com.jpa.entities.SiteLicence;
import com.jpa.entities.SiteSalesOperation;
import com.jpa.entities.SiteSubMeter;
import com.jpa.entities.User;
import com.jpa.entities.UserSiteAccess;
import com.jpa.repositories.AreaRepo;
import com.jpa.repositories.ClusterRepo;
import com.jpa.repositories.CompanyRepo;
import com.jpa.repositories.CountryRepo;
import com.jpa.repositories.DistrictRepo;
import com.jpa.repositories.LicenseRepo;
import com.jpa.repositories.SiteDeliveryRepo;
import com.jpa.repositories.SiteRepo;
import com.jpa.repositories.SiteSalesOperationRepo;
import com.jpa.repositories.SiteSubmeterRepo;
import com.jpa.repositories.UserDAO;
import com.jpa.repositories.UserSiteAccessRepo;
import com.pmsapp.view.vo.CreateSiteVO;
import com.pmsapp.view.vo.LoginUser;
import com.pmsapp.view.vo.SiteDeliveryVO;
import com.pmsapp.view.vo.SiteLicenceVO;
import com.pmsapp.view.vo.SiteOperationVO;
import com.pmsapp.view.vo.SiteSubmeterVO;
import com.pmsapp.view.vo.SiteVO;
import com.web.service.SiteService;

import sun.misc.BASE64Decoder;



@Service("siteService")
public class SiteServiceImpl implements SiteService{

	private final static Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

	@Autowired
	private SiteRepo siteRepo;

	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private LicenseRepo licenseRepo;

	@Autowired
	private ClusterRepo clusterRepo;

	@Autowired
	private DistrictRepo districtRepo;

	@Autowired
	private AreaRepo areaRepo;

	@Autowired
	private SiteDeliveryRepo siteDeliveryRepo;

	@Autowired
	private SiteSalesOperationRepo siteSalesOperationRepo;

	@Autowired
	private SiteSubmeterRepo siteSubMeterRepo;

	@Autowired
	private UserSiteAccessRepo userSiteAccessRepo;

	@Autowired
	private UserDAO userDAO;

	@Override
	@Transactional
	public List<CreateSiteVO> getSiteList(LoginUser user) throws Exception {
		Set<Site> siteList= null ;
		List<Cluster> clusterList = clusterRepo.findAll();
		List<District> districtList = districtRepo.findAll();
		List<Area> areaList = areaRepo.findAll();
		List<CreateSiteVO> siteVOList=new ArrayList<CreateSiteVO>();
		List<UserSiteAccess> userSiteAccessList = userSiteAccessRepo.findSiteAssignedFor(user.getUserId());

		LOGGER.info("Site List for User : " + userSiteAccessList.size());
		if(userSiteAccessList!=null && !userSiteAccessList.isEmpty()){
			siteList = new HashSet<Site>( userSiteAccessList.size());

			for(UserSiteAccess userSite : userSiteAccessList){
				Site site = siteRepo.findOne(userSite.getSite().getSiteId());
				site.getSiteLicences().clear();
				List<SiteLicence> licenseList = licenseRepo.findBySiteSiteId(site.getSiteId());
				site.setSiteLicences(licenseList);


				List<SiteSalesOperation> salesOpsList = siteSalesOperationRepo.findBySiteSiteId(site.getSiteId());
				site.setSiteSalesOpetaionTimes(salesOpsList);

				List<SiteDeliveryOperation> deliveryOpsList = siteDeliveryRepo.findBySiteSiteId(site.getSiteId());
				site.setSiteDeliveryOpetaionTimes(deliveryOpsList);


				List<SiteSubMeter> subMeterList = siteSubMeterRepo.findBySiteSiteId(site.getSiteId());
				site.setSiteSubmeterList(subMeterList);
				siteList.add(site);

			}
		}


		if(siteList!=null &&  !siteList.isEmpty()){
			List<String> fullAddress = new ArrayList<String>();
			for(Site site:siteList){
				CreateSiteVO siteVO=new CreateSiteVO();
				List<SiteLicenceVO> siteLicensesVO =  siteVO.getSiteLicense();
				siteVO.setSiteId(site.getSiteId());
				siteVO.setSiteName(site.getSiteName());
				siteVO.setSiteAddress1(site.getSiteAddress1());
				siteVO.setSiteAddress2(site.getSiteAddress2());
				siteVO.setSiteAddress3(site.getSiteAddress3());
				siteVO.setSiteAddress4(site.getSiteAddress4());
				siteVO.setZipCode(site.getPostCode());
				
				if(!StringUtils.isEmpty(siteVO.getSiteAddress1())){
					fullAddress.add(siteVO.getSiteAddress1());
				}
				if(!StringUtils.isEmpty(siteVO.getSiteAddress2())){
					fullAddress.add(siteVO.getSiteAddress2());
				}
				if(!StringUtils.isEmpty(siteVO.getSiteAddress3())){
					fullAddress.add(siteVO.getSiteAddress3());
				}
				if(!StringUtils.isEmpty(siteVO.getSiteAddress4())){
					fullAddress.add(siteVO.getSiteAddress4());
				}
				if(!StringUtils.isEmpty(siteVO.getZipCode())){
					fullAddress.add(siteVO.getZipCode());
				}
				
				String finalAddress = org.apache.commons.lang3.StringUtils.join(fullAddress,",");
				siteVO.setFullAddress(finalAddress);
				
				siteVO.setOperator(site.getOperator()); 
				
				siteVO.setOwner(site.getSiteOwner());


				if(site.getClusterId() !=null){
					for(Cluster cluster : clusterList){
						if(site.getClusterId().equals(cluster.getClusterID())){
							siteVO.setCluster(cluster);
							break;
						}
					}
				}
				if(site.getDistrictId() !=null){
					for(District district : districtList){
						if(site.getDistrictId().equals(district.getDistrictId())){
							siteVO.setDistrict(district);
							break;
						}
					}
				}
				if(site.getAreaId() !=null){
					for(Area area : areaList){
						if(site.getAreaId().equals(area.getAreaId())){
							siteVO.setArea(area);
							break;
						}
					}
				}

				siteVO.setContactName(site.getAreaManagerName());
				siteVO.setEmail(site.getEmail());
				siteVO.setElectricityId(site.getElectricIdNo());
				if(site.getLatitude()==null && site.getLatitude() == null){

				}else{
					siteVO.setLatitude(site.getLatitude().toString());
					siteVO.setLongitude(site.getLongitude().toString());
				}

				if(site.getSiteNumberOne() ==  null ) {

				}else{
					siteVO.setSiteNumber1(site.getSiteNumberOne().toString());
				}

				if(site.getSiteNumberTwo()==null ) {

				}else{
					siteVO.setSiteNumber2(site.getSiteNumberTwo().toString());
				}


				if(StringUtils.isEmpty(site.getPrimaryContact())){

				}
				else{
					siteVO.setPrimaryContact(site.getPrimaryContact().toString());
				}
				if(StringUtils.isEmpty(site.getSecondaryContact())){

				}else{
					siteVO.setSecondaryContact(site.getSecondaryContact().toString());
				}


				if(!site.getSiteLicences().isEmpty()){
					for(SiteLicence siteLicence : site.getSiteLicences()){
						SiteLicenceVO siteLicenceVO = new SiteLicenceVO();
						siteLicenceVO.setLicenseId(siteLicence.getLicenseId());
						siteLicenceVO.setLicenseName(siteLicence.getLicenseName());

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						if(siteLicence.getStartDate()!=null && siteLicence.getEndDate()!=null){
							Date licenseValidFrom =  siteLicence.getStartDate();
							Date licenseValidTo  = siteLicence.getEndDate();
							String startDate = formatter.format(licenseValidFrom);
							String endDate = formatter.format(licenseValidTo);
							siteLicenceVO.setValidfrom(startDate);
							siteLicenceVO.setValidto(endDate);
							siteLicensesVO.add(siteLicenceVO);
						}
						siteVO.setSiteLicense(siteLicensesVO);

					}
				}
				//siteVO.getSiteOperation().clear();
				for(SiteSalesOperation siteSalesOperation : site.getSiteSalesOpetaionTimes()){
					SiteOperationVO siteOperationVO = new SiteOperationVO();
					siteOperationVO.setOpId(siteSalesOperation.getSaledsOpId());
					siteOperationVO.setDays(siteSalesOperation.getDayOfWeek());
					siteOperationVO.setFrom(siteSalesOperation.getOpStartTime());
					siteOperationVO.setTo(siteSalesOperation.getOpCloseTime());
					siteVO.getSiteOperation().add(siteOperationVO);
				}

				//siteVO.getSiteDelivery().clear();
				for(SiteDeliveryOperation siteDeliveryOperation : site.getSiteDeliveryOpetaionTimes()){
					SiteDeliveryVO siteDeliveryVO = new SiteDeliveryVO();
					siteDeliveryVO.setOpId(siteDeliveryOperation.getDeliveryOpId());
					siteDeliveryVO.setDays(siteDeliveryOperation.getDayOfWeek());
					siteDeliveryVO.setFrom(siteDeliveryOperation.getOpStartTime());
					siteDeliveryVO.setTo(siteDeliveryOperation.getOpCloseTime());
					siteVO.getSiteDelivery().add(siteDeliveryVO);
				}
				if(!site.getSiteSubmeterList().isEmpty()){
					for(SiteSubMeter siteSubmeter: site.getSiteSubmeterList()){
						SiteSubmeterVO siteSubmeterVO = new SiteSubmeterVO();
						siteSubmeterVO.setSubMeterId(siteSubmeter.getSubMeterId());
						siteSubmeterVO.setSubMeterNumber(siteSubmeter.getSubMeterNumber());
						siteSubmeterVO.setSubMeterUser(siteSubmeter.getSubMeterUser());
						siteVO.getSiteSubmeter().add(siteSubmeterVO);
					}
				}
				siteVOList.add(siteVO);
				fullAddress.clear();
			}
		}
		return siteVOList == null?Collections.EMPTY_LIST:siteVOList;
	}

	@Deprecated
	public SiteVO saveOrUpdateFromRest(final SiteVO siteVO) throws Exception {
		LOGGER.info("Inside SiteServiceImpl - saveOrUpdate");
		Site savedSite = null;
		if(!StringUtils.isEmpty(siteVO.getSiteName()) && siteVO.getSiteId()==null){
			Site site = new Site();
			BeanUtils.copyProperties(siteVO, site);
			LOGGER.info("site : " + site);
			site.setCreatedDate(new Date());
			savedSite= siteRepo.save(site);
			BeanUtils.copyProperties(savedSite, siteVO);
		}
		LOGGER.info("Exit SiteServiceImpl - saveOrUpdate");
		return siteVO;
	}

	@Override
	@Transactional
	public CreateSiteVO saveOrUpdate(final CreateSiteVO siteVO, LoginUser user) throws Exception {
		LOGGER.info("Inside SiteServiceImpl - saveOrUpdate");
		Site savedSite = null;

		CreateSiteVO savedSiteVO = new CreateSiteVO();
		savedSiteVO = validateObject(savedSite, siteVO);

		if(savedSiteVO.getStatus() == 200){
			if(siteVO.getSiteId() == null){
				savedSite = new Site();
			}else{
				savedSite = siteRepo.findOne(siteVO.getSiteId());
			}

			siteVO.setCreatedBy(user.getEmail());
			try{
				savedSite=populateSitePrimaryData(savedSite,siteVO);
				savedSite=populateSiteContact(savedSite,siteVO);
				savedSite=populateLicenseDetails(savedSite,siteVO);
				savedSite=populateOperationDetails(savedSite,siteVO);
				Company company = user.getCompany();
				savedSite.setOperator(company);
				LOGGER.info("Getting Sales and Operations timings");
				if(savedSite.getSiteSalesOpetaionTimes().size()==7 && savedSite.getSiteDeliveryOpetaionTimes().size()==7) {
					savedSite=populateSubmeterDetails(savedSite,siteVO);
					savedSite.setCreatedBy(siteVO.getCreatedBy());
					savedSite = siteRepo.save(savedSite);
					if(savedSite.getSiteId() != null ){
						LOGGER.info("Site information saved successfully");
						savedSiteVO.setSiteId(savedSite.getSiteId());
						savedSiteVO.setSiteName(savedSite.getSiteName());
						LOGGER.info("Saving User and Site data to User site Access table");
						UserSiteAccess userSiteAccess = userSiteAccessRepo.findAccessDetails(user.getUserId(), savedSite.getSiteId());
						if(userSiteAccess==null){
							userSiteAccess = new UserSiteAccess();
							User siteUser = userDAO.findOne(user.getUserId());
							userSiteAccess.setSite(savedSite);
							userSiteAccess.setUser(siteUser);
							userSiteAccess = userSiteAccessRepo.save(userSiteAccess);
							if(userSiteAccess.getAccessId()!=null){
								LOGGER.info("Site mapped to user successfully");
								savedSiteVO.setStatus(201);
								/*if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getFileInput()) && org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getFileExtension())){
									uploadSiteImage(savedSiteVO, siteVO.getFileInput(), siteVO.getFileExtension());
								}*/
							}
						}else{
							LOGGER.info("Site ID already mapped to user in  User site Access table");
							savedSiteVO.setStatus(202);
						}
					}

				}else{
					LOGGER.info("Site Operation details not entered");
				}
			}
			catch(Exception e){
				LOGGER.info("Exception while populating site information :", e);
				savedSiteVO.setValidationMessage("Exception occured. Please verify all the tab information.");
			}


		}

		LOGGER.info("Exit SiteServiceImpl - saveOrUpdate");
		return savedSiteVO;
	}


	private CreateSiteVO uploadSiteImage(CreateSiteVO savedSiteVO, String fileInput, String fileExtension) {
		LOGGER.info("Inside SiteServiceImpl .. uploadSiteImage");
		String base64Image = fileInput.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage image = null;
		String uploadLocation = "D:\\TechM\\SM351137\\GIT\\chris-app\\pms-product\\uploads\\";
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			imageBytes = decoder.decodeBuffer(base64Image);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			image = ImageIO.read(bis);
			bis.close();
			// write the image to a file
			LOGGER.info("Saving image to location : "+ uploadLocation );
			File outputfile = new File(uploadLocation+"\\"+savedSiteVO.getSiteName()+"."+fileExtension.toLowerCase());
			boolean isUploaded = ImageIO.write(image, fileExtension.toLowerCase(), outputfile);
			if(isUploaded){
				savedSiteVO.setFileLocation(uploadLocation);
				savedSiteVO.setFileInput(fileInput);
				// S3 Integration Code
				//uploadImageToAwsS3Bucket(outputfile.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.info("Exit SiteServiceImpl .. uploadSiteImage");
		return savedSiteVO;
	}

	

	private CreateSiteVO validateObject(Site savedSite, CreateSiteVO siteVO) {
		try {
			if (Validator.validateForNulls(siteVO)) {
				// Do something that you want to
				LOGGER.info("Validations Successful");
				siteVO.setStatus(200);
			}
		} catch (RequiredFieldException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
			LOGGER.info("Validation error for site object", e);
			siteVO.setStatus(500);
			siteVO.setValidationMessage("Please enter the required field.");
		}
		return siteVO;
	}

	// Site Primary Information - UI TAB 1
	private Site populateSitePrimaryData(Site site, CreateSiteVO siteVO) {
		LOGGER.info("Inside SiteServiceImpl - populateSitePrimaryData");

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getSiteName())){
			site.setSiteName(siteVO.getSiteName());
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getOwner())){
			site.setSiteOwner(siteVO.getOwner());
		}

		if(siteVO.getDistrict()!=null){
			LOGGER.info("District Selected" + siteVO.getDistrict().getDistrictName());
			site.setDistrictId(siteVO.getDistrict().getDistrictId());
		}
		if(siteVO.getArea()!=null){
			LOGGER.info("Area Selected" + siteVO.getArea().getAreaName());
			site.setAreaId(siteVO.getArea().getAreaId());
		}
		if(siteVO.getCluster()!=null){
			LOGGER.info("Cluster Selected" + siteVO.getArea().getAreaName());
			site.setClusterId(siteVO.getCluster().getClusterID());
		}

		site.setElectricIdNo(siteVO.getElectricityId());

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getSiteNumber1())){
			site.setSiteNumberOne(Long.parseLong(siteVO.getSiteNumber1()));
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getSiteNumber2())){
			site.setSiteNumberTwo(Long.parseLong(siteVO.getSiteNumber2()));
		}

		LOGGER.info("Exit SiteServiceImpl - populateSitePrimaryData");
		return site;
	}


	// Site Contact Information - UI TAB 2
	private Site populateSiteContact(Site site, final CreateSiteVO siteVO) {
		LOGGER.info("Inside SiteServiceImpl - populateSiteContact");

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getContactName())){
			site.setAreaManagerName(siteVO.getContactName());
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getEmail())){
			site.setEmail(siteVO.getEmail());
		}

		site.setLatitude(siteVO.getLatitude());
		site.setLongitude(siteVO.getLongitude());

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getPrimaryContact())){
			site.setPrimaryContact(Long.parseLong(siteVO.getPrimaryContact()));
		}

		if(org.apache.commons.lang.StringUtils.isNotBlank(siteVO.getSecondaryContact())){
			site.setSecondaryContact(Long.parseLong(siteVO.getSecondaryContact()));
		}
		site.setSiteAddress1(siteVO.getSiteAddress1());
		site.setSiteAddress2(siteVO.getSiteAddress2());
		site.setSiteAddress3(siteVO.getSiteAddress3());
		site.setSiteAddress4(siteVO.getSiteAddress4());
		site.setPostCode(siteVO.getZipCode());
		
		LOGGER.info("Exit SiteServiceImpl - populateSiteContact");
		return site;
	}

	// Site License Information - UI TAB 3
	private Site populateLicenseDetails(Site site, CreateSiteVO siteVO) {
		LOGGER.info("Inside SiteServiceImpl - populateLicenseDetails");
		List<SiteLicenceVO> licenseVOList= siteVO.getSiteLicense();
		List<SiteLicence> licenseList= null; 
		if(siteVO.getSiteId()== null){
			licenseList = new ArrayList<SiteLicence>();
		}else if(siteVO.getSiteId()!=null){
			licenseList = new ArrayList<SiteLicence>();
			//licenseList = site.getSiteLicences();
			for(SiteLicenceVO siteLicenceVO : licenseVOList){
				if(siteLicenceVO.getLicenseId()!=null){
					licenseRepo.delete(siteLicenceVO.getLicenseId());
				}
			}
		}
		for(SiteLicenceVO siteLicenceVO : licenseVOList){
			SiteLicence siteLicense = new SiteLicence();
			siteLicense.setLicenceName(siteLicenceVO.getLicenseName());
			String licenceFromData = siteLicenceVO.getValidfrom();
			String licenceToData = siteLicenceVO.getValidto();

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			if(!StringUtils.isEmpty(licenceFromData) && !StringUtils.isEmpty(licenceToData)){
				Date licenseValidFrom;
				Date licenseValidTo;
				try {
					licenseValidFrom = formatter.parse(licenceFromData);
					licenseValidTo = formatter.parse(licenceToData);
					siteLicense.setStartDate(licenseValidFrom);
					siteLicense.setEndDate(licenseValidTo);
				} catch (ParseException e) {
					e.printStackTrace();
				}


			}
			/*if(!StringUtils.isEmpty(siteLicenceVO.getAttachment()){
					siteLicense.setAttachmentPath(siteLicenceVO.getAttachment());
				}*/
			siteLicense.setCreatedBy(siteVO.getCreatedBy());
			siteLicense.setSite(site);
			licenseList.add(siteLicense);

		}

		site.setSiteLicences(licenseList);
		LOGGER.info("Exit SiteServiceImpl - populateLicenseDetails");
		return site;
	}

	// Site Operation Information - UI TAB 4
	private Site populateOperationDetails(Site site, CreateSiteVO siteVO) {
		LOGGER.info("Inside SiteServiceImpl - populateOperationDetails");

		List<SiteOperationVO> siteOperationVOList =  siteVO.getSiteOperation();
		List<SiteDeliveryVO> siteDeliveryVOList =  siteVO.getSiteDelivery();

		List<SiteSalesOperation> salesOperationList = new ArrayList<SiteSalesOperation>();
		List<SiteDeliveryOperation> deliveryOperationList = new ArrayList<SiteDeliveryOperation>();

		if(siteVO.getSiteId() == null){
			LOGGER.info("Adding new Operation Details");
			for(SiteOperationVO siteSalesOpsVO : siteOperationVOList){
				SiteSalesOperation siteSalesOperation = new SiteSalesOperation();
				siteSalesOperation.setDayOfWeek(siteSalesOpsVO.getDays());
				if(org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getFrom()) && org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getTo())){
					siteSalesOperation.setOpStartTime(siteSalesOpsVO.getFrom());
					siteSalesOperation.setOpCloseTime(siteSalesOpsVO.getTo());
					siteSalesOperation.setSite(site);
				}else{
					siteSalesOperation.setOpStartTime("NO TIME");
					siteSalesOperation.setOpCloseTime("NO TIME");
				}
				salesOperationList.add(siteSalesOperation);
			}

			for(SiteDeliveryVO siteDeliveryVO : siteDeliveryVOList){
				SiteDeliveryOperation siteDeliveryOperation =  new SiteDeliveryOperation();;
				siteDeliveryOperation.setDayOfWeek(siteDeliveryVO.getDays());
				if(org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getFrom()) && org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getTo())){
					siteDeliveryOperation.setOpStartTime(siteDeliveryVO.getFrom());
					siteDeliveryOperation.setOpCloseTime(siteDeliveryVO.getTo());
					siteDeliveryOperation.setSite(site);

				}else{
					siteDeliveryOperation.setOpStartTime("NO TIME");
					siteDeliveryOperation.setOpCloseTime("NO TIME");
				}
				deliveryOperationList.add(siteDeliveryOperation);
			}

		}else if(siteVO.getSiteId()!=null){
			LOGGER.info("Updating existing Operation Details");

			if(!siteOperationVOList.isEmpty()){
				salesOperationList = siteSalesOperationRepo.findBySiteSiteId(siteVO.getSiteId());
				deliveryOperationList = siteDeliveryRepo.findBySiteSiteId(siteVO.getSiteId());
				/*for(SiteOperationVO siteSalesOperationVO :  siteOperationVOList){
					siteSalesOperationRepo.delete(siteSalesOperationVO.getOpId());
				}
				for(SiteDeliveryVO siteDeliveryVO : siteDeliveryVOList){
					siteDeliveryRepo.delete(siteDeliveryVO.getOpId());
				}*/
				List<SiteSalesOperation> temSalesOperations = new ArrayList<SiteSalesOperation>();
				List<SiteDeliveryOperation> temDeliveryOperations = new ArrayList<SiteDeliveryOperation>();

				for(SiteSalesOperation selectedSalesOps:salesOperationList){
					temSalesOperations.add(selectedSalesOps);
				}
				for(SiteDeliveryOperation selectedDeliveryOps:deliveryOperationList){
					temDeliveryOperations.add(selectedDeliveryOps);
				}
				salesOperationList.clear();
				deliveryOperationList.clear();

				for(SiteSalesOperation salesOperation: temSalesOperations){
					SiteSalesOperation tempSalesOperation = new SiteSalesOperation();
					for(SiteOperationVO siteSalesOpsVO : siteOperationVOList){
						if(salesOperation.getSaledsOpId().equals(siteSalesOpsVO.getOpId())){
							tempSalesOperation.setSaledsOpId(salesOperation.getSaledsOpId());
							tempSalesOperation.setDayOfWeek(siteSalesOpsVO.getDays());
							if(org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getFrom()) && 
									org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getTo())){
								tempSalesOperation.setOpStartTime(siteSalesOpsVO.getFrom());
								tempSalesOperation.setOpCloseTime(siteSalesOpsVO.getTo());
								tempSalesOperation.setSite(site);
							}else{
								tempSalesOperation.setOpStartTime("NO TIME");
								tempSalesOperation.setOpCloseTime("NO TIME");
							}
							salesOperationList.add(tempSalesOperation);
						}
					}
				}


				for(SiteDeliveryOperation deliveryOperation: temDeliveryOperations){
					SiteDeliveryOperation tempDelOperation = new SiteDeliveryOperation();
					for(SiteDeliveryVO siteDeliveryVO : siteDeliveryVOList){
						if(deliveryOperation.getDeliveryOpId().equals(siteDeliveryVO.getOpId())){
							tempDelOperation.setDeliveryOpId(deliveryOperation.getDeliveryOpId());
							tempDelOperation.setDayOfWeek(siteDeliveryVO.getDays());
							if(org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getFrom()) && 
									org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getTo())){
								tempDelOperation.setOpStartTime(siteDeliveryVO.getFrom());
								tempDelOperation.setOpCloseTime(siteDeliveryVO.getTo());
								tempDelOperation.setSite(site);
							}else{
								tempDelOperation.setOpStartTime("NO TIME");
								tempDelOperation.setOpCloseTime("NO TIME");
							}
							deliveryOperationList.add(tempDelOperation);
						}
					}
				}
			}
		}

		/*	if(!siteOperationVOList.isEmpty()){
			// TO be decided for optimization later

			for(SiteOperationVO siteSalesOpsVO : siteOperationVOList){
				SiteSalesOperation siteSalesOperation = new SiteSalesOperation();
				siteSalesOperation.setDayOfWeek(siteSalesOpsVO.getDays());
				if(org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getFrom()) && org.apache.commons.lang.StringUtils.isNotBlank(siteSalesOpsVO.getTo())){
					siteSalesOperation.setOpStartTime(siteSalesOpsVO.getFrom());
					siteSalesOperation.setOpCloseTime(siteSalesOpsVO.getTo());
					siteSalesOperation.setSite(site);
				}else{
					siteSalesOperation.setOpStartTime("NO TIME");
					siteSalesOperation.setOpCloseTime("NO TIME");
				}
				salesOperationList.add(siteSalesOperation);
			}
		}*/

		/*for(SiteDeliveryVO siteDeliveryVO : siteDeliveryVOList){
			SiteDeliveryOperation siteDeliveryOperation =  new SiteDeliveryOperation();;
			siteDeliveryOperation.setDayOfWeek(siteDeliveryVO.getDays());
			if(org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getFrom()) && org.apache.commons.lang.StringUtils.isNotBlank(siteDeliveryVO.getTo())){
				siteDeliveryOperation.setOpStartTime(siteDeliveryVO.getFrom());
				siteDeliveryOperation.setOpCloseTime(siteDeliveryVO.getTo());
				siteDeliveryOperation.setSite(site);

			}else{
				siteDeliveryOperation.setOpStartTime("NO TIME");
				siteDeliveryOperation.setOpCloseTime("NO TIME");
			}
			deliveryOperationList.add(siteDeliveryOperation);
		}*/
		if(salesOperationList.size()==7 && deliveryOperationList.size()==7){
			site.setSiteDeliveryOpetaionTimes(deliveryOperationList);
			site.setSiteSalesOpetaionTimes(salesOperationList);
		}

		LOGGER.info("Exit SiteServiceImpl - populateOperationDetails");
		return site;
	}


	// Site Submeter Information - UI TAB 5
	private Site populateSubmeterDetails(Site site, CreateSiteVO siteVO) {
		LOGGER.info("Inside SiteServiceImpl - populateSubmeterDetails");
		List<SiteSubmeterVO> siteSubmeterVOList =  siteVO.getSiteSubmeter();
		List<SiteSubMeter> subMeterList = new ArrayList<SiteSubMeter>();
		if(siteVO.getSiteId()== null){
			subMeterList = new ArrayList<SiteSubMeter>();

		}else if(siteVO.getSiteId()!=null){
			subMeterList = new ArrayList<SiteSubMeter>();
		}
		for(SiteSubmeterVO siteSubmeterVO :  siteSubmeterVOList){
			if(siteSubmeterVO.getSubMeterId()!=null){
				SiteSubMeter siteSubmeter = siteSubMeterRepo.findOne(siteSubmeterVO.getSubMeterId());
				siteSubmeter.setSubMeterNumber(siteSubmeterVO.getSubMeterNumber());
				siteSubmeter.setSubMeterUser(siteSubmeterVO.getSubMeterUser());
				siteSubmeter.setCreatedBy(siteVO.getCreatedBy());
				siteSubmeter.setSite(site);
				subMeterList.add(siteSubmeter);
			}else{
				SiteSubMeter siteSubmeter = new SiteSubMeter();
				siteSubmeter.setSubMeterNumber(siteSubmeterVO.getSubMeterNumber());
				siteSubmeter.setSubMeterUser(siteSubmeterVO.getSubMeterUser());
				siteSubmeter.setCreatedBy(siteVO.getCreatedBy());
				siteSubmeter.setSite(site);
				subMeterList.add(siteSubmeter);
			}
		}


		/*	for(SiteSubmeterVO siteSubmeterVO : siteSubmeterVOList){
			SiteSubMeter siteSubmeter = new SiteSubMeter();
			siteSubmeter.setSubMeterNumber(siteSubmeterVO.getSubMeterNumber());
			siteSubmeter.setSubMeterUser(siteSubmeterVO.getSubMeterUser());
			siteSubmeter.setCreatedBy(siteVO.getCreatedBy());
			siteSubmeter.setSite(site);
			subMeterList.add(siteSubmeter);
		}*/
		site.setSiteSubmeterList(subMeterList);
		LOGGER.info("Exit SiteServiceImpl - populateSubmeterDetails");
		return site;
	}



	@Override
	public CreateSiteVO getSiteDetails(final Long siteId) {
		LOGGER.info("Inside SiteServiceImpl - getSiteDetails");
		Site site = siteRepo.findOne(siteId);
		CreateSiteVO siteVO = new CreateSiteVO();
		if(site!=null){
			List<String> fullAddress = new ArrayList<String>();
			List<SiteLicenceVO> siteLicensesVO =  siteVO.getSiteLicense();
			List<SiteLicence> licenseList = licenseRepo.findBySiteSiteId(site.getSiteId());
			site.setSiteLicences(licenseList);
			List<SiteSalesOperation> salesOpsList = siteSalesOperationRepo.findBySiteSiteId(site.getSiteId());
			site.setSiteSalesOpetaionTimes(salesOpsList);
			List<SiteDeliveryOperation> deliveryOpsList = siteDeliveryRepo.findBySiteSiteId(site.getSiteId());
			site.setSiteDeliveryOpetaionTimes(deliveryOpsList);
			List<SiteSubMeter> subMeterList = siteSubMeterRepo.findBySiteSiteId(site.getSiteId());
			site.setSiteSubmeterList(subMeterList);
			siteVO.setSiteId(site.getSiteId());
			siteVO.setSiteName(site.getSiteName());
			siteVO.setSiteAddress1(site.getSiteAddress1());
			siteVO.setSiteAddress2(site.getSiteAddress2());
			siteVO.setSiteAddress3(site.getSiteAddress3());
			siteVO.setSiteAddress4(site.getSiteAddress4());
			siteVO.setZipCode(site.getPostCode());
			
			if(!StringUtils.isEmpty(siteVO.getSiteAddress1())){
				fullAddress.add(siteVO.getSiteAddress1());
			}
			if(!StringUtils.isEmpty(siteVO.getSiteAddress2())){
				fullAddress.add(siteVO.getSiteAddress2());
			}
			if(!StringUtils.isEmpty(siteVO.getSiteAddress3())){
				fullAddress.add(siteVO.getSiteAddress3());
			}
			if(!StringUtils.isEmpty(siteVO.getSiteAddress4())){
				fullAddress.add(siteVO.getSiteAddress4());
			}
			if(!StringUtils.isEmpty(siteVO.getZipCode())){
				fullAddress.add(siteVO.getZipCode());
			}
			
			String finalAddress = org.apache.commons.lang3.StringUtils.join(fullAddress,",");
			siteVO.setFullAddress(finalAddress);
			
		
			siteVO.setOperator(site.getOperator()); 
			siteVO.setOwner(site.getSiteOwner());


			if(site.getClusterId() !=null){
				Cluster cluster = clusterRepo.findOne(site.getClusterId());
				siteVO.setCluster(cluster);
			}
			if(site.getDistrictId() !=null){
				District district = districtRepo.findOne(site.getDistrictId());
				siteVO.setDistrict(district);
			}
			if(site.getAreaId() !=null){
				Area area = areaRepo.findOne(site.getAreaId());
				siteVO.setArea(area);
			}

			siteVO.setContactName(site.getAreaManagerName());
			siteVO.setEmail(site.getEmail());
			siteVO.setElectricityId(site.getElectricIdNo());
			if(site.getLatitude()==null && site.getLatitude() == null){

			}else{
				siteVO.setLatitude(site.getLatitude().toString());
				siteVO.setLongitude(site.getLongitude().toString());
			}

			if(site.getSiteNumberOne() ==  null ) {

			}else{
				siteVO.setSiteNumber1(site.getSiteNumberOne().toString());
			}

			if(site.getSiteNumberTwo()==null ) {

			}else{
				siteVO.setSiteNumber2(site.getSiteNumberTwo().toString());
			}


			if(StringUtils.isEmpty(site.getPrimaryContact())){

			}
			else{
				siteVO.setPrimaryContact(site.getPrimaryContact().toString());
			}
			if(StringUtils.isEmpty(site.getSecondaryContact())){

			}else{
				siteVO.setSecondaryContact(site.getSecondaryContact().toString());
			}


			if(!site.getSiteLicences().isEmpty()){
				for(SiteLicence siteLicence : site.getSiteLicences()){
					SiteLicenceVO siteLicenceVO = new SiteLicenceVO();
					siteLicenceVO.setLicenseId(siteLicence.getLicenseId());
					siteLicenceVO.setLicenseName(siteLicence.getLicenseName());

					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					if(siteLicence.getStartDate()!=null && siteLicence.getEndDate()!=null){
						Date licenseValidFrom =  siteLicence.getStartDate();
						Date licenseValidTo  = siteLicence.getEndDate();
						String startDate = formatter.format(licenseValidFrom);
						String endDate = formatter.format(licenseValidTo);
						siteLicenceVO.setValidfrom(startDate);
						siteLicenceVO.setValidto(endDate);
						siteLicensesVO.add(siteLicenceVO);
					}
					siteVO.setSiteLicense(siteLicensesVO);

				}
			}
			//siteVO.getSiteOperation().clear();
			for(SiteSalesOperation siteSalesOperation : site.getSiteSalesOpetaionTimes()){
				SiteOperationVO siteOperationVO = new SiteOperationVO();
				siteOperationVO.setOpId(siteSalesOperation.getSaledsOpId());
				siteOperationVO.setDays(siteSalesOperation.getDayOfWeek());
				siteOperationVO.setFrom(siteSalesOperation.getOpStartTime());
				siteOperationVO.setTo(siteSalesOperation.getOpCloseTime());
				siteVO.getSiteOperation().add(siteOperationVO);
			}

			//siteVO.getSiteDelivery().clear();
			for(SiteDeliveryOperation siteDeliveryOperation : site.getSiteDeliveryOpetaionTimes()){
				SiteDeliveryVO siteDeliveryVO = new SiteDeliveryVO();
				siteDeliveryVO.setOpId(siteDeliveryOperation.getDeliveryOpId());
				siteDeliveryVO.setDays(siteDeliveryOperation.getDayOfWeek());
				siteDeliveryVO.setFrom(siteDeliveryOperation.getOpStartTime());
				siteDeliveryVO.setTo(siteDeliveryOperation.getOpCloseTime());
				siteVO.getSiteDelivery().add(siteDeliveryVO);
			}
			if(!site.getSiteSubmeterList().isEmpty()){
				for(SiteSubMeter siteSubmeter: site.getSiteSubmeterList()){
					SiteSubmeterVO siteSubmeterVO = new SiteSubmeterVO();
					siteSubmeterVO.setSubMeterId(siteSubmeter.getSubMeterId());
					siteSubmeterVO.setSubMeterNumber(siteSubmeter.getSubMeterNumber());
					siteSubmeterVO.setSubMeterUser(siteSubmeter.getSubMeterUser());
					siteVO.getSiteSubmeter().add(siteSubmeterVO);
				}
			}
		}
		LOGGER.info("Exit SiteServiceImpl - getSiteDetails");
		return siteVO;
	}


}
