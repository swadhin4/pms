package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.IncidentReport;

public interface IncidentReportService {

	public List<IncidentReport> findAllIncident();
	
	public List<IncidentReport> findReportsByUser(String loggedUser);
}
