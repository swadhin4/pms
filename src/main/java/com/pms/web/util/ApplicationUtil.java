package com.pms.web.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationUtil.class);
	public static String getServerUploadLocation(){
		LOGGER.info("Inside ApplicationUtil .. getServerUploadLocation");
		LOGGER.info("Getting server resource storage location...");
		String serverPath = System.getProperty("catalina.base");
		serverPath=serverPath.replaceAll("\\\\","/");
		Path path = FileSystems.getDefault().getPath(serverPath+"/uploads/malay-first-s3-bucket-pms-test/"); 
	 	String fileUploadLocation = path.toString();
	 	LOGGER.info("Resource Storage location : "+ fileUploadLocation);
	 	LOGGER.info("Exit ApplicationUtil .. getServerUploadLocation");
		return fileUploadLocation;
	}
	
	public static String getServerDownloadLocation(){
		LOGGER.info("Inside ApplicationUtil .. getServerDownloadLocation");
		LOGGER.info("Getting server resource storage location...");
		String serverPath = System.getProperty("catalina.base");
		serverPath=serverPath.replaceAll("\\\\","/");
		Path path = FileSystems.getDefault().getPath(serverPath+"/downloads/malay-first-s3-bucket-pms-test/"); 
	 	String fileDownloadLocation = path.toString();
	 	LOGGER.info("Resource Storage location : "+ fileDownloadLocation);
	 	LOGGER.info("Exit ApplicationUtil .. getServerDownloadLocation");
		return fileDownloadLocation;
	}
	
}
