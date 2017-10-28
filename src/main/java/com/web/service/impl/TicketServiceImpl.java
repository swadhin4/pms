package com.web.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.Asset;
import com.jpa.entities.CustomerSPLinkedTicket;
import com.jpa.entities.CustomerTicket;
import com.jpa.entities.ServiceProvider;
import com.jpa.entities.ServiceProviderSLADetails;
import com.jpa.entities.Site;
import com.jpa.entities.Status;
import com.jpa.entities.TicketCategory;
import com.jpa.entities.TicketComment;
import com.jpa.entities.TicketEscalation;
import com.jpa.entities.TicketHistory;
import com.jpa.entities.TicketPriority;
import com.jpa.entities.TicketPrioritySettings;
import com.jpa.entities.User;
import com.jpa.entities.UserSiteAccess;
import com.jpa.repositories.AssetRepo;
import com.jpa.repositories.CustomerSPLinkedTicketRepo;
import com.jpa.repositories.CustomerTicketRepo;
import com.jpa.repositories.GenericQueryExecutorDAO;
import com.jpa.repositories.JDBCQueryDAO;
import com.jpa.repositories.ServiceProviderRepo;
import com.jpa.repositories.ServiceProviderSLARepo;
import com.jpa.repositories.SiteRepo;
import com.jpa.repositories.StatusRepo;
import com.jpa.repositories.TicketCategoryRepo;
import com.jpa.repositories.TicketCommentRepo;
import com.jpa.repositories.TicketEscalationRepo;
import com.jpa.repositories.TicketHistoryRepo;
import com.jpa.repositories.TicketPriorityRepo;
import com.jpa.repositories.TicketPrioritySettingsRepo;
import com.jpa.repositories.UserDAO;
import com.jpa.repositories.UserSiteAccessRepo;
import com.pmsapp.view.vo.CustomerSPLinkedTicketVO;
import com.pmsapp.view.vo.CustomerTicketVO;
import com.pmsapp.view.vo.LoginUser;
import com.pmsapp.view.vo.SPLoginVO;
import com.pmsapp.view.vo.TicketCommentVO;
import com.pmsapp.view.vo.TicketEscalationVO;
import com.pmsapp.view.vo.TicketHistoryVO;
import com.pmsapp.view.vo.TicketPrioritySLAVO;
import com.pmsapp.view.vo.TicketVO;
import com.web.service.TicketService;
import com.web.util.RandomUtils;

@Service("ticketService")
public class TicketServiceImpl implements TicketService {

