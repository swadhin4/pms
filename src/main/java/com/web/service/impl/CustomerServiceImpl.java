package com.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.Customer;
import com.jpa.repositories.CustomerRepo;
import com.pmsapp.view.vo.CustomerVO;
import com.web.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepo customerRepo;

	@Override
	public CustomerVO saveCustomer(final CustomerVO customerVO) throws Exception {
		logger.info("Inside CustomerServiceImpl .. saveCustomer");
		CustomerVO savedCustomerVO = null;
		Customer customer= new Customer();
		customer.setEmail(customerVO.getEmail());
		customer.setMessage(customerVO.getMessage());
		customer = customerRepo.save(customer);
		if(customer.getCustomerId()!=null){
			savedCustomerVO = new CustomerVO();
			savedCustomerVO.setCustomerId(customer.getCustomerId());
			savedCustomerVO.setEmail(customer.getEmail());
			savedCustomerVO.setMessage(customer.getMessage());
			savedCustomerVO.setRegistered(true);
		}
		return savedCustomerVO;
	}

}
