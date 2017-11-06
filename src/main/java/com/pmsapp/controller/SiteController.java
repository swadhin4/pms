/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pmsapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pmsapp.view.vo.CreateSiteVO;
import com.pmsapp.view.vo.LoginUser;
import com.web.service.ApplicationService;
import com.web.service.EmailService;
import com.web.service.SiteService;
import com.web.service.UserService;
import com.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/site")
@Controller
public class SiteController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SiteController.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private EmailService emailService;


	@Autowired
	private SiteService siteService;



	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = "application/json")
	public String userHome(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			boolean isSessionEnabled=request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled +""+ request.getSession().getId());
			model.put("user", loginUser);
			return "site.list";
		} else {
			return "redirect:/login";
		}
	}


	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String userDetails(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				return "site.details";
			}
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> createNewSite(final Locale locale, final ModelMap model,
			@RequestBody final CreateSiteVO createSiteVO, final HttpSession session) {
		logger.info("Inside SiteController .. createNewSite");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);

		CreateSiteVO savedSiteVO = null;
		if(loginUser!=null){
			try {
				logger.info("CreateSiteVO : "+ createSiteVO);
				savedSiteVO= siteService.saveOrUpdate(createSiteVO, loginUser);
				if(savedSiteVO.getSiteId()==null){
					response.setStatusCode(204);
					response.setMessage("Please verify the required fields");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}
				else if(savedSiteVO.getSiteId() != null){
					if(savedSiteVO.getStatus()==201){
						response.setStatusCode(200);
						response.setObject(savedSiteVO);
						response.setMessage("New site created successfully");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}else{
						response.setStatusCode(200);
						response.setObject(savedSiteVO);
						response.setMessage("Site updated successfully");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
				}

			} catch (Exception e) {
				logger.info("Exception occured while saving or updating site", e);
				response.setMessage("Exception occured while saving or updating site");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);

			}
		}

		logger.info("Exit SiteController .. createNewSite");
		return responseEntity;
	}
	
	@RequestMapping(value = "/selected/{siteId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getSelectedSite(@PathVariable(value="siteId") Long siteId, final HttpSession session) {
		logger.info("Inside SiteController .. getSelectedSite");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);

		CreateSiteVO savedSiteVO = null;
		if(loginUser!=null){
			try {
				savedSiteVO= siteService.getSiteDetails(siteId);
				if(savedSiteVO.getSiteId()!=null){
						response.setStatusCode(200);
						response.setObject(savedSiteVO);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}else{
						response.setStatusCode(404);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
					}

			} catch (Exception e) {
				logger.info("Exception while getting site details for "+ siteId, e);
				response.setMessage("Exception while getting site details for "+ siteId);
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);

			}
		}

		logger.info("Exit SiteController .. getSelectedSite");
		return responseEntity;
	}
}
