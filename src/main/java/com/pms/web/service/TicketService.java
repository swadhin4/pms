package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.CustomerTicketVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPLoginVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.CustomerSPLinkedTicket;
import com.pms.jpa.entities.CustomerTicket;


public interface TicketService {

	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser user, SPLoginVO savedLoginVO) throws Exception;

	public CustomerTicket saveOrUpdate(CustomerTicket customerTicket) throws Exception;

	/*public List<CustomerTicketVO> getOpenCustomerTickets() throws Exception;*/

	public List<CustomerTicket> getTicketsByStatus(Long statusId) throws Exception;

	public List<CustomerTicket> getOpenTicketsBySite(Long siteId) throws Exception;


	public CustomerTicketVO getCustomerTicket(Long ticktId) throws Exception;

	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser) throws Exception;

	public TicketVO getSelectedTicket(Long ticketId) throws Exception;
	
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId);

	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) throws Exception;

	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, LoginUser loginUser) throws Exception;

	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicket) throws Exception;

	public CustomerSPLinkedTicket deleteLinkedTicket(Long linkedTicketId, String email);

	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId);

	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId);
	
	public List<TicketHistoryVO> getTicketHistory(Long ticketId);

	public CustomerSPLinkedTicketVO changeLinkedTicketStatus(Long linkedTicket, String string) throws Exception;
	
	public List<TicketCommentVO> getTicketComments(Long ticketId);
	
	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser user) throws Exception;

	public List<TicketVO> getCustomerTicketsBySP(Long spId) throws Exception;

	public CustomerSPLinkedTicketVO saveSPLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, String spEmail) throws Exception;

	public TicketCommentVO saveSPTicketComment(TicketCommentVO ticketCommentVO, SPLoginVO savedLoginVO) throws Exception;

}
