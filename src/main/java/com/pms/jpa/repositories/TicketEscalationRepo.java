package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketEscalation;

public interface TicketEscalationRepo extends JpaRepository<TicketEscalation, Long> {

	public List<TicketEscalation> findByTicketId(Long ticketId);

	public TicketEscalation findByTicketIdAndEscLevelId(Long ticketId, Long escId);

}
