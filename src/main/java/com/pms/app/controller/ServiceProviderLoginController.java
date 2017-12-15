package com.pms.app.controller;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPLoginVO;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.repositories.TicketAttachmentRepo;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.service.TicketService;
import com.pms.web.service.UserService;
import com.pms.web.util.RestResponse;

@RequestMapping(value = "/sp")
@Controller
public class ServiceProviderLoginController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderLoginController.class);
	
	

	@Autowired
	private TicketService ticketSerice;
	
	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	private TicketAttachmentRepo ticketAttachmentRepo;
	
	@RequestMapping(value = "/login/validator", method = RequestMethod.POST)
	public String validateSPLogin(final HttpSession session, 
			@RequestParam(value="spemail") String spUserName, @RequestParam(value="accesscode") String accessCode, ModelMap model) {
		logger.info("Inside ServiceProviderLoginController .. validateSPLogin");
		RestResponse response = new RestResponse();
		SPLoginVO savedLoginVO=null;
			try {
				savedLoginVO = serviceProviderService.validateServiceProvider(spUserName, accessCode);
				if (savedLoginVO.isValidated()) {
					response.setStatusCode(200);
					/*Authentication springAuthentication = SecurityContextHolder.getContext().getAuthentication();
					UsernamePasswordAuthenticationToken auth = null;
					try {
						List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
					    grantedAuths.add(new SimpleGrantedAuthority("ROLE_SP_EXTERNAL"));
						auth = new UsernamePasswordAuthenticationToken(spEmail, null, grantedAuths);
					    auth.setDetails(savedLoginVO);
					    
						}
						catch (Exception e1) {
							e1.printStackTrace();
					}*/
					
				}else{
					logger.info("Unable to login using Service Provider");
				}
			} catch (Exception e) {
				logger.info("Exception in getting service provider", e);
				response.setMessage("Exception while getting service provider");
			}
		logger.info("Inside ServiceProviderController .. validateSPLogin");
		if(response.getStatusCode()==200){
			session.setAttribute("savedsp", savedLoginVO);
			model.put("savedsp", savedLoginVO);
			return "redirect:/sp/incident/details";
		}else{
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/session/user", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> loggedInSP(final HttpServletRequest request, final HttpSession session) {
		logger.info("Inside ServiceProviderLoginController .. loggedInSP");
	
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
		if (savedLoginVO.getSpId()!=null) {
			response.setStatusCode(200);
			response.setObject(savedLoginVO);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
		} else {
			response.setStatusCode(404);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
		}
		}catch (Exception e) {
			response.setStatusCode(500);
			logger.info("Exception in getting service provider details", e);
		}
		
		logger.info("Exit ServiceProviderLoginController .. loggedInSP");
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/incident/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO!=null) {
			model.put("savedsp", savedLoginVO);
			return "sp.incident.details";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/incident/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllTickets(final HttpSession session) {
		List<TicketVO> tickets = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO.getSpId()!=null) {
				tickets = ticketSerice.getCustomerTicketsBySP(savedLoginVO.getSpId());
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


	@RequestMapping(value = "/incident/details/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO.getSpId()!=null) {
			model.put("savedsp", savedLoginVO);
				model.put("mode", "EDIT");
				return "sp.incident.update";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/incident/details/view", method = RequestMethod.GET)
	public String incidentDetailsView(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO.getSpId()!=null) {
			model.put("savedsp", savedLoginVO);
				model.put("mode", "EDIT");
				return "sp.incident.view";
		} else {
			return "redirect:/";
		}
	}
	
	
	
	@RequestMapping(value = "/selected/ticket", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> getSelectedTicket(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketVO ticketVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO.getSpId()!=null) {
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
					
					List<TicketAttachment> fileAttachments = ticketAttachmentRepo.findByTicketNumber(ticketVO.getTicketNumber());
					if(fileAttachments==null){
						logger.info("Not Ticket Attachment for "+ ticketVO.getTicketNumber());
					}else{
						if(fileAttachments.isEmpty()){
							logger.info("Not Ticket Attachment for "+ ticketVO.getTicketNumber());
						}else{
							List<TicketAttachment> fileAttachmentList = new ArrayList<TicketAttachment>();
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
								for(TicketAttachment ticketAttachment : fileAttachments){
									ticketAttachment.setCreatedDate(formatter.format(ticketAttachment.getCreatedOn()));
									fileAttachmentList.add(ticketAttachment);
								}
								ticketVO.setAttachments(fileAttachmentList);
						}
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
	

	
	@RequestMapping(value = "/session/ticket/update", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> incidentSessionTicket(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO.getSpId()!=null) {
			model.put("savedsp", savedLoginVO);
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




	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getSelectedTicket(@PathVariable(value="ticketId") Long ticketId,final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO!=null) {
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
	public ResponseEntity<RestResponse> getPriorityAndSLA(@PathVariable(value="spId") Long spId, 
			@PathVariable(value="categoryId") Long categoryId,
			final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO!=null) {
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
/*	
	@RequestMapping(value = "/escalate", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> escalate(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketEscalationVO ticketEscalationLevels) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO.getSpId()!=null) {
				TicketEscalationVO savedTicketEscalation = ticketSerice.saveTicketEscalations(ticketEscalationLevels, loginUser);
					if(savedTicketEscalation.getEscId()!=null){
						response.setStatusCode(200);
						response.setObject(savedTicketEscalation);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
				}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}

		return responseEntity;
	}*/
	
	@RequestMapping(value = "/linkedticket/{custticket}/{custticketnumber}/{linkedticket}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> escalate(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="custticket") Long custTicket, @PathVariable (value="custticketnumber") String custTicketNumber,
			@PathVariable (value="linkedticket") String linkedTicket) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO!=null) {
				CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveSPLinkedTicket(custTicket,custTicketNumber, linkedTicket, savedLoginVO.getEmail());
					if(savedTicketLinked.getId()!=null){
						response.setStatusCode(200);
						response.setObject(savedTicketLinked);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception while saving link ticket", e);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/linkedticket/list/{custTicketId}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> listLinkedTickets(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @PathVariable (value="custTicketId") Long custTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO!=null) {
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
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO!=null) {
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
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO!=null) {
				ticketSerice.deleteLinkedTicket(linkedTicketId, savedLoginVO.getEmail());
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
/*	
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
	*/
	
	@RequestMapping(value = "/incident/comment/save", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> comment(final ModelMap model,final HttpServletRequest request, 
			final HttpSession session, @RequestBody TicketCommentVO ticketCommentVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
			if (savedLoginVO!=null) {
				TicketCommentVO savedComment = ticketSerice.saveSPTicketComment(ticketCommentVO, savedLoginVO);
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
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO!=null) {
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
	
	@RequestMapping(value = "/incident/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateIncident(final Locale locale, final ModelMap model,
			@RequestBody final TicketVO ticketVO, final HttpSession session) {
		logger.info("Inside ServiceProviderLoginController .. updateIncident");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		TicketVO savedTicketVO = null;
		RestResponse emailResponse =null;
		if(savedLoginVO!=null){
			try {
				logger.info("TicektVO : "+ ticketVO);
				savedTicketVO = ticketSerice.saveOrUpdate(ticketVO, null, savedLoginVO);
			   if(savedTicketVO.getTicketId()!= null && savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){
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
			
		}

		logger.info("Exit ServiceProviderLoginController .. updateIncident");
		return responseEntity;
	}

}
