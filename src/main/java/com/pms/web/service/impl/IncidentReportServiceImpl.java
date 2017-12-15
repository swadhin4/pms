package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.IncidentReport;
import com.pms.jpa.repositories.GenericQueryExecutorDAO;
import com.pms.jpa.repositories.IncidentRepo;
import com.pms.web.service.IncidentReportService;

@Service("incidentReportService")
public class IncidentReportServiceImpl implements IncidentReportService {

	private static Logger logger = LoggerFactory.getLogger(IncidentReportServiceImpl.class);
	
	@Autowired
	private IncidentRepo incidentReportRepo;
	
	@Autowired
	private GenericQueryExecutorDAO genericQueryDAO;
	
	@Override
	public List<IncidentReport> findAllIncident() {
		List<IncidentReport> incidentReportList = incidentReportRepo.findAll();
		return incidentReportList==null?Collections.EMPTY_LIST:incidentReportList;
	}

	@Override
	public List<IncidentReport> findReportsByUser(String loggedUser) {
		List<IncidentReport> incidentReportList = new ArrayList<IncidentReport>();
		/*String ejbql = "select * from vw_incident_report";
		List<Object> objectList = genericQueryDAO.executeProjectedQuery(ejbql);*/
		incidentReportList=incidentReportRepo.findByTicketRaiseBy(loggedUser);
		return incidentReportList==null?Collections.EMPTY_LIST:incidentReportList;
	}

}
