package com.pms.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.jpa.entities.Asset;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Site;
import com.pms.jpa.entities.SiteLicence;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.repositories.AssetRepo;
import com.pms.jpa.repositories.LicenseRepo;
import com.pms.jpa.repositories.SiteRepo;
import com.pms.jpa.repositories.TicketAttachmentRepo;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.service.FileIntegrationService;
import com.pms.web.util.ApplicationUtil;
import com.pms.web.util.RestResponse;

@Service(value="fileIntegrationService")
public class FileIntegrationServiceImpl implements FileIntegrationService {

	private final static Logger LOGGER = LoggerFactory.getLogger(FileIntegrationServiceImpl.class);
	
	@Autowired
	private SiteRepo siteRepo;
	
	@Autowired
	private AwsIntegrationService awsIntegrationService;
	
	@Autowired
	private LicenseRepo licenseRepo;
	
	@Autowired
	private AssetRepo assetRepo;
	
	@Autowired
	private TicketAttachmentRepo ticketAttachmentRepo;
	
	@Override
	public String siteFileUpload(CreateSiteVO siteVO, UploadFile siteFile, Company company) throws IOException{
		LOGGER.info("Insdie FileIntegrationServiceImpl .. siteFileUpload");
		String base64Image = siteFile.getBase64ImageString().split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
		Site site=null;
		String generatedFileName="";
		Path destinationFile =null;
		String fileKey="";
		String siteName=siteVO.getSiteName();
		if(siteVO.getSiteId()!=null){
			site=siteRepo.findOne(siteVO.getSiteId());
			if(StringUtils.isNotBlank(site.getAttachmentPath())){
				generatedFileName=site.getAttachmentPath();
				fileKey=generatedFileName;
				destinationFile = Paths.get(fileUploadLocation+"\\"+generatedFileName);
			}else{
				siteName = siteName.replaceAll(" ", "_").toLowerCase();
				generatedFileName = siteName+"_"+Calendar.getInstance().getTimeInMillis()+"."+siteFile.getFileExtension().toLowerCase();
				destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\site\\"+generatedFileName);
				fileKey=company.getCompanyCode()+"/site/"+generatedFileName;
			}
		}else{
			generatedFileName = siteVO.getSiteName()+"_"+Calendar.getInstance().getTimeInMillis()+"."+siteFile.getFileExtension().toLowerCase();
			destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\site\\"+generatedFileName);
			fileKey=company.getCompanyCode()+"/site/"+generatedFileName;
		}
		
		try {
			Files.write(destinationFile, imageBytes);
			LOGGER.info("Saving image to location : "+ destinationFile.toString() );
			siteVO.setFileLocation(destinationFile.toString());
			//pushToAwsS3(destinationFile, fileKey);
		
		} catch (IOException e) {
			LOGGER.info("Unable to upload site image ", e );
		}
		
		LOGGER.info("Exit FileIntegrationServiceImpl .. siteFileUpload");
		return fileKey;
	}



