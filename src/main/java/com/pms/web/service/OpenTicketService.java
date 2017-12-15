package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.OpenTicketsView;
import com.pms.jpa.entities.TicketSiteView;

public interface OpenTicketService {

	public List<OpenTicketsView> findOpenTicketsViews();

	public List<TicketSiteView> findAllTicketViews();

}
