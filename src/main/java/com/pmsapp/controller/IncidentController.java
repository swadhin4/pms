/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pmsapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

import com.jpa.entities.SPEscalationLevels;
import com.jpa.repositories.SPEscalationLevelRepo;
import com.pmsapp.view.vo.CustomerSPLinkedTicketVO;
import com.pmsapp.view.vo.EscalationLevelVO;
import com.pmsapp.view.vo.LoginUser;
import com.pmsapp.view.vo.ServiceProviderVO;
import com.pmsapp.view.vo.TicketCommentVO;
import com.pmsapp.view.vo.TicketEscalationVO;
import com.pmsapp.view.vo.TicketHistoryVO;
import com.pmsapp.view.vo.TicketPrioritySLAVO;
import com.pmsapp.view.vo.TicketVO;
import com.web.service.ApplicationService;
import com.web.service.AssetService;
import com.web.service.EmailService;
import com.web.service.ServiceProviderService;
import com.web.service.TicketService;
import com.web.service.UserService;
import com.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/incident")
@Controller
public class IncidentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private EmailService emailService;


	@Autowired
	private AssetService assetService;

	@Autowired
	private TicketService ticketSerice;
	
	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@Autowired
	private SPEscalationLevelRepo spEscalationRepo;



	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				return "incident.details";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/details/create", method = RequestMethod.GET)
	public String incidentDetailsCreate(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "NEW");
				return "incident.create";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> createNewIncident(final Locale locale, final ModelMap model,
			@RequestBody final TicketVO ticketVO, final HttpSession session) {
		logger.info("Inside IncidentController .. createNewIncident");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		TicketVO savedTicketVO = null;
		RestResponse emailResponse =null;
		if(loginUser!=null){
			try {
				logger.info("TicektVO : "+ ticketVO);
				savedTicketVO = ticketSerice.saveOrUpdate(ticketVO, loginUser);
				if(savedTicketVO.getTicketId()!= null && savedTicketVO.getMessage().equalsIgnoreCase("CREATED")){
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("New Incident created successfully");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}else if(savedTicketVO.getTicketId()!= null && savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("Incident updated successfully");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while creating an incident");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			}
			
			if(response.getStatusCode()==200 && savedTicketVO.getMessage().equalsIgnoreCase("CREATED")){
				try {
					 emailResponse = emailService.successTicketCreationSPEmail(savedTicketVO, "CREATED");
				 } catch (Exception e) {
					 logger.info("Exception in sending incident creation mail", e);
				}

			}/*else if(response.getStatusCode()==200 && savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){

			}
			/*else if(response.getStatusCode()==200 && savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){

				try {
					 emailResponse = emailService.successTicketCreationSPEmail(savedTicketVO, "UPDATED");
				 } catch (Exception e) {
					 logger.info("Exception in sending incident update mail", e);
				}
			}*/
		}

		logger.info("Exit SiteController .. createNewIncident");
		return responseEntity;
	}

	@RequestMapping(value = "/selected/ticket", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> getSelectedTicket(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketVO ticketVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
					ServiceProviderVO serviceProviderVO = serviceProviderService.findServiceProvider(ticketVO.getAssignedTo());
					if(StringUtils.isNotBlank(serviceProviderVO.getHelpDeskEmail())){
					ticketVO.setAssignedSPEmail(serviceProviderVO.getHelpDeskEmail());
					}
					List<EscalationLevelVO> escalationLevelVOs = serviceProviderVO.getEscalationLevelList();
					if(escalationLevelVOs.isEmpty()){
						
					}else{
						List<EscalationLevelVO> finalEscalationList = new ArrayList<EscalationLevelVO>();
						for(EscalationLevelVO escalationVO:escalationLevelVOs){
							TicketEscalationVO ticketEscalationVO = ticketSerice.getEscalationStatus(ticketVO.getTicketId(), escalationVO.getEscId());
							EscalationLevelVO tempEscalationVO = new EscalationLevelVO();
							if(ticketEscalationVO.getCustEscId()!=null){
								tempEscalationVO.setStatus("Escalated");
							}
							tempEscalationVO.setEscId(escalationVO.getEscId());
							tempEscalationVO.setEscalationEmail(escalationVO.getEscalationEmail());
							tempEscalationVO.setEscalationLevel(escalationVO.getEscalationLevel());
							tempEscalationVO.setLevelId(escalationVO.getLevelId());
							tempEscalationVO.setServiceProdviderId(escalationVO.getServiceProdviderId());
							tempEscalationVO.setEscalationPerson(escalationVO.getEscalationPerson());
							finalEscalationList.add(tempEscalationVO);
						}
						
						ticketVO.setEscalationLevelList(finalEscalationList);
					}
					List<CustomerSPLinkedTicketVO> customerLinkedTickets = ticketSerice.getAllLinkedTickets(ticketVO.getTicketId());
					if(!customerLinkedTickets.isEmpty()){
						ticketVO.setLinkedTickets(customerLinkedTickets);
					}
					
					List<TicketCommentVO> selectedTicketComments=ticketSerice.getTicketComments(ticketVO.getTicketId());
					if(!selectedTicketComments.isEmpty()){
						ticketVO.setTicketComments(selectedTicketComments);
					}
				 	session.setAttribute("selectedTicket", ticketVO);
					response.setStatusCode(200);
					response.setObject(ticketVO);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception in getting ticket response", e);
		}

		return responseEntity;
	}
	
	
	@RequestMapping(value = "/details/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "EDIT");
				return "incident.update";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/details/view", method = RequestMethod.GET)
	public String incidentDetailsView(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "EDIT");
				return "incident.view";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/session/ticket/update", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> incidentSessionTicket(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser!=null) {
			model.put("user", loginUser);
				TicketVO selectedTicketVO=(TicketVO)session.getAttribute("selectedTicket");
				if(selectedTicketVO!=null){
					response.setStatusCode(200);
					response.setObject(selectedTicketVO);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
				else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}
		}
		return responseEntity;
	}


	@RequestMapping(value = "/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllTickets(final HttpSession session) {
		List<TicketVO> tickets = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				tickets = ticketSerice.getAllCustomerTickets(loginUser);
				if (tickets.isEmpty()) {
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NO_CONTENT);
					return responseEntity;
				}else{
					response.setStatusCode(200);
					response.setObject(tickets);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			logger.info("Exception in getting ticket list response", e);
		}

		return responseEntity;
	}



	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSelectedTicket(@PathVariable(value="ticketId") Long ticketId,final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				TicketVO selectedTicket = ticketSerice.getSelectedTicket(ticketId);
				if(selectedTicket.getTicketId()!= null){
					response.setStatusCode(200);
					response.setObject(selectedTicket);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}else{
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while getting ticket");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);

			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/priority/sla/{spId}/{categoryId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getPriorityAndSLA(@PathVariable(value="spId") Long spId, @PathVariable(value="categoryId") Long categoryId,
			final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				TicketPrioritySLAVO ticketPrioritySLAVO = ticketSerice.getTicketPriority(spId, categoryId);
				if(ticketPrioritySLAVO.getPriorityId()!= null){
					response.setStatusCode(200);
					response.setObject(ticketPrioritySLAVO);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}else{
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while getting Priority");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);

			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/escalate", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> escalate(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketEscalationVO ticketEscalationLevels) {
		logger.info("Inside IncidentController .. escalate");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		TicketEscalationVO savedTicketEscalation =null;
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				savedTicketEscalation = ticketSerice.saveTicketEscalations(ticketEscalationLevels, loginUser);
					if(savedTicketEscalation.getEscId()!=null){
						response.setStatusCode(200);
						response.setObject(savedTicketEscalation);
						response.setMessage("Incident "+savedTicketEscalation.getTicketNumber()+ "escalated to "+savedTicketEscalation.getEscLevelDesc());
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}
		if(response.getStatusCode() == 200){
			List<String> escCCMailList = new ArrayList<String>(0);
			SPEscalationLevels spEscalationLevel = null;
			List<EscalationLevelVO> escalationLevelVOs = ticketEscalationLevels.getTicketData().getEscalationLevelList();
			
			if(escalationLevelVOs.size()==0){
				logger.info("No escalation list available");
			}else{
				logger.info("Escalation Level list : "+  escalationLevelVOs.size());
				spEscalationLevel = spEscalationRepo.findOne(savedTicketEscalation.getEscId());
				for(EscalationLevelVO escLevelVO: escalationLevelVOs){
					if(StringUtils.isNotBlank(escLevelVO.getStatus())){
						escCCMailList.add(escLevelVO.getEscalationEmail());
					}
				}
				logger.info("escCCMailList :" + escCCMailList);
			}
			
			if(spEscalationLevel!=null){
				String ccEscList= "";
				if(!escCCMailList.isEmpty()){	
					ccEscList= StringUtils.join(escCCMailList, ',');
					logger.info("Escalation To List : "+  spEscalationLevel.getEscalationEmail());
					logger.info("Escalation CC List : "+  ccEscList);
					
				}
				try {
					emailService.successEscalationLevel(ticketEscalationLevels.getTicketData(), spEscalationLevel, ccEscList, savedTicketEscalation.getEscLevelDesc());
				} catch (Exception e) {
					logger.info("Exception while sending email", e);
				}
				
			}else{
				logger.info("No ticket escalated for SP");
			}
		}
		
		logger.info("Exit IncidentController .. escalate");
		return responseEntity;
	}
	
	@RequestMapping(value = "/linkedticket/{custticket}/{custticketnumber}/{linkedticket}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> linked(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="custticket") Long custTicket, @PathVariable (value="custticketnumber") String custTicketNumber,
			@PathVariable (value="linkedticket") String linkedTicket) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveLinkedTicket(custTicket,custTicketNumber, linkedTicket, loginUser);
					if(savedTicketLinked.getId()!=null){
						response.setStatusCode(200);
						response.setObject(savedTicketLinked);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/linkedticket/list/{custTicketId}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> listLinkedTickets(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="custTicketId") Long custTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				List<CustomerSPLinkedTicketVO> linkedTickets = ticketSerice.getAllLinkedTickets(custTicketId);
						response.setStatusCode(200);
						TicketVO ticketVO = (TicketVO)session.getAttribute("selectedTicket");
						ticketVO.getLinkedTickets().clear();
						ticketVO.setLinkedTickets(linkedTickets);
						session.setAttribute("selectedTicket", ticketVO);
						response.setObject(ticketVO);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while getting listLinkedTickets", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/linkedticket/status/{linkedTicket}/{status}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> changeStatusLinkedTicket(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="linkedTicket") Long linkedTicket) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
						CustomerSPLinkedTicketVO linkedTicketStatus = ticketSerice.changeLinkedTicketStatus(linkedTicket,"CLOSE");
						if(linkedTicketStatus.getClosedFlag().equalsIgnoreCase("CLOSED")){
							response.setStatusCode(200);
							TicketVO ticketVO = (TicketVO)session.getAttribute("selectedTicket");
							ticketVO.getLinkedTickets().clear();
							ticketVO.setLinkedTickets(ticketVO.getLinkedTickets());
							session.setAttribute("selectedTicket", ticketVO);
							response.setObject(ticketVO);
							responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
						}
					}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while getting listLinkedTickets", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/linkedticket/delete/{linkedTicketId}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> deleteLinkedTicket(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="linkedTicketId") Long linkedTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				ticketSerice.deleteLinkedTicket(linkedTicketId);
				response.setStatusCode(200);
				TicketVO sessionTicket = (TicketVO) session.getAttribute("selectedTicket");
				List<CustomerSPLinkedTicketVO> customerLinkedTickets = sessionTicket.getLinkedTickets();
				boolean isAvailable =false;
				CustomerSPLinkedTicketVO foundTicket = null;
				for(CustomerSPLinkedTicketVO custTicket:customerLinkedTickets){
					if(custTicket.getId().equals(linkedTicketId)){
						foundTicket = custTicket;
						isAvailable=true;
						break;
					}
				}
				boolean isRemoved = false;
				if(isAvailable){
					isRemoved = customerLinkedTickets.remove(foundTicket);
				}
				if(isRemoved){
					response.setObject(customerLinkedTickets);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while deleting", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/history/{ticketId}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> incidentSessionTicket(final ModelMap model,@PathVariable (value="ticketId") Long ticketId,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser!=null) {
			model.put("user", loginUser);
				List<TicketHistoryVO> selectedTicketHistory=ticketSerice.getTicketHistory(ticketId);
				if(!selectedTicketHistory.isEmpty()){
					response.setStatusCode(200);
					response.setObject(selectedTicketHistory);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
				else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/comment/save", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> comment(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketCommentVO ticketCommentVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				TicketCommentVO savedComment = ticketSerice.saveTicketComment(ticketCommentVO, loginUser);
					if(savedComment.getCommentId()!=null){
						response.setStatusCode(200);
						response.setObject(savedComment);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
				}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while saving comment", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/comment/list/{ticketId}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> commentLint(final ModelMap model,@PathVariable (value="ticketId") Long ticketId,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser!=null) {
				List<TicketCommentVO> selectedTicketComments=ticketSerice.getTicketComments(ticketId);
				if(!selectedTicketComments.isEmpty()){
					response.setStatusCode(200);
					response.setObject(selectedTicketComments);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
				else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}
		}
		return responseEntity;
	}
	
}