	private final static Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);

	@Autowired
	private CustomerTicketRepo customerTicketRepo;
	
	@Autowired
	private SiteRepo siteRepo;
	
	@Autowired
	private AssetRepo assetRepo;
	
	@Autowired
	private TicketCategoryRepo ticketCategoryRepo;
	
	@Autowired
	private ServiceProviderRepo serviceProviderRepo;
	
	@Autowired
	private TicketPrioritySettingsRepo ticketSettingsRepo;
	
	@Autowired
	private TicketPriorityRepo ticketPriorityRepo;
	
	@Autowired
	private ServiceProviderSLARepo spSLARepo;

	@Autowired
	private StatusRepo statusRepo;
	
	@Autowired
	private GenericQueryExecutorDAO genericQueryExecutorDAO;
	
	@Autowired
	private CustomerSPLinkedTicketRepo customerLinkedRepo;
	
	@Autowired
	private TicketEscalationRepo ticketEscalationRepo;
	
	@Autowired
	private TicketHistoryRepo ticketHistoryRepo;
	
	@Autowired
	private TicketCommentRepo ticketCommentRepo;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private JDBCQueryDAO jdbcQueryDAO;
	
	@Autowired
	private UserSiteAccessRepo userSiteAccessRepo;
	
	@Override
	public TicketVO saveOrUpdate(final TicketVO customerTicketVO, LoginUser user) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - saveOrUpdate");
		CustomerTicket customerTicket = null;
		if(customerTicketVO.getTicketId()==null){
			customerTicket = new CustomerTicket();
			String ticketNumber = "INC00" + RandomUtils.randomAlphanumeric(3) + Calendar.getInstance().getTimeInMillis();
			LOGGER.info("Ticket Number Generated : " + ticketNumber);
			customerTicket.setTicketNumber(ticketNumber);
			TicketCategory category = ticketCategoryRepo.findOne(customerTicketVO.getCategoryId());
			customerTicket.setTicketCategory(category);
		}else {
			customerTicket = customerTicketRepo.findOne(customerTicketVO.getTicketId());
			if(customerTicketVO.getStatusId() == 6){
				customerTicket.setCloseCode(customerTicketVO.getCloseCode());
				customerTicket.setClosedBy(user.getUsername());
				customerTicket.setCloseNote(customerTicketVO.getCloseNote());
				
				customerTicket.setClosedOn(new Date());
			}
			TicketCategory category = ticketCategoryRepo.findOne(customerTicketVO.getCategoryId());
			category.setId(customerTicketVO.getCategoryId());
			customerTicket.setTicketCategory(category);
			customerTicket.setModifiedBy(user.getUsername());
			customerTicket.setModifiedOn(new Date());
		}	
			if(StringUtils.isNotBlank(customerTicketVO.getTicketTitle())){
				customerTicket.setTicketTitle(customerTicketVO.getTicketTitle());
			}
			
			if(StringUtils.isNotBlank(customerTicketVO.getDescription())){
				customerTicket.setTicketDesc(customerTicketVO.getDescription());
			}
			
			Site site = siteRepo.findOne(customerTicketVO.getSiteId());
			customerTicket.setSite(site);
			
			Asset asset = assetRepo.findOne(customerTicketVO.getAssetId());
			customerTicket.setAsset(asset);
			
			ServiceProvider serviceProvider = serviceProviderRepo.findOne(asset.getServiceProviderId());
			
			customerTicket.setAssignedTo(serviceProvider);
			customerTicket.setPriority(customerTicketVO.getPriorityDescription());
			customerTicket.setCreatedBy(user.getUsername());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date oldFormat=null;
			try {
				oldFormat = simpleDateFormat.parse(customerTicketVO.getTicketStartTime());
			} catch (ParseException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			customerTicket.setTicketStarttime(oldFormat);
					
			Status status = statusRepo.findOne(customerTicketVO.getStatusId());
			if(status!=null){
			customerTicket.setStatus(status);
			}
			
			customerTicket = customerTicketRepo.save(customerTicket);
			LOGGER.info("Customer Ticket : "+ customerTicket);
		
		if(customerTicket.getId()!=null && customerTicket.getModifiedOn()==null){
			LOGGER.info("New Ticket saved successfully with ticket number as "+customerTicket.getTicketNumber());
			customerTicketVO.setTicketId(customerTicket.getId());
			customerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
			customerTicketVO.setAssignedSPEmail(serviceProvider.getHelpDeskEmail());
			customerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
			customerTicketVO.setStatus(customerTicket.getStatus().getStatus());
			String slaDueDate = jdbcQueryDAO.getSlaDate(customerTicket.getTicketNumber(), customerTicketVO.getDuration(), customerTicketVO.getUnit());
			LOGGER.info("SLA Date for the Ticket is : "+  slaDueDate);
			if(StringUtils.isNotBlank(slaDueDate)){
			customerTicketVO.setSla(slaDueDate);
			}
			customerTicketVO.setMessage("CREATED");
			
		}else if(customerTicket.getId()!=null && customerTicket.getModifiedOn()!=null){
			LOGGER.info("Ticket "+ customerTicket.getTicketNumber() +" updated successfully");
			customerTicketVO.setTicketId(customerTicket.getId());
			customerTicketVO.setAssignedSPEmail(serviceProvider.getHelpDeskEmail());
			customerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
			customerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
			customerTicketVO.setStatus(customerTicket.getStatus().getStatus());
			customerTicketVO.setMessage("UPDATED");
		}
		LOGGER.info("Exit TicketServiceImpl - saveOrUpdate");
		return customerTicketVO;
	}

	@Override
	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllCustomerTickets");
		List<TicketVO> customerTicketList = new ArrayList<TicketVO>();
		LOGGER.info("Getting Asset List for logged in user : "+  loginUser.getFirstName() + "" + loginUser.getLastName());
		List<UserSiteAccess> userSiteAccessList = userSiteAccessRepo.findSiteAssignedFor(loginUser.getUserId());
		if(userSiteAccessList.isEmpty()){
			LOGGER.info("User donot have any access to sites");
		}else{
			LOGGER.info("User is having access to "+userSiteAccessList.size()+" sites");
			List<Long> siteIdList = new ArrayList<Long>();
			for(UserSiteAccess userSiteAccess: userSiteAccessList){
				siteIdList.add(userSiteAccess.getSite().getSiteId());
			}
		List<CustomerTicket> savedCustomerTicketList = customerTicketRepo.findBySiteSiteIdIn(siteIdList);
		if(!savedCustomerTicketList.isEmpty()){
			for(CustomerTicket customerTicket:savedCustomerTicketList){
				TicketVO tempCustomerTicketVO=new TicketVO();
				tempCustomerTicketVO.setTicketId(customerTicket.getId());
				tempCustomerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
				tempCustomerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
				tempCustomerTicketVO.setDescription(customerTicket.getTicketDesc());
				tempCustomerTicketVO.setSiteId(customerTicket.getSite().getSiteId());
				tempCustomerTicketVO.setSiteName(customerTicket.getSite().getSiteName());
				tempCustomerTicketVO.setAssetId(customerTicket.getAsset().getAssetId());
				tempCustomerTicketVO.setAssetName(customerTicket.getAsset().getAssetName());
				tempCustomerTicketVO.setCategoryId(customerTicket.getTicketCategory().getId());
				tempCustomerTicketVO.setCategoryName(customerTicket.getTicketCategory().getDescription());
				tempCustomerTicketVO.setAssignedSP(customerTicket.getAssignedTo().getName());
				tempCustomerTicketVO.setAssignedTo(customerTicket.getAssignedTo().getServiceProviderId());
				tempCustomerTicketVO.setPriorityDescription(customerTicket.getPriority());
				tempCustomerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
				tempCustomerTicketVO.setStatus(customerTicket.getStatus().getStatus());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:MM:ss");
				tempCustomerTicketVO.setRaisedOn(simpleDateFormat.format(customerTicket.getCreatedOn()));
				tempCustomerTicketVO.setRaisedBy(customerTicket.getCreatedBy());
				tempCustomerTicketVO.setTicketStartTime(simpleDateFormat.format(customerTicket.getTicketStarttime()));
				tempCustomerTicketVO.setSla(simpleDateFormat.format(customerTicket.getSlaDuedate()));
				tempCustomerTicketVO.setCloseCode(customerTicket.getCloseCode());
				
				if(StringUtils.isNotBlank(customerTicket.getClosedBy())){
					tempCustomerTicketVO.setClosedBy(customerTicket.getClosedBy());
				}
				
				if(customerTicket.getClosedOn()!=null){
				tempCustomerTicketVO.setClosedOn(simpleDateFormat.format(customerTicket.getClosedOn()));
				}
				
				if(customerTicket.getCloseNote()!=null){
					tempCustomerTicketVO.setClosedNote(customerTicket.getCloseNote());
				}
				if(customerTicket.getModifiedOn()!=null){
					tempCustomerTicketVO.setModifiedOn(simpleDateFormat.format(customerTicket.getModifiedOn()));
				}
				
				if(StringUtils.isNotBlank(customerTicket.getModifiedBy())){
					tempCustomerTicketVO.setModifiedBy(customerTicket.getModifiedBy());
				}
				Date slaDueDate = customerTicket.getSlaDuedate();
				Date creationTime = customerTicket.getCreatedOn();
				if(!customerTicket.getStatus().getStatus().equalsIgnoreCase("Closed")){
					Date currentDateTime = new Date();
					long numeratorDiff = currentDateTime.getTime() - creationTime.getTime(); 
					long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
					double slaPercent=0.0;
					
					double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
					double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
					if (deNumValue != 0){
						slaPercent = Math.round((numValue / deNumValue) * 100);
					}	
					tempCustomerTicketVO.setSlaPercent(slaPercent);
				}else{
					Date closedTime = customerTicket.getClosedOn();
					long numeratorDiff = closedTime.getTime() - creationTime.getTime(); 
					long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
					double slaPercent =0.0; 
					double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
					double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
					if (deNumValue != 0){
						slaPercent = Math.round((numValue / deNumValue) * 100);
					}
					tempCustomerTicketVO.setSlaPercent(slaPercent);
				}
				
				customerTicketList.add(tempCustomerTicketVO);
			}
		}else{
			LOGGER.info("No Tickets available");
			}
		}
		LOGGER.info("Exit TicketServiceImpl - getAllCustomerTickets");
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;
	}

	@Override
	public List<CustomerTicket> getOpenTicketsBySite(final Long siteId)
			throws Exception {
		List<CustomerTicket> customerTicketList = new ArrayList<CustomerTicket>();
		//customerTicketList = customerTicketRepo.findOpenTicketsBySite(siteId);
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerTicket> getTicketsByStatus(final Long statusId)
			throws Exception {
		List<CustomerTicket> customerTicketList = new ArrayList<CustomerTicket>();
		customerTicketList = customerTicketRepo.findOpenTicketsByStatus(statusId);
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;

	}

	@Override
	public CustomerTicketVO getCustomerTicket(final Long ticktId) throws Exception {
		CustomerTicket customerTicket = customerTicketRepo.findTicketById(ticktId);
		CustomerTicketVO customerTicketVO = new CustomerTicketVO();
		if(customerTicket!=null){
			customerTicketVO.setId(customerTicket.getId());
			customerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
			customerTicketVO.setTicketDesc(customerTicket.getTicketDesc());
			customerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
			System.out.println(customerTicket.getAsset().getAssetId());
			//Asset asset = getAssetDetails(customerTicket.getId());
			
			customerTicketVO.setAsset(customerTicket.getAsset());
			customerTicketVO.setAssignedTo(customerTicket.getAssignedTo());
			customerTicketVO.setSite(customerTicket.getSite());
			customerTicketVO.setTicketCategory(customerTicket.getTicketCategory());
			customerTicketVO.setPriority(customerTicket.getPriority());
			/*customerTicketVO.setIsEscalated(customerTicket.getIsEscalated());*/
			customerTicketVO.setTicketStartTime(customerTicket.getTicketStarttime());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
			customerTicketVO.setTicketStartDateAndTime(simpleDateFormat.format(customerTicket.getTicketStarttime()));
			customerTicketVO.setDueSlaDate(simpleDateFormat.format(customerTicket.getSlaDuedate()));
			customerTicketVO.setStatus(customerTicket.getStatus());
			customerTicketVO.setAssignedTo(customerTicket.getAssignedTo());
		}
		return customerTicketVO;
	}

	/*private Asset getAssetDetails(Long id) {
		String assetEjbQl = "from CustomerTicket ct where ct.id="+id
				
		return null;
	}*/

	@Override
	public CustomerTicket saveOrUpdate(final CustomerTicket customerTicket)
			throws Exception {
		return null;
	}

/*	@Override
	public List<CustomerTicketVO> getOpenCustomerTickets() throws Exception {
		List<CustomerTicket> savedCustomerTicketList = customerTicketRepo.findOpenTickets();
		List<CustomerTicketVO> customerTicketList = new ArrayList<CustomerTicketVO>();
		for(CustomerTicket customerTicket:savedCustomerTicketList){
			CustomerTicketVO customerTicketVO =new CustomerTicketVO();
			customerTicketVO.setId(customerTicket.getId());
			customerTicketVO.setSite(customerTicket.getSite());
			customerTicketVO.setAsset(customerTicket.getAsset());
			customerTicketVO.setTicketCategory(customerTicket.getTicketCategory());
			customerTicketVO.setTicketStartTime(customerTicket.getTicketStarttime());
			customerTicketVO.setPriority(customerTicket.getPriority());
			customerTicketVO.setSlaDueDate(customerTicket.getSlaDuedate());
			customerTicketVO.setStatus(customerTicket.getStatus());
			customerTicketVO.setIsEscalated(customerTicket.getIsEscalated());
			customerTicketVO.setAssignedTo(customerTicket.getAssignedTo());
			customerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
			customerTicketVO.setTicketDesc(customerTicket.getTicketDesc());
			customerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
			customerTicketVO.setCreatedOn(customerTicket.getCreatedOn());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
			String createdDate = simpleDateFormat.format(customerTicket.getCreatedOn());
			String slaDueDate = simpleDateFormat.format(customerTicket.getSlaDuedate());
			String ticketStartedTime = simpleDateFormat.format(customerTicket.getTicketStarttime());
			customerTicketVO.setTicketStartDateAndTime(ticketStartedTime);
			customerTicketVO.setCreatedDate(createdDate);
			customerTicketVO.setDueSlaDate(slaDueDate);
			customerTicketList.add(customerTicketVO);
		}
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;
	}*/

	@Override
	public TicketVO getSelectedTicket(Long ticketId) throws Exception {
		CustomerTicket customerTicket = customerTicketRepo.findOne(ticketId);
		TicketVO customerTicketVO = new TicketVO();
		if(customerTicket!=null){
			customerTicketVO.setTicketId(customerTicket.getId());
			customerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
			customerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
			customerTicketVO.setDescription(customerTicket.getTicketDesc());
			
			customerTicketVO.setSiteId(customerTicket.getSite().getSiteId());
			customerTicketVO.setSiteName(customerTicket.getSite().getSiteName());
			customerTicketVO.setAssetId(customerTicket.getAsset().getAssetId());
			customerTicketVO.setAssetName(customerTicket.getAsset().getAssetName());
			customerTicketVO.setCategoryId(customerTicket.getTicketCategory().getId());
			customerTicketVO.setCategoryName(customerTicket.getTicketCategory().getDescription());
			customerTicketVO.setAssignedSP(customerTicket.getAssignedTo().getName());
			customerTicketVO.setAssignedTo(customerTicket.getAssignedTo().getServiceProviderId());
			customerTicketVO.setPriorityDescription(customerTicket.getPriority());
			customerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
			customerTicketVO.setStatus(customerTicket.getStatus().getStatus());
			customerTicketVO.setRaisedBy(customerTicket.getCreatedBy());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			customerTicketVO.setRaisedOn(simpleDateFormat.format(customerTicket.getCreatedOn()));
			
		}
		return customerTicketVO;
	}

	@Override
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId) {
		LOGGER.info("Inside TicketServiceImpl - getTicketPriority");
		TicketPrioritySettings ticketPrioritySettings = ticketSettingsRepo.findByTicketCategoryId(ticketCategoryId);
		TicketPrioritySLAVO ticketPrioritySLAVO = new TicketPrioritySLAVO();
		if(ticketPrioritySettings!=null){
			LOGGER.info("Getting ticket priority for id : "+ ticketPrioritySettings.getPriorityId());
			TicketPriority ticketPriority = ticketPriorityRepo.findOne(ticketPrioritySettings.getPriorityId());
			if(ticketPriority!=null){
				LOGGER.info("Getting SP Details and  priority for : SP - "+ serviceProviderID  + " , P - " +ticketPrioritySettings.getPriorityId());
				ServiceProviderSLADetails spSLADetails = spSLARepo.findByServiceProviderServiceProviderIdAndPriorityId(serviceProviderID, ticketPriority.getPriorityId());
				if(spSLADetails!=null){
					int duration = spSLADetails.getDuration();
					String unit =  spSLADetails.getUnit();
					Date slaDate=null;
					if(unit.equalsIgnoreCase("hours")){
						slaDate = DateUtils.addHours(new Date(), duration);
					}else if(unit.equalsIgnoreCase("days")){
						slaDate = DateUtils.addDays(new Date(), duration);
					}
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");
					ticketPrioritySLAVO.setPriorityId(ticketPriority.getPriorityId());
					ticketPrioritySLAVO.setPriorityName(ticketPriority.getDescription());
					ticketPrioritySLAVO.setServiceProviderId(spSLADetails.getServiceProvider().getServiceProviderId());
					ticketPrioritySLAVO.setServiceProviderName(spSLADetails.getServiceProvider().getName());
					ticketPrioritySLAVO.setTicketCategoryId(ticketPrioritySettings.getTicketCategoryId());
					ticketPrioritySLAVO.setTicketSlaDueDate(simpleDateFormat.format(slaDate));
					ticketPrioritySLAVO.setDuration(duration);
					ticketPrioritySLAVO.setUnits(unit);
				}
			}
		}
		LOGGER.info("Exit TicketServiceImpl - getTicketPriority");
		return ticketPrioritySLAVO;
	}

	@Override
	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) {
		LOGGER.info("Inside TicketServiceImpl - saveTicketEscalations");
		TicketEscalation ticketEscalation = new TicketEscalation();
		TicketEscalationVO savedTicketEscVO = new TicketEscalationVO();
			ticketEscalation.setTicketId(ticketEscalationLevel.getTicketId());
			ticketEscalation.setEscLevelDesc(ticketEscalationLevel.getEscLevelDesc());
			ticketEscalation.setTicketNumber(ticketEscalationLevel.getTicketNumber());
			ticketEscalation.setEscalatedBy(user.getUsername());
			ticketEscalation.setEscLevelId(ticketEscalationLevel.getEscId());
			ticketEscalation = ticketEscalationRepo.save(ticketEscalation);
			if(ticketEscalation.getEscId()!=null){
				savedTicketEscVO.setCustEscId(ticketEscalation.getEscId());
				savedTicketEscVO.setEscLevelDesc(ticketEscalation.getEscLevelDesc());
				savedTicketEscVO.setTicketId(ticketEscalation.getTicketId());
				savedTicketEscVO.setTicketNumber(ticketEscalation.getTicketNumber());
				savedTicketEscVO.setEscId(ticketEscalation.getEscLevelId());
				savedTicketEscVO.setEscalationStatus("Escalated");
			}
		LOGGER.info("Exit TicketServiceImpl - saveTicketEscalations");
		return savedTicketEscVO;
	}
	
	@Override
	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId) {
		List<TicketEscalation> ticketsEscalated = ticketEscalationRepo.findByTicketId(ticketId);
		List<TicketEscalationVO> escalatedTickets = new ArrayList<TicketEscalationVO>();
		if(ticketsEscalated!=null){
			for(TicketEscalation ticketEscalation:ticketsEscalated){
				TicketEscalationVO savedTicketEscVO = new TicketEscalationVO();
				savedTicketEscVO.setEscId(ticketEscalation.getEscId());
				savedTicketEscVO.setEscLevelDesc(ticketEscalation.getEscLevelDesc());
				savedTicketEscVO.setTicketId(ticketEscalation.getTicketId());
				savedTicketEscVO.setTicketNumber(ticketEscalation.getTicketNumber());
				savedTicketEscVO.setEscalationStatus("Escalated");
			escalatedTickets.add(savedTicketEscVO);
			}
		}
		return escalatedTickets == null?Collections.EMPTY_LIST:escalatedTickets;
	}

	@Override
	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, LoginUser user) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - saveLinkedTicket");
		CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
		if(StringUtils.isNotBlank(linkedTicket)){
			CustomerSPLinkedTicket customerSPLinkedTicket = new CustomerSPLinkedTicket();
			customerSPLinkedTicket.setCustTicketId(custTicket);
			customerSPLinkedTicket.setCustTicketNo(custTicketNumber);
			customerSPLinkedTicket.setSpTicketNo(linkedTicket);
			customerSPLinkedTicket.setClosedFlag("OPEN");
			customerSPLinkedTicket.setCreatedBy(user.getUsername());
			customerSPLinkedTicket = customerLinkedRepo.save(customerSPLinkedTicket);
			if(customerSPLinkedTicket.getId()!=null){
				customerSPLinkedTicketVO.setId(customerSPLinkedTicket.getId());
				customerSPLinkedTicketVO.setSpLinkedTicket(customerSPLinkedTicket.getSpTicketNo());
				customerSPLinkedTicketVO.setClosedFlag(customerSPLinkedTicket.getClosedFlag());
			}
		}
		LOGGER.info("Exit TicketServiceImpl - saveLinkedTicket");
		return customerSPLinkedTicketVO;
	}
	
	
	@Override
	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicket) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllLinkedTickets");
		List<CustomerSPLinkedTicket> customerSPLinkedTickets = customerLinkedRepo.findByCustTicketId(custTicket);
		List<CustomerSPLinkedTicketVO> customerSPLinkedTicketVOList = new ArrayList<CustomerSPLinkedTicketVO>();
		if(!customerSPLinkedTickets.isEmpty()){
			for(CustomerSPLinkedTicket customerSPLinkedTicket: customerSPLinkedTickets){
				CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
				customerSPLinkedTicketVO.setId(customerSPLinkedTicket.getId());
				customerSPLinkedTicketVO.setSpLinkedTicket(customerSPLinkedTicket.getSpTicketNo());
				customerSPLinkedTicketVO.setCustTicketId(customerSPLinkedTicket.getCustTicketId().toString());
				customerSPLinkedTicketVO.setCustTicketNumber(customerSPLinkedTicket.getCustTicketNo());
				customerSPLinkedTicketVO.setClosedFlag(customerSPLinkedTicket.getClosedFlag());
				customerSPLinkedTicketVOList.add(customerSPLinkedTicketVO);
			}
		}
		LOGGER.info("Exit TicketServiceImpl - getAllLinkedTickets");
		return customerSPLinkedTicketVOList==null?Collections.EMPTY_LIST:customerSPLinkedTicketVOList;
	}

	@Override
	public void deleteLinkedTicket(Long linkedTicketId) {
		LOGGER.info("Inside TicketServiceImpl - deleteLinkedTicket");
		customerLinkedRepo.delete(linkedTicketId);
		LOGGER.info("Exit TicketServiceImpl - deleteLinkedTicket");
	}

	@Override
	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId) {
		TicketEscalation ticketEscalation = ticketEscalationRepo.findByTicketIdAndEscLevelId(ticketId, escId);
		TicketEscalationVO savedTicketEscVO = new TicketEscalationVO();
		if(ticketEscalation!=null){
			savedTicketEscVO.setCustEscId(ticketEscalation.getEscId());
			savedTicketEscVO.setEscId(ticketEscalation.getEscLevelId());
			savedTicketEscVO.setEscLevelDesc(ticketEscalation.getEscLevelDesc());
			savedTicketEscVO.setTicketId(ticketEscalation.getTicketId());
			savedTicketEscVO.setTicketNumber(ticketEscalation.getTicketNumber());
			savedTicketEscVO.setEscalationStatus("Escalated");
		}
		return savedTicketEscVO;
	}

	@Override
	public List<TicketHistoryVO> getTicketHistory(Long ticketId) {
		CustomerTicket customerTicket = customerTicketRepo.findOne(ticketId);
		
		List<TicketHistory> ticketHistoryDetails=ticketHistoryRepo.findByTicketNumber(customerTicket.getTicketNumber());
		List<TicketHistoryVO> ticketHistoryVOList =  new ArrayList<TicketHistoryVO>();
		if(!ticketHistoryDetails.isEmpty()){
			for(TicketHistory ticketHistory: ticketHistoryDetails){
				TicketHistoryVO ticketHistoryVO = new TicketHistoryVO();
				ticketHistoryVO.setHistoryId(ticketHistory.getHistoryId());
				ticketHistoryVO.setTicketNumber(ticketHistory.getTicketNumber());
				ticketHistoryVO.setAction(ticketHistory.getAction());
				ticketHistoryVO.setMessage(ticketHistory.getMessage());
				ticketHistoryVO.setColumnName(ticketHistory.getColumnName());
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY");
				ticketHistoryVO.setTimeStamp(simpleDateFormat.format(ticketHistory.getTimeStamp()));
				ticketHistoryVO.setValueBefore(ticketHistory.getValueBefore());
				ticketHistoryVO.setValueAfter(ticketHistory.getValueAfter());
				ticketHistoryVO.setWho(ticketHistory.getWho());
				ticketHistoryVOList.add(ticketHistoryVO);
			}
		}
		return ticketHistoryVOList==null?Collections.EMPTY_LIST:ticketHistoryVOList;
	}

	@Override
	public CustomerSPLinkedTicketVO changeLinkedTicketStatus(Long linkedTicket, String string) throws Exception {
		CustomerSPLinkedTicket customerLinkedTicket = customerLinkedRepo.findOne(linkedTicket);
		CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
		if(customerLinkedTicket!=null){
			customerLinkedTicket.setClosedFlag("CLOSED");
			Date closedDate = new Date();
			customerLinkedTicket.setClosedTime(closedDate);
			customerLinkedTicket = customerLinkedRepo.save(customerLinkedTicket);
			customerSPLinkedTicketVO.setClosedFlag(customerLinkedTicket.getClosedFlag());
			customerSPLinkedTicketVO.setId(customerLinkedTicket.getCustTicketId());
			customerSPLinkedTicketVO.setSpLinkedTicket(customerLinkedTicket.getSpTicketNo());
		}
		
		return customerSPLinkedTicketVO;
	}

	@Override
	public List<TicketCommentVO> getTicketComments(Long ticketId) {
		List<TicketComment> commentList = ticketCommentRepo.findByTicketId(ticketId);
		List<TicketCommentVO> commentListVO = new ArrayList<TicketCommentVO>();
		if(!commentList.isEmpty()){
			for(TicketComment  ticketComment : commentList){
				TicketCommentVO ticketCommentVO = new TicketCommentVO();
				ticketCommentVO.setCreatedBy(ticketComment.getCreatedBy());
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY");
				ticketCommentVO.setCreatedDate(simpleDateFormat.format(ticketComment.getCreatedDate()));
				ticketCommentVO.setTicketNumber(ticketComment.getCustTicketNumber());
				ticketCommentVO.setTicketId(ticketComment.getTicketId());
				ticketCommentVO.setCommentId(ticketComment.getCommentId());
				ticketCommentVO.setComment(ticketComment.getComment());
				commentListVO.add(ticketCommentVO);
			}
		}
		return commentListVO == null?Collections.EMPTY_LIST:commentListVO;
	}

	@Override
	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser user) throws Exception {
			TicketComment ticketComment = new TicketComment();
			ticketComment.setCreatedBy(user.getUsername());
			ticketComment.setCustTicketNumber(ticketCommentVO.getTicketNumber());
			ticketComment.setTicketId(ticketCommentVO.getTicketId());
			ticketComment.setComment(ticketCommentVO.getComment());
			ticketComment = ticketCommentRepo.save(ticketComment);
			
			if(ticketComment.getCommentId()!=null){
				ticketCommentVO.setTicketNumber(ticketComment.getCustTicketNumber());
				ticketCommentVO.setTicketId(ticketComment.getTicketId());
				ticketCommentVO.setCommentId(ticketComment.getCommentId());
				ticketCommentVO.setComment(ticketComment.getComment());
				ticketCommentVO.setCreatedBy(ticketComment.getCreatedBy());
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY");
				ticketCommentVO.setCreatedDate(simpleDateFormat.format(ticketComment.getCreatedDate()));
			}
		return ticketCommentVO;
		
	}

	@Override
	public List<TicketVO> getCustomerTicketsBySP(Long spId) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getCustomerTicketsBySP");
		List<TicketVO> customerTicketList = new ArrayList<TicketVO>();
		if (spId != null) {
			ServiceProvider serviceProvider = serviceProviderRepo.findOne(spId);
			if (serviceProvider != null) {
				List<CustomerTicket> savedCustomerTicketList = customerTicketRepo.findByAssignedTo(serviceProvider);
				if (!savedCustomerTicketList.isEmpty()) {
					for (CustomerTicket customerTicket : savedCustomerTicketList) {
						TicketVO tempCustomerTicketVO = new TicketVO();
						tempCustomerTicketVO.setTicketId(customerTicket.getId());
						tempCustomerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
						tempCustomerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
						tempCustomerTicketVO.setDescription(customerTicket.getTicketDesc());
						tempCustomerTicketVO.setSiteId(customerTicket.getSite().getSiteId());
						tempCustomerTicketVO.setSiteName(customerTicket.getSite().getSiteName());
						tempCustomerTicketVO.setAssetId(customerTicket.getAsset().getAssetId());
						tempCustomerTicketVO.setAssetName(customerTicket.getAsset().getAssetName());
						tempCustomerTicketVO.setCategoryId(customerTicket.getTicketCategory().getId());
						tempCustomerTicketVO.setCategoryName(customerTicket.getTicketCategory().getDescription());
						tempCustomerTicketVO.setAssignedSP(customerTicket.getAssignedTo().getName());
						tempCustomerTicketVO.setAssignedTo(customerTicket.getAssignedTo().getServiceProviderId());
						tempCustomerTicketVO.setPriorityDescription(customerTicket.getPriority());
						tempCustomerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
						tempCustomerTicketVO.setStatus(customerTicket.getStatus().getStatus());
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:MM:ss");
						tempCustomerTicketVO.setRaisedOn(simpleDateFormat.format(customerTicket.getCreatedOn()));
						tempCustomerTicketVO.setRaisedBy(customerTicket.getCreatedBy());
						tempCustomerTicketVO
								.setTicketStartTime(simpleDateFormat.format(customerTicket.getTicketStarttime()));
						tempCustomerTicketVO.setSla(simpleDateFormat.format(customerTicket.getSlaDuedate()));

						tempCustomerTicketVO.setCloseCode(customerTicket.getCloseCode());

						if (StringUtils.isNotBlank(customerTicket.getClosedBy())) {
							tempCustomerTicketVO.setClosedBy(customerTicket.getClosedBy());
						}

						if (customerTicket.getClosedOn() != null) {
							tempCustomerTicketVO.setClosedOn(simpleDateFormat.format(customerTicket.getClosedOn()));
						}

						if (customerTicket.getCloseNote() != null) {
							tempCustomerTicketVO.setClosedNote(customerTicket.getCloseNote());
						}
						if (customerTicket.getModifiedOn() != null) {
							tempCustomerTicketVO.setModifiedOn(simpleDateFormat.format(customerTicket.getModifiedOn()));
						}

						if (StringUtils.isNotBlank(customerTicket.getModifiedBy())) {
							tempCustomerTicketVO.setModifiedBy(customerTicket.getModifiedBy());
						}
						Date slaDueDate = customerTicket.getSlaDuedate();
						Date creationTime = customerTicket.getCreatedOn();
						if (!customerTicket.getStatus().getStatus().equalsIgnoreCase("Closed")) {
							Date currentDateTime = new Date();
							long numeratorDiff = currentDateTime.getTime() - creationTime.getTime();
							long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
							double slaPercent = 0.0;

							double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
							double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
							if (deNumValue != 0) {
								slaPercent = Math.round((numValue / deNumValue) * 100);
							}
							tempCustomerTicketVO.setSlaPercent(slaPercent);
						} else {
							Date closedTime = customerTicket.getClosedOn();
							long numeratorDiff = closedTime.getTime() - creationTime.getTime();
							long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
							double slaPercent = 0.0;
							double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
							double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
							if (deNumValue != 0) {
								slaPercent = Math.round((numValue / deNumValue) * 100);
							}
							tempCustomerTicketVO.setSlaPercent(slaPercent);
						}

						customerTicketList.add(tempCustomerTicketVO);
					}
				} else {
					LOGGER.info("No Tickets available");
				}
			}
		}
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;
	}

	@Override
	public CustomerSPLinkedTicketVO saveSPLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, String spEmail)
			throws Exception {
		LOGGER.info("Inside TicketServiceImpl - saveLinkedTicket");
		CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
		if(StringUtils.isNotBlank(linkedTicket)){
			CustomerSPLinkedTicket customerSPLinkedTicket = new CustomerSPLinkedTicket();
			customerSPLinkedTicket.setCustTicketId(custTicket);
			customerSPLinkedTicket.setCustTicketNo(custTicketNumber);
			customerSPLinkedTicket.setSpTicketNo(linkedTicket);
			customerSPLinkedTicket.setClosedFlag("OPEN");
			customerSPLinkedTicket.setCreatedBy(spEmail);
			customerSPLinkedTicket = customerLinkedRepo.save(customerSPLinkedTicket);
			if(customerSPLinkedTicket.getId()!=null){
				customerSPLinkedTicketVO.setId(customerSPLinkedTicket.getId());
				customerSPLinkedTicketVO.setSpLinkedTicket(customerSPLinkedTicket.getSpTicketNo());
				customerSPLinkedTicketVO.setClosedFlag(customerSPLinkedTicket.getClosedFlag());
			}
		}
		LOGGER.info("Exit TicketServiceImpl - saveLinkedTicket");
		return customerSPLinkedTicketVO;
	}

	@Override
	public TicketCommentVO saveSPTicketComment(TicketCommentVO ticketCommentVO, SPLoginVO spLoginVO) throws Exception {
			ServiceProvider serviceProvider = serviceProviderRepo.findOne(spLoginVO.getSpId());
			if(serviceProvider!=null){
			TicketComment ticketComment = new TicketComment();
			ticketComment.setCreatedBy(serviceProvider.getHelpDeskEmail());
			ticketComment.setCustTicketNumber(ticketCommentVO.getTicketNumber());
			ticketComment.setTicketId(ticketCommentVO.getTicketId());
			ticketComment.setComment(ticketCommentVO.getComment());
			ticketComment = ticketCommentRepo.save(ticketComment);
			
			if(ticketComment.getCommentId()!=null){
				ticketCommentVO.setTicketNumber(ticketComment.getCustTicketNumber());
				ticketCommentVO.setTicketId(ticketComment.getTicketId());
				ticketCommentVO.setCommentId(ticketComment.getCommentId());
				ticketCommentVO.setComment(ticketComment.getComment());
				ticketCommentVO.setCreatedBy(ticketComment.getCreatedBy());
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY");
				ticketCommentVO.setCreatedDate(simpleDateFormat.format(ticketComment.getCreatedDate()));
			}
			}
		return ticketCommentVO;
	}

	
}