	private void pushToAwsS3(Path destinationFile, String fileKey) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAICD42CCYTOXBJDOA","fwKFXHtteCVnKt3bxaj6muPNs55ZlI3BvKw70Zp/");
		@SuppressWarnings("deprecation")
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		String bucketName="malay-first-s3-bucket-pms-test";
		awsIntegrationService.uploadObject(new PutObjectRequest(bucketName, fileKey, destinationFile.toFile()).withCannedAcl(CannedAccessControlList.Private), s3client);
	}

	

	@Override
	public String siteLicenseFileUpload(UploadFile licenseFile, Company company)  throws IOException {
		LOGGER.info("Inside FileIntegrationServiceImpl .. siteLicenseFileUpload");
			String base64Image = licenseFile.getBase64ImageString().split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
			SiteLicence license = null;
			if(licenseFile.getLicenseId()!=null){
				license = licenseRepo.findOne(licenseFile.getLicenseId());
			}
			String generatedFileName="";
			String fileKey="";
			Path destinationFile = null;
			String licenseName=licenseFile.getFileName();
			if(license!=null){
				if(StringUtils.isNotBlank(license.getAttachmentPath())){
					generatedFileName=license.getAttachmentPath();
					destinationFile = Paths.get(fileUploadLocation+"\\"+generatedFileName);
					fileKey=generatedFileName;
				}else{
					licenseName=licenseName.replaceAll(" ", "_").toLowerCase();
					generatedFileName = licenseName+"_"+Calendar.getInstance().getTimeInMillis()+"."+licenseFile.getFileExtension().toLowerCase();
					destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\site\\license\\"+generatedFileName);
					fileKey=company.getCompanyCode()+"/site/license/"+generatedFileName;
				}
			}else{
				licenseName=licenseName.replaceAll(" ", "_").toLowerCase();
				generatedFileName = licenseName+"_"+Calendar.getInstance().getTimeInMillis()+"."+licenseFile.getFileExtension().toLowerCase();
				destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\site\\license\\"+generatedFileName);
				fileKey=company.getCompanyCode()+"/site/license/"+generatedFileName;
			}
			try {
				Files.write(destinationFile, imageBytes);
				LOGGER.info("Saving image to location : "+  destinationFile.toString() );
				//pushToAwsS3(destinationFile,  fileKey);
			} catch (IOException e) {
				LOGGER.info("Unable to upload license image ", e );
			}
		
		LOGGER.info("Exit SiteServiceImpl .. uploadSiteLicenseImage");
		return fileKey;
	}

	@Override
	public String siteIncidentFileUpload(List<UploadFile> incidentFileList, TicketVO customerTicketVO, Company company, String folderLocation)  throws IOException {
		LOGGER.info("Inside FileIntegrationServiceImpl .. siteIncidentFileUpload");
		Map<Path,String> incidentKeyMap = new HashMap<Path, String>();
		for(UploadFile attachment : incidentFileList){
			String base64Image = "";
			String fileKey="";
			Path destinationFile = null;
			String generatedFileName="";
			//String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
				base64Image = attachment.getBase64ImageString().split(",")[1];
				generatedFileName=customerTicketVO.getTicketNumber()+"_"+Calendar.getInstance().getTimeInMillis()+"."+attachment.getFileExtension().toLowerCase();
				destinationFile = Paths.get(folderLocation+"\\"+generatedFileName);
				fileKey=company.getCompanyCode()+"/incident/"+customerTicketVO.getTicketNumber()+"/"+generatedFileName;
			if(StringUtils.isEmpty(base64Image)){
				LOGGER.info("No Image or Document selected" );
			}else{
				byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
				try {
					Files.write(destinationFile, imageBytes);
					LOGGER.info("Saving image to location : "+ destinationFile.toString() );
					incidentKeyMap.put(destinationFile, fileKey);
					TicketAttachment ticketAttachment = new TicketAttachment();
					ticketAttachment.setAttachmentPath(fileKey);
					ticketAttachment.setTicketId(customerTicketVO.getTicketId());
					ticketAttachment.setTicketNumber(customerTicketVO.getTicketNumber());
					ticketAttachmentRepo.save(ticketAttachment);
				} catch (IOException e) {
					LOGGER.info("Unable to upload incident files ", e );
				}
			}
		}
		
		for (Map.Entry<Path, String> pathEntry : incidentKeyMap.entrySet()) {
			LOGGER.info("Uploading file to S3 : "+ pathEntry.getValue());
			
			pushToAwsS3(pathEntry.getKey(),  pathEntry.getValue());
		}

		LOGGER.info("Exit FileIntegrationServiceImpl .. siteIncidentFileUpload");
		return null;
	}

	@Override
	public AssetVO siteAssetFileUpload(AssetVO assetVO,UploadFile assetFile, Company company, String type)  throws IOException{
		LOGGER.info("Inside FileIntegrationServiceImpl .. siteAssetFileUpload");
		String base64Image = assetFile.getBase64ImageString().split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
		Asset asset=null;
		String generatedFileName="";
		Path destinationFile =null;
		String fileKey="";
		String assetName=assetVO.getAssetName();
		if(assetVO.getAssetId()!=null){
			asset=assetRepo.findOne(assetVO.getAssetId());
			if(type.equalsIgnoreCase("IMAGE")){
				if(StringUtils.isNotBlank(asset.getImagePath())){
					generatedFileName=asset.getImagePath();
					destinationFile = Paths.get(fileUploadLocation+"\\"+generatedFileName);
					fileKey=generatedFileName;
				}else{
					assetName = assetName.replaceAll(" ", "_").toLowerCase();
					generatedFileName = assetName+"_"+Calendar.getInstance().getTimeInMillis()+"."+assetFile.getFileExtension().toLowerCase();
					destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\asset\\"+generatedFileName);
					fileKey=company.getCompanyCode()+"/asset/"+generatedFileName;
				}
				try {
					Files.write(destinationFile, imageBytes);
					LOGGER.info("Saving image to location : "+ destinationFile.toString() );
					assetVO.setImagePath(fileKey);
					//pushToAwsS3(destinationFile, fileKey);
					
				
				} catch (IOException e) {
					LOGGER.info("Unable to upload site image ", e );
				}
				
			}
			
			if(type.equalsIgnoreCase("DOC")){
				if(StringUtils.isNotBlank(asset.getDocumentPath())){
					generatedFileName=asset.getDocumentPath();
					destinationFile = Paths.get(fileUploadLocation+"\\"+generatedFileName);
					fileKey=generatedFileName;
				}else{
					assetName = assetName.replaceAll(" ", "_").toLowerCase();
					generatedFileName = assetName+"_"+Calendar.getInstance().getTimeInMillis()+"."+assetFile.getFileExtension().toLowerCase();
					destinationFile = Paths.get(fileUploadLocation+"\\"+company.getCompanyCode()+"\\asset\\"+generatedFileName);
					fileKey=company.getCompanyCode()+"/asset/"+generatedFileName;
				}
				
				try {
					Files.write(destinationFile, imageBytes);
					LOGGER.info("Saving Doc to location : "+ destinationFile.toString() );
					assetVO.setDocumentPath(fileKey);
					//pushToAwsS3(destinationFile, fileKey);
				
				} catch (IOException e) {
					LOGGER.info("Unable to upload site image ", e );
				}
			}
		}
		LOGGER.info("Exit FileIntegrationServiceImpl .. siteAssetFileUpload");
		return assetVO;
	}



	@Override
	public RestResponse getFileLocation(Company company, String keyName) throws Exception {
		LOGGER.info("Inside FileIntegrationServiceImpl .. getFileLocation");
		RestResponse response = new RestResponse();
		File file = null;
		if (org.apache.commons.lang3.StringUtils.isNotBlank(keyName)) {
			try{
			String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
			String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
			file = new File(fileUploadLocation+"/"+keyName);
			if(file.exists()){
				 File downloadDirectory = new File(fileDownloadLocation+"\\"+keyName);
				 copyFileUsingApacheCommonsIO(file, downloadDirectory);
				 LOGGER.info("File copied to download location : "+ downloadDirectory.getPath());
					response.setStatusCode(200);
					response.setMessage(downloadDirectory.getPath());
				 
			}else{
				 LOGGER.info("File not found in server, so connecting to AWS S3");
			file = awsIntegrationService.downloadFile("malay-first-s3-bucket-pms-test", keyName);
			if (file!=null){
				if(file.exists()) {
					response.setStatusCode(200);
					response.setMessage(file.getPath());
				}
			}
			else{
				response.setStatusCode(404);
				response.setMessage("File not found");
			}
			
			}
			}catch(AmazonS3Exception e){
				LOGGER.info("Key name"+ keyName +" does not exits.", e);
				response.setStatusCode(500);
				response.setMessage("Invalid File name");
			}
			catch(Exception e){
				LOGGER.info("File does not exits", e);
				response.setStatusCode(500);
				response.setMessage("File does not exits");
			}
		}else{
			response.setStatusCode(404);
			response.setMessage("File Key name is empty");
			
		}
		LOGGER.info("Exit FileIntegrationServiceImpl .. getFileLocation");
		return response;
	}

	private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
	    FileUtils.copyFile(source, dest);
	}

	@Override
	public String createIncidentFolder(String incidentNumber, Company company) throws IOException {
		LOGGER.info("Inside FileIntegrationServiceImpl .. createIncidentFolder");
		String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
		String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
		LOGGER.info("Incident Folder Location : "+  fileUploadLocation);
		String directoryName = incidentNumber;
		 File uploadDirectory = new File(fileUploadLocation+"\\"+company.getCompanyCode()+"\\incident\\"+directoryName);
		 File downloadDirectory = new File(fileDownloadLocation+"\\"+company.getCompanyCode()+"\\incident\\"+directoryName);
		    if (! uploadDirectory.exists()){
		        if(uploadDirectory.mkdir()){
		        	 LOGGER.info("Incident Upload Folder created : "+  uploadDirectory.getPath());
		        	 downloadDirectory.mkdir();
		        	 LOGGER.info("Incident Download Folder created : "+  downloadDirectory.getPath());
		        }else{
		        	LOGGER.info("Unable to create incident directory");
		        }
		       
		    }else{
		    	LOGGER.info("Directory already exists");
		    }
		LOGGER.info("Exit FileIntegrationServiceImpl .. createIncidentFolder");
		return uploadDirectory.getPath();
	}



	@Override
	public RestResponse deleteFile(Long siteId, List<Long> licenseIdList, Long assetId, List<Long> incidentList, String keyName) throws Exception {
		LOGGER.info("Inside FileIntegrationServiceImpl .. deleteFile");
		RestResponse response = new RestResponse();
		if(siteId!=null){
			Site site = siteRepo.findOne(siteId);
			LOGGER.info("Deleting attachment for Site : "+site.getSiteName());
			deleteFileFromS3(site.getAttachmentPath(), response);
			LOGGER.info("Deleted successfully from S3");
			site.setAttachmentPath(null);
			siteRepo.save(site);
			response.setStatusCode(200);
		}
		if(licenseIdList!=null && !licenseIdList.isEmpty()){
			List<SiteLicence> siteLicenseList = licenseRepo.findByLicenseIdIn(licenseIdList);
			if(!siteLicenseList.isEmpty()){
				List<KeyVersion> keys = new ArrayList<KeyVersion>();
				for(SiteLicence license:siteLicenseList){
					LOGGER.info("Deleting attachment for License  : "+license.getLicenseName());
					SiteLicence tempLicense = license;
					tempLicense.setAttachmentPath(null);
					keys.add(new KeyVersion(tempLicense.getAttachmentPath()));
					licenseRepo.save(tempLicense);
				}
				response= awsIntegrationService.deleteMultipleFile(keys);
				if(response.getStatusCode()==200){
					LOGGER.info("Deleted successfully from S3");
					response.setStatusCode(200);
				}
			}
			
		}
		
		if(assetId!=null){
			Asset asset  = assetRepo.findOne(assetId);
			if(asset.getImagePath()!=null){
				LOGGER.info("Deleting attachment image for asset  : "+asset.getAssetName());
				deleteFileFromS3(asset.getImagePath(), response);
				asset.setImagePath(null);
				LOGGER.info("Deleted successfully from S3");
			}
			if(asset.getDocumentPath()!=null){
				LOGGER.info("Deleting attachment document for asset  : "+asset.getAssetName());
				deleteFileFromS3(asset.getImagePath(), response);
				asset.setDocumentPath(null);
				LOGGER.info("Deleted successfully from S3");
			}
			assetRepo.save(asset);
			response.setStatusCode(200);
		}
		
		if(incidentList!=null && !incidentList.isEmpty()){
			List<TicketAttachment> ticketAttachmentList = ticketAttachmentRepo.findByAttachmentIdIn(incidentList);
			if(!ticketAttachmentList.isEmpty()){
				List<KeyVersion> keys = new ArrayList<KeyVersion>();
				for(TicketAttachment attachment :ticketAttachmentList){
					LOGGER.info("Deleting incident attachment file for incident  : "+attachment.getTicketNumber());
					TicketAttachment tempAttachment = attachment;
					keys.add(new KeyVersion(tempAttachment.getAttachmentPath()));
					ticketAttachmentRepo.delete(tempAttachment.getAttachmentId());
				}
				response= awsIntegrationService.deleteMultipleFile(keys);
				if(response.getStatusCode()==200){
					LOGGER.info("Deleted successfully from S3");
					response.setStatusCode(200);
				}
			}
		}
		
		
		LOGGER.info("Exit FileIntegrationServiceImpl .. deleteFile");
		return response;
	}



	private void deleteFileFromS3(String keyName, RestResponse response) throws Exception {
		try{
			awsIntegrationService.deleteFile("malay-first-s3-bucket-pms-test", keyName);
			response.setStatusCode(200);
		}catch(IOException e){
			response.setStatusCode(500);
			LOGGER.info("Exception while deleting attachment", e);
		}
	}
	

}
