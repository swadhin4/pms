package com.pms.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.CustomerTicketVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPTicketEscalatedVO;
import com.pms.app.view.vo.SPTicketPriorityVO;
import com.pms.app.view.vo.SiteTicketPriority;
import com.pms.app.view.vo.SiteVO;
import com.pms.jpa.entities.Area;
import com.pms.jpa.entities.Cluster;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.CustomerTicket;
import com.pms.jpa.entities.District;
import com.pms.jpa.entities.OpenTicketsView;
import com.pms.jpa.entities.Region;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.Site;
import com.pms.jpa.entities.SlaApproachingView;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketCategory;
import com.pms.jpa.entities.TicketEscalationCountView;
import com.pms.jpa.entities.TicketPriorityCountView;
import com.pms.jpa.entities.TicketSiteView;
import com.pms.web.service.AreaService;
import com.pms.web.service.AssetService;
import com.pms.web.service.ClusterService;
import com.pms.web.service.CompanyService;
import com.pms.web.service.CountryService;
import com.pms.web.service.DistrictService;
import com.pms.web.service.OpenTicketService;
import com.pms.web.service.RegionService;
import com.pms.web.service.SiteService;
import com.pms.web.service.SlaApproachingViewService;
import com.pms.web.service.StatusService;
import com.pms.web.service.TicketCategoryService;
import com.pms.web.service.TicketEscalationCountService;
import com.pms.web.service.TicketPriorityCountService;
import com.pms.web.service.TicketService;
import com.pms.web.util.RestResponse;

@RestController
@RequestMapping("/test/api")
public class TestController extends BaseController{

	public static final Logger logger = LoggerFactory.getLogger(TestController.class);


	@Autowired
	private AreaService areaService;

	@Autowired
	private AssetService assetService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private TicketCategoryService ticketCategoryService;


	@Autowired
	private StatusService statusService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private OpenTicketService openTicketService;

	@Autowired
	private SlaApproachingViewService slaApproachingViewService;

	@Autowired
	private TicketPriorityCountService ticketPriorityCountService;

	@Autowired
	private TicketEscalationCountService ticketEscalationCountService;

	@Autowired
	private RegionService regionService;


