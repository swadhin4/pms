package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.IncidentReport;

public interface IncidentRepo extends JpaRepository<IncidentReport, Long> {

	public List<IncidentReport> findByTicketRaiseBy(String ticketNumber);

}
