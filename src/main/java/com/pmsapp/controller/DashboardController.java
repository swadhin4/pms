package com.pmsapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jpa.entities.ViewSPPriority;
import com.jpa.entities.ViewSPStatus;
import com.jpa.entities.ViewSitePriority;
import com.jpa.entities.ViewSiteStatus;
import com.jpa.repositories.ViewSPPriorityRepo;
import com.jpa.repositories.ViewSPStatusRepo;
import com.jpa.repositories.ViewSitePriorityRepo;
import com.jpa.repositories.ViewSiteStatusRepo;
import com.pmsapp.view.vo.LoginUser;
import com.web.util.RestResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private ViewSiteStatusRepo viewStatusRepo;
	
	@Autowired
	private ViewSitePriorityRepo viewSitePriorityRepo;
	
	@Autowired
	private ViewSPStatusRepo viewSPStatusRepo;
	
	@Autowired
	private ViewSPPriorityRepo viewSPPriorityRepo;
	

	@RequestMapping(value = "/site/ticket/status", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSiteTicketStatusById(final HttpSession session) {
		LOGGER.info("Inside DashboardController .. getSiteStatusById");
		List<ViewSiteStatus> siteticketStatusList = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				siteticketStatusList = viewStatusRepo.findAll();
				if(!siteticketStatusList.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(siteticketStatusList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No Ticket Status found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(505);
				response.setMessage("Exception in getting response");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				LOGGER.info("Exception in getting response", e);

			}
		}

		LOGGER.info("Exit DashboardController .. getSiteStatusById");
		return responseEntity;
	}
	
	@RequestMapping(value = "/site/ticket/priority", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSiteTicketPriority(final HttpSession session) {
		LOGGER.info("Inside DashboardController .. getSiteTicketPriority");
		List<ViewSitePriority> siteTicketPriority = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				siteTicketPriority = viewSitePriorityRepo.findAll();
				if(!siteTicketPriority.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(siteTicketPriority);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No Site Priority found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(505);
				response.setMessage("Exception in getting response");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				LOGGER.info("Exception in getting response", e);

			}
		}

		LOGGER.info("Exit DashboardController .. getSiteTicketPriority");
		return responseEntity;
	}

	@RequestMapping(value = "/sp/ticket/priority", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSPTicketPriority(final HttpSession session) {
		LOGGER.info("Inside DashboardController .. getSPTicketPriority");
		List<ViewSPPriority> spTicketPriority = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				spTicketPriority = viewSPPriorityRepo.findAll();
				if(!spTicketPriority.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(spTicketPriority);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No SP Priority found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(505);
				response.setMessage("Exception in getting response");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				LOGGER.info("Exception in getting response", e);

			}
		}

		LOGGER.info("Exit DashboardController .. getSPTicketPriority");
		return responseEntity;
	}
	
	@RequestMapping(value = "/sp/ticket/status", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSPTicketStatus(final HttpSession session) {
		LOGGER.info("Inside DashboardController .. getSPTicketStatus");
		List<ViewSPStatus> spTicketStatus = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				spTicketStatus = viewSPStatusRepo.findAll();
				if(!spTicketStatus.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(spTicketStatus);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No SP Status found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(505);
				response.setMessage("Exception in getting response");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				LOGGER.info("Exception in getting response", e);

			}
		}

		LOGGER.info("Exit DashboardController .. getSPTicketStatus");
		return responseEntity;
	}
}
