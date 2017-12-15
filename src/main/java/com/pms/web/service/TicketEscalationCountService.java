package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.SPTicketEscalatedVO;
import com.pms.app.view.vo.SPTicketPriorityVO;
import com.pms.jpa.entities.TicketEscalationCountView;


public interface TicketEscalationCountService {


	public List<TicketEscalationCountView> getAllTicketCount() throws Exception;

	public List<TicketEscalationCountView> getEscalatedTicketCount() throws Exception;


	public List<SPTicketEscalatedVO> getSPEscalatedTicketCount() throws Exception;

	public List<SPTicketPriorityVO> getSPPriorityTicketCount() throws Exception;


}
