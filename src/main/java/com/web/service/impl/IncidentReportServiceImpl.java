package com.web.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.IncidentReport;
import com.jpa.repositories.GenericQueryExecutorDAO;
import com.jpa.repositories.IncidentRepo;
import com.web.service.IncidentReportService;

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
