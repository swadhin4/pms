package com.web.service;

import java.util.List;

import com.jpa.entities.Status;

public interface StatusService {
	public List<Status> getStatusByCategory(String category) throws Exception;

}
