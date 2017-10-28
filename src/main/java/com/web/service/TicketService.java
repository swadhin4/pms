package com.web.service;

import java.util.List;

import com.jpa.entities.CustomerTicket;
import com.pmsapp.view.vo.CustomerSPLinkedTicketVO;
import com.pmsapp.view.vo.CustomerTicketVO;
import com.pmsapp.view.vo.LoginUser;
import com.pmsapp.view.vo.SPLoginVO;
import com.pmsapp.view.vo.TicketCommentVO;
import com.pmsapp.view.vo.TicketEscalationVO;
import com.pmsapp.view.vo.TicketHistoryVO;
import com.pmsapp.view.vo.TicketPrioritySLAVO;
import com.pmsapp.view.vo.TicketVO;


public interface TicketService {

	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser user) throws Exception;

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

	public void deleteLinkedTicket(Long linkedTicketId);

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
