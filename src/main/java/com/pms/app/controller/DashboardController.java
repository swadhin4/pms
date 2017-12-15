package com.pms.app.controller;

import java.util.ArrayList;
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

import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.UserSiteAccess;
import com.pms.jpa.entities.ViewSPPriority;
import com.pms.jpa.entities.ViewSPStatus;
import com.pms.jpa.entities.ViewSitePriority;
import com.pms.jpa.entities.ViewSiteStatus;
import com.pms.jpa.repositories.ServiceProviderRepo;
import com.pms.jpa.repositories.UserSiteAccessRepo;
import com.pms.jpa.repositories.ViewSPPriorityRepo;
import com.pms.jpa.repositories.ViewSPStatusRepo;
import com.pms.jpa.repositories.ViewSitePriorityRepo;
import com.pms.jpa.repositories.ViewSiteStatusRepo;
import com.pms.web.util.RestResponse;

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
	
	@Autowired
	private UserSiteAccessRepo userSiteAccessRepo;
	

	@Autowired
	private ServiceProviderRepo serviceProvderRepo;
	
	@RequestMapping(value = "/site/ticket/status", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSiteTicketStatusById(final HttpSession session) {
		LOGGER.info("Inside DashboardController .. getSiteStatusById");
		List<ViewSiteStatus> siteticketStatusList = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				List<UserSiteAccess> userSiteAccessList = userSiteAccessRepo.findSiteAssignedFor(loginUser.getUserId());
				if(userSiteAccessList.isEmpty()){
					LOGGER.info("User donot have any access to sites");
				}else{
					LOGGER.info("User is having access to "+userSiteAccessList.size()+" sites");
					List<Long> siteIdList = new ArrayList<Long>();
					for(UserSiteAccess userSiteAccess: userSiteAccessList){
						siteIdList.add(userSiteAccess.getSite().getSiteId());
					}
					siteticketStatusList = viewStatusRepo.findBySiteIdIn(siteIdList);
					if(!siteticketStatusList.isEmpty()){				
						response.setStatusCode(200);
						response.setObject(siteticketStatusList);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}else{
						response.setStatusCode(404);
						response.setMessage("No Ticket Status found");
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
					}
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
				List<UserSiteAccess> userSiteAccessList = userSiteAccessRepo.findSiteAssignedFor(loginUser.getUserId());
				if(userSiteAccessList.isEmpty()){
					LOGGER.info("User donot have any access to sites");
				}else{
					LOGGER.info("User is having access to "+userSiteAccessList.size()+" sites");
					List<Long> siteIdList = new ArrayList<Long>();
					for(UserSiteAccess userSiteAccess: userSiteAccessList){
						siteIdList.add(userSiteAccess.getSite().getSiteId());
					}
				siteTicketPriority = viewSitePriorityRepo.findBySiteIdIn(siteIdList);
				if(!siteTicketPriority.isEmpty()){				
					response.setStatusCode(200);
					response.setObject(siteTicketPriority);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					response.setMessage("No Site Priority found");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
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
				List<Long> spList = new ArrayList<>(0);
				List<ServiceProvider> serviceProviderList =  serviceProvderRepo.findByCompany(loginUser.getCompany().getCompanyId());
				if(!serviceProviderList.isEmpty()){
					for(ServiceProvider serviceProvider:serviceProviderList){
						spList.add(serviceProvider.getServiceProviderId());
					}
						spTicketPriority = viewSPPriorityRepo.findBySpIdIn(spList);
						if(!spTicketPriority.isEmpty()){				
							response.setStatusCode(200);
							response.setObject(spTicketPriority);
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}else{
							response.setStatusCode(404);
							response.setMessage("No SP Priority found");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
					}
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
				List<Long> spList = new ArrayList<>(0);
				List<ServiceProvider> serviceProviderList =  serviceProvderRepo.findByCompany(loginUser.getCompany().getCompanyId());
				if(!serviceProviderList.isEmpty()){
					for(ServiceProvider serviceProvider:serviceProviderList){
						spList.add(serviceProvider.getServiceProviderId());
					}
					spTicketStatus = viewSPStatusRepo.findBySpIdIn(spList);
					if(!spTicketStatus.isEmpty()){				
						response.setStatusCode(200);
						response.setObject(spTicketStatus);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}else{
						response.setStatusCode(404);
						response.setMessage("No SP Status found");
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
					}
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
