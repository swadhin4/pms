package com.web.service;

import java.util.List;

import com.jpa.entities.IncidentReport;

public interface IncidentReportService {

	public List<IncidentReport> findAllIncident();
	
	public List<IncidentReport> findReportsByUser(String loggedUser);
}
