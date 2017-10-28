package com.web.service;

import java.util.List;

import com.jpa.entities.OpenTicketsView;
import com.jpa.entities.TicketSiteView;

public interface OpenTicketService {

	public List<OpenTicketsView> findOpenTicketsViews();

	public List<TicketSiteView> findAllTicketViews();

}
