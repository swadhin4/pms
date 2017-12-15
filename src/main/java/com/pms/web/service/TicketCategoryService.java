package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.TicketCategory;


public interface TicketCategoryService {


	public List<TicketCategory> getAllTicketCategories() throws Exception;


}
