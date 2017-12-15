package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.CustomerSPLinkedTicket;

public interface CustomerSPLinkedTicketRepo extends JpaRepository<CustomerSPLinkedTicket, Long> {

	public List<CustomerSPLinkedTicket> findByCustTicketIdAndDelFlag(Long custTicketId, int delFlag);

}
