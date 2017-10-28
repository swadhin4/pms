package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.entities.CustomerTicket;
import com.jpa.entities.ServiceProvider;

public interface CustomerTicketRepo extends JpaRepository<CustomerTicket, Long> {

	public CustomerTicket findByTicketNumber(String ticketNumber);

	public List<CustomerTicket> findBySiteSiteIdIn(List<Long> siteId); 

	@Query("from CustomerTicket ct where ct.status=:statusId")
	public List<CustomerTicket> findOpenTicketsByStatus(@Param(value="statusId") Long statusId);
	
	@Query("from CustomerTicket ct where ct.id=:ticketId order by ct.createdOn")
	public CustomerTicket findTicketById(@Param(value="ticketId") Long ticketId);
	
	
	public List<CustomerTicket> findByAssignedTo(ServiceProvider serviceProvider); 


}
