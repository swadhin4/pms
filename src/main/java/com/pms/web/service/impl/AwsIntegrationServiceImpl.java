package com.pms.web.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.util.ApplicationUtil;
import com.pms.web.util.RestResponse;

@Service("awsIntegrationService")
public class AwsIntegrationServiceImpl implements AwsIntegrationService{
	
	private static final String SUFFIX = "/";
	
	@Override
	public void uploadObject(PutObjectRequest fileObject, AmazonS3 s3Client) {
		s3Client.putObject(fileObject);
	}

	@Override
	public void createFolder(String bucketName, String folderName, AmazonS3 s3Client) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent, metadata);

		// send request to S3 to create folder
		s3Client.putObject(putObjectRequest);
	}

	@Override
	public void deleteFolder(String bucketName, String folderName, AmazonS3 s3Client) {
		List<S3ObjectSummary> fileList = s3Client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			s3Client.deleteObject(bucketName, file.getKey());
		}
		s3Client.deleteObject(bucketName, folderName);
	}

	@Override
	public File downloadFile(String bucketName, String keyName) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAICD42CCYTOXBJDOA","fwKFXHtteCVnKt3bxaj6muPNs55ZlI3BvKw70Zp/");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		byte[] readBuf = new byte[1024];
		String imageBytes ="";
		File file=null;
		try{
			S3Object o=s3client.getObject(bucketName, keyName);
			S3ObjectInputStream s3is = o.getObjectContent();
			String contentType = o.getObjectMetadata().getContentType();
			String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
			file=new File(fileDownloadLocation+"//"+keyName);
				FileOutputStream fos =  new FileOutputStream(file);
				int read_len = 0;
				while((read_len = s3is.read(readBuf))>0){
					fos.write(readBuf,0,read_len);
				}
				
				s3is.close();
				fos.close();
			//System.out.println(imageBytes);
			
		}catch(AmazonServiceException e){
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return file;
	}

	@Override
	public RestResponse deleteFile(String bucketName, String keyName) throws Exception {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAICD42CCYTOXBJDOA","fwKFXHtteCVnKt3bxaj6muPNs55ZlI3BvKw70Zp/");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		return null;
	}

	@Override
	public RestResponse deleteMultipleFile(List<KeyVersion> keys) throws Exception {
		RestResponse response = new RestResponse();
		AWSCredentials credentials = new BasicAWSCredentials("AKIAICD42CCYTOXBJDOA","fwKFXHtteCVnKt3bxaj6muPNs55ZlI3BvKw70Zp/");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest("malay-first-s3-bucket-pms-test");
		multiObjectDeleteRequest.setKeys(keys);
		try {
		    DeleteObjectsResult delObjRes = s3client.deleteObjects(multiObjectDeleteRequest);
		    System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
		    response.setStatusCode(200);			
		} catch (MultiObjectDeleteException e) {
			   response.setStatusCode(500);
			   e.printStackTrace();
		}
		return response;
	}

}
