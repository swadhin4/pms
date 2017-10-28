package com.web.service;

import java.util.List;

import com.jpa.entities.TicketCategory;


public interface TicketCategoryService {


	public List<TicketCategory> getAllTicketCategories() throws Exception;


}
