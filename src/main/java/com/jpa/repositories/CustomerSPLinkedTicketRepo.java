package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.CustomerSPLinkedTicket;

public interface CustomerSPLinkedTicketRepo extends JpaRepository<CustomerSPLinkedTicket, Long> {

	public List<CustomerSPLinkedTicket> findByCustTicketId(Long custTicketId);

}