	@RequestMapping(value = "/siteticketstatus", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<Set<SiteTicketPriority>> listTickets() {
		Set<SiteTicketPriority> openSiteTickets = new HashSet<SiteTicketPriority>();

		try {
			List<TicketPriorityCountView> openTickets =  ticketPriorityCountService.getPriorityTicketCount();
			//List<CustomerTicketVO> customerTickets =  ticketService.getOpenCustomerTickets();
			List<OpenTicketsView> openTicketsView = openTicketService.findOpenTicketsViews();
			List<TicketEscalationCountView> openEscalatedTickets = ticketEscalationCountService.getEscalatedTicketCount();
			Set<Site> siteNames = new HashSet<Site>();
			Set<ServiceProvider> serviceProviderNames = new HashSet<ServiceProvider>();

			/*for(CustomerTicketVO customerTicket : customerTickets){
				siteNames.add(customerTicket.getSite());
				serviceProviderNames.add(customerTicket.getAssignedTo());
			}*/

			for(Site site:siteNames){
				SiteTicketPriority siteTicketPriority = new SiteTicketPriority();
				siteTicketPriority.setSiteName(site.getSiteName());
				for(OpenTicketsView ticketSiteView : openTicketsView){
					if(ticketSiteView.getSiteName().equalsIgnoreCase(site.getSiteName())){
						siteTicketPriority.setTotalTicketCount(ticketSiteView.getNoOfOpenTickets().intValue());
					}
				}
				List<String> ticketPriorities = new ArrayList<String>();
				for(TicketPriorityCountView customerTicket : openTickets) {
					if(customerTicket.getSiteName().equalsIgnoreCase(site.getSiteName())){
						ticketPriorities.add(customerTicket.getPriority() +","+ customerTicket.getTicketCountFrom());

					}
				}
				siteTicketPriority.setPriorityList(ticketPriorities);
				for(TicketEscalationCountView customerTicket : openEscalatedTickets) {
					if(customerTicket.getSiteName().equalsIgnoreCase(site.getSiteName())){
						siteTicketPriority.setEscalatedTicketCount(customerTicket.getTicketCount());
					}
				}
				openSiteTickets.add(siteTicketPriority);
			}


			if (openSiteTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Set<SiteTicketPriority>>(openSiteTickets, HttpStatus.OK);
	}

	@RequestMapping(value = "/prioritytickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<TicketPriorityCountView>> listPriorityTickets() {
		List<TicketPriorityCountView> openTickets = null;
		try {
			openTickets = ticketPriorityCountService.getPriorityTicketCount();
			if (openTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<TicketPriorityCountView>>(openTickets, HttpStatus.OK);
	}




	@RequestMapping(value = "/sptickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<Set<SiteTicketPriority>> listEscalatedTickets() {

		Set<SiteTicketPriority> openSiteTickets = new HashSet<SiteTicketPriority>();

		try {
			List<SPTicketEscalatedVO> openSPEscalatedTickets =  ticketEscalationCountService.getSPEscalatedTicketCount();
			List<SPTicketPriorityVO> openPriorityickets = ticketEscalationCountService.getSPPriorityTicketCount();
			//List<CustomerTicketVO> customerTickets =  ticketService.getOpenCustomerTickets();
			List<OpenTicketsView> openTicketsView = openTicketService.findOpenTicketsViews();
			Set<ServiceProvider> serviceProviderNames = new HashSet<ServiceProvider>();

			/*for(CustomerTicketVO customerTicket : customerTickets){
				serviceProviderNames.add(customerTicket.getAssignedTo());
			}*/

			for(ServiceProvider sp:serviceProviderNames){
				SiteTicketPriority siteTicketPriority = new SiteTicketPriority();
				siteTicketPriority.setServiceProviderName(sp.getName());
				/*for(OpenTicketsView ticketSiteView : openTicketsView){
					if(ticketSiteView.getSiteName().equalsIgnoreCase(site.getSiteName())){
						siteTicketPriority.setTotalTicketCount(ticketSiteView.getNoOfOpenTickets().intValue());
					}
				}*/
				List<String> ticketPriorities = new ArrayList<String>();
				for(SPTicketPriorityVO customerTicket : openPriorityickets) {
					if(customerTicket.getCompanyName().equalsIgnoreCase(sp.getName())){
						ticketPriorities.add(customerTicket.getPriority() +","+ customerTicket.getTicketCount());

					}
				}
				siteTicketPriority.setPriorityList(ticketPriorities);
				for(SPTicketEscalatedVO customerTicket : openSPEscalatedTickets) {
					if(customerTicket.getCompanyName().equalsIgnoreCase(sp.getName())){
						siteTicketPriority.setTotalTicketCount(customerTicket.getTicketCount());
					}
				}
				openSiteTickets.add(siteTicketPriority);
			}


			if (openSiteTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Set<SiteTicketPriority>>(openSiteTickets, HttpStatus.OK);

	}



	@RequestMapping(value = "/opentickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<TicketSiteView>> listOpenTickets() {
		List<TicketSiteView> openTickets = null;
		try {
			openTickets = openTicketService.findAllTicketViews();
			if (openTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<TicketSiteView>>(openTickets, HttpStatus.OK);
	}



	@RequestMapping(value = "/allopentickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<OpenTicketsView>> listAllOpenTickets() {
		List<OpenTicketsView> openTickets = null;
		try {
			openTickets = openTicketService.findOpenTicketsViews();
			if (openTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<OpenTicketsView>>(openTickets, HttpStatus.OK);
	}


	@RequestMapping(value = "/slatickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<SlaApproachingView>> listAllTicketsBySLA() {
		List<SlaApproachingView> slaList = null;
		try {
			slaList = slaApproachingViewService.findTicketsApproachingSLA();
			if (slaList.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<SlaApproachingView>>(slaList, HttpStatus.OK);
	}

	@RequestMapping(value = "/countries", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Country>> listAllCountries() {
		List<Country> countries = null;
		try {
			countries = countryService.findAllCountries();
			if (countries.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
	}

	@RequestMapping(value = "/areas/{districtId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Area>> listAllArea(@PathVariable (value="districtId") Long districtId) {
		List<Area> areas = null;
		try {
			areas = areaService.findAllAreas(districtId);
			if (areas.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Area>>(areas, HttpStatus.OK);
	}

	@RequestMapping(value = "/companies", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Company>> listAllCompanies() {
		List<Company> companies = null;
		try {
			companies = companyService.findCompanyByType();
			if (companies.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Company>>(companies, HttpStatus.OK);
	}

	@RequestMapping(value = "/asset/serviceprovider", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Company>> findAssetServiceProvider() {
		List<Company> companies = null;
		try {
			companies = companyService.findAssetServiceProvider();
			if (companies.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Company>>(companies, HttpStatus.OK);
	}

	@RequestMapping(value = "/districts", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<District>> listAllDistricts() {
		List<District> districts = null;
		try {
			districts = districtService.findAllDistricts();
			if (districts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<District>>(districts, HttpStatus.OK);
	}

	@RequestMapping(value = "/clusters", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Cluster>> listAllClusters() {
		List<Cluster> clusters = null;
		try {
			clusters = clusterService.getAllClusters();
			if (clusters.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Cluster>>(clusters, HttpStatus.OK);
	}

	@RequestMapping(value = "/clusters/{districtId}/{areaId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Cluster>> listAllClustersBy(@PathVariable (value="districtId") Long districtId, 
			@PathVariable (value="areaId") Long areaId) {
		List<Cluster> clusters = null;
		try {
			clusters = clusterService.getAllClustersBy(districtId,areaId);
			if (clusters.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Cluster>>(clusters, HttpStatus.OK);
	}

	@RequestMapping(value = "/sites", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<CreateSiteVO>> listAllSites(HttpSession session) {
		logger.info("Inside TestController -- ListAllSites");
		List<CreateSiteVO> sitesList = null;
		try {
			LoginUser user= getCurrentLoggedinUser(session);
			if(user!=null){
				sitesList = siteService.getSiteList(user);
				if (sitesList.isEmpty()) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
					// You many decide to return HttpStatus.NOT_FOUND
				}else{
					Collections.sort(sitesList, CreateSiteVO.COMPARE_BY_SITENAME);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Exit TestController -- ListAllSites");
		return new ResponseEntity<List<CreateSiteVO>>(sitesList, HttpStatus.OK);
	}


	@RequestMapping(value = "/site/{siteId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<CreateSiteVO> getSiteDetail(@PathVariable(value="siteId") final Long siteId) {
		CreateSiteVO site = null;
		try {
			site = siteService.getSiteDetails(siteId);
			if (site==null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<CreateSiteVO>(site, HttpStatus.OK);
	}



	/*@RequestMapping(value = "/assets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Asset>> listAllAssets() {
		List<Asset> assets = null;
		try {
			assets = assetService.findAllAsset();
			if (assets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Asset>>(assets, HttpStatus.OK);
	}*/

	/*@RequestMapping(value = "/asset/{assetid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<Asset> getAssetServiceProvider(@PathVariable (value="assetid") final Long assetid) {
		Asset asset=null;
		try {
			asset = assetService.findAssetBy(assetid);
			if (asset == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Asset>(asset, HttpStatus.OK);
	}*/

	/*@RequestMapping(value = "/asset/site/{siteId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Asset>> listAllAssetBySite(@PathVariable (value="siteId") final Long siteId) {
		List<Asset> assets = null;
		try {
			assets = assetService.findAssetsBySite(siteId);
			if (assets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Asset>>(assets, HttpStatus.OK);
	}*/


	/*@RequestMapping(value = "/tickets", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<CustomerTicketVO>> listAllTickets() {
		List<CustomerTicketVO> customerTickets = null;
		try {
			customerTickets = ticketService.getOpenCustomerTickets();
			if (customerTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<CustomerTicketVO>>(customerTickets, HttpStatus.OK);
	}*/

	@RequestMapping(value = "/ticket/{ticketid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<CustomerTicketVO> getTicket(@PathVariable(value="ticketid") final Long ticketid) {
		CustomerTicketVO customerTicketVO = null;
		try {
			customerTicketVO = ticketService.getCustomerTicket(ticketid);
			if (StringUtils.isEmpty(customerTicketVO.getTicketNumber())) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<CustomerTicketVO>(customerTicketVO, HttpStatus.OK);
	}

	@RequestMapping(value = "/tickets/status/{statusId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<CustomerTicket>> listAllOpenTickets(@PathVariable (value="statusId") final Long statusId) {
		List<CustomerTicket> customerTickets = null;
		try {
			customerTickets = ticketService.getTicketsByStatus(statusId);
			if (customerTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<CustomerTicket>>(customerTickets, HttpStatus.OK);
	}

	@RequestMapping(value = "/site/tickets/{siteId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<CustomerTicket>> listAllOpenSiteTickets(@PathVariable (value="siteId") final Long siteId) {
		List<CustomerTicket> customerTickets = null;
		try {
			customerTickets = ticketService.getOpenTicketsBySite(siteId);
			if (customerTickets.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<CustomerTicket>>(customerTickets, HttpStatus.OK);
	}

	@RequestMapping(value = "/ticketcategories", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<TicketCategory>> listAllTicketCategories() {
		List<TicketCategory> categories = null;
		try {
			categories = ticketCategoryService.getAllTicketCategories();
			if (categories.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<TicketCategory>>(categories, HttpStatus.OK);
	}

	@RequestMapping(value = "/status/{category}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Status>> listAllOpenTickets(@PathVariable (value="category") final String category) {
		List<Status> statusList = null;
		try {
			statusList = statusService.getStatusByCategory(category);
			if (statusList.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Status>>(statusList, HttpStatus.OK);
	}

	@RequestMapping(value = "/ticket/assignedto/{cid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<Company> getServiceProvider(@PathVariable (value="cid") final Long cid) {
		Company company =null;
		try {
			company = companyService.findCompany(cid);
			if (company==null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND);
				// You many decide to return HttpStatus.NOT_FOUND
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@RequestMapping(value = "/ticket/save", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public RestResponse createTicket(@RequestBody final CustomerTicketVO customerTicketVO) {
		logger.info("customerTicket : " + customerTicketVO);
		RestResponse response=new RestResponse();
		CustomerTicketVO savedCustomerTIcket=null;
		try {
			if(customerTicketVO.getId()==null ){
				//savedCustomerTIcket = ticketService.saveOrUpdate(customerTicketVO);
				if(savedCustomerTIcket.getId()!=null){
					response.setStatusCode(200);
					response.setMessage("Ticket created successfully.");
				}else{
					response.setStatusCode(204);
					response.setMessage("Unable to create ticket");
				}
			}else if(customerTicketVO.getId()!=null){
				//savedCustomerTIcket = ticketService.saveOrUpdate(customerTicketVO);
				if(savedCustomerTIcket.getId()!=null){
					response.setStatusCode(200);
					response.setMessage("Ticket updated successfully.");
				}else{
					response.setStatusCode(204);
					response.setMessage("Unable to update ticket");
				}
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			logger.error("Exception while saving or updating a ticket", e);
			response.setStatusCode(500);
			response.setMessage("Error while saving /updating ticket");
		}
		return response;
	}

	@RequestMapping(value = "/site/save", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public RestResponse createTicket(@RequestBody final SiteVO siteVO) {
		logger.info("siteVO" + siteVO);
		RestResponse response=new RestResponse();
		SiteVO savedSiteVO=null;
		try {
			//savedSiteVO = siteService.saveOrUpdate(siteVO);
			if(savedSiteVO.getSiteId()!=null && siteVO.getSiteId() == null){
				response.setStatusCode(200);
				response.setObject(savedSiteVO);
				response.setMessage("Site created successfully.");
			}else if(savedSiteVO.getSiteId()!=null && siteVO.getSiteId() != null){
				response.setStatusCode(200);
				response.setObject(savedSiteVO);
				response.setMessage("Site updated successfully.");
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			logger.error("Exception while saving or updating site", e);
			response.setMessage("Exception while saving or updating site");
		}
		return response;
	}


	@RequestMapping(value = "/regions", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Region>> listAllRegions() {
		List<Region> regions = null;
		try {
			regions = regionService.findAllRegions();
			if (regions.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception getting region list", e);
		}

		return new ResponseEntity<List<Region>>(regions, HttpStatus.OK);
	}


	@RequestMapping(value = "/country/{regionId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Country>> listAllCountries(@PathVariable (value="regionId") final Long regionId ) {
		List<Country> countryList = null;
		try {
			countryList = countryService.findCountryByRegion(regionId);
			if (countryList.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception getting region list", e);
		}

		return new ResponseEntity<List<Country>>(countryList, HttpStatus.OK);
	}
}