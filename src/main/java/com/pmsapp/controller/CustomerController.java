package com.pmsapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pmsapp.view.vo.CustomerVO;
import com.web.service.CustomerService;
import com.web.service.EmailService;
import com.web.util.RestResponse;

@RestController
@RequestMapping("/api")
public class CustomerController {

	private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/register/customer", method = RequestMethod.POST,produces="application/json")
	public ResponseEntity<RestResponse> saveCustomerDetails(@RequestBody final CustomerVO customerVO) {
		logger.info("Inside CustomerController ... saveCustomerDetails..");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		CustomerVO savedCustomerVO = null;
		try {
			logger.info("Regstering new customer having email id as "+ customerVO.getEmail());
			savedCustomerVO = customerService.saveCustomer(customerVO);
			if(savedCustomerVO.getCustomerId()!=null){
				logger.info("Sending Email to customer");
				response = emailService.sendEmail(customerVO.getEmail(), customerVO.getMessage());
				if (response.getStatusCode() == 500) {
					logger.info("Unable to send email");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
					// You many decide to return HttpStatus.NOT_FOUND
				}
				else if(response.getStatusCode() == 200){
					response.setObject(savedCustomerVO);
					response.setMessage("Thank you for expressing interest in PMS Platform.");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else{
				response.setMessage("!! Sorry, this email id is already taken. Please try another one or login.");
				response.setObject(savedCustomerVO);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("Exception while registering new customer");
			logger.error("Exception while registering new customer ", e);
		}
		logger.info("Exit CustomerController ... saveCustomerDetails..");
		return responseEntity;
	}
}
