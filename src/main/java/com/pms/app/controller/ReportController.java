package com.pms.app.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.IncidentReport;
import com.pms.web.service.IncidentReportService;
import com.pms.web.util.RestResponse;

@Controller
@RequestMapping ("/reports")
public class ReportController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private IncidentReportService incidentReportService;
	
	@RequestMapping(value = "/view", method = RequestMethod.GET, produces = "application/json")
	public String userHome(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			boolean isSessionEnabled=request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled +""+ request.getSession().getId());
			model.put("user", loginUser);
			return "report.list";
		} else {
			return "redirect:/login";
		}
	}
	
	
	@RequestMapping(value = "/details/{loggeduser}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> commentLint(final ModelMap model,@PathVariable (value="loggeduser") String loggeduser,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser!=null) {
				List<IncidentReport> userIncidents=incidentReportService.findReportsByUser(loginUser.getUsername());
				if(!userIncidents.isEmpty()){
					response.setStatusCode(200);
					response.setObject(userIncidents);
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
