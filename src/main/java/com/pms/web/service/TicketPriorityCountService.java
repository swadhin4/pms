package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.TicketPriorityCountView;


public interface TicketPriorityCountService {


	public List<TicketPriorityCountView> getAllTicketCount() throws Exception;

	public List<TicketPriorityCountView> getPriorityTicketCount() throws Exception;

}
