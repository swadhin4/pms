package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketAttachment;

public interface TicketAttachmentRepo extends JpaRepository<TicketAttachment, Long> {

	public List<TicketAttachment> findByTicketNumber(String ticketNumber);
	
	
	public List<TicketAttachment> findByAttachmentIdIn(List<Long> attachementIds);
}
