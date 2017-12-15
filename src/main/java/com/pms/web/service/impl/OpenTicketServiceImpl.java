package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.OpenTicketsView;
import com.pms.jpa.entities.TicketSiteView;
import com.pms.jpa.repositories.OpenTicketsRepo;
import com.pms.jpa.repositories.TicketViewRepo;
import com.pms.web.service.OpenTicketService;

@Service("openTicketViewService")
public class OpenTicketServiceImpl implements OpenTicketService {

	@Autowired
	private OpenTicketsRepo openTicketsRepo;

	@Autowired
	private TicketViewRepo ticketViewRepo;

	@Override
	public List<OpenTicketsView> findOpenTicketsViews() {
		List<OpenTicketsView> openTicketList = new ArrayList<OpenTicketsView>();
		openTicketList = openTicketsRepo.findAll();
		return openTicketList == null?Collections.EMPTY_LIST:openTicketList;
	}

	@Override
	public List<TicketSiteView> findAllTicketViews() {
		List<TicketSiteView> ticketViewList = new ArrayList<TicketSiteView>();
		ticketViewList = ticketViewRepo.findAll();
		return ticketViewList == null?Collections.EMPTY_LIST:ticketViewList;
	}

}
