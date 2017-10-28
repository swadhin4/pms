/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pmsapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pmsapp.view.vo.DistrictVO;
import com.pmsapp.view.vo.LoginUser;
import com.web.service.ApplicationService;
import com.web.service.DistrictService;
import com.web.service.EmailService;
import com.web.service.UserService;
import com.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/district")
@Controller
public class DistrictController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(DistrictController.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private DistrictService districtService;



	@RequestMapping(value = "/api/country/{countryId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllDistricts(@PathVariable(value="countryId") final Long countryId, final HttpSession session) {
		logger.info("Inside DistrictController .. listAllDistricts");
		List<DistrictVO> districtList = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				districtList = districtService.findDistrictByCountry(countryId);
				if(!districtList.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(districtList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No district found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(505);
				response.setMessage("Exception in getting response");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				logger.info("Exception in getting response", e);

			}
		}

		logger.info("Exit DistrictController .. listAllDistricts");
		return responseEntity;
	}
}
