package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.entities.TicketHistory;

public interface TicketHistoryRepo extends JpaRepository<TicketHistory, Long> {

	@Query("from TicketHistory t where t.ticketNumber =:ticketNumber order by t.timeStamp desc")
	public List<TicketHistory>  findByTicketNumber(@Param(value="ticketNumber") String ticketNumber);

}
