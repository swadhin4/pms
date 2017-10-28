package com.web.service;

import org.springframework.mail.MailException;

import com.app.mail.EmailTemplate;
import com.jpa.entities.SPEscalationLevels;
import com.pmsapp.view.vo.AppUserVO;
import com.pmsapp.view.vo.ServiceProviderVO;
import com.pmsapp.view.vo.TicketVO;
import com.web.util.RestResponse;

public interface EmailService {


	public void sendEmail(EmailTemplate emailTemplate) throws MailException;
	public RestResponse sendEmail(String to, String message) throws Exception;
	public RestResponse sendSuccessSaveEmail(String emailId, AppUserVO appUserVO) throws Exception;
	public RestResponse successSaveSPEmail(ServiceProviderVO serviceProviderVO) throws Exception;
	public RestResponse successTicketCreationSPEmail(TicketVO ticketVO, String creationStatus) throws Exception;
	public RestResponse sendForgotPasswordEmail(String email, String passwordResetLink) throws Exception;
	RestResponse successEscalationLevel(TicketVO ticketVO, SPEscalationLevels spEscalationLevel, String ccLevelEmail, String level) throws Exception;
}
