package com.web.service;

import java.util.List;

import com.jpa.entities.TicketEscalationCountView;
import com.pmsapp.view.vo.SPTicketEscalatedVO;
import com.pmsapp.view.vo.SPTicketPriorityVO;


public interface TicketEscalationCountService {


	public List<TicketEscalationCountView> getAllTicketCount() throws Exception;

	public List<TicketEscalationCountView> getEscalatedTicketCount() throws Exception;


	public List<SPTicketEscalatedVO> getSPEscalatedTicketCount() throws Exception;

	public List<SPTicketPriorityVO> getSPPriorityTicketCount() throws Exception;


}
