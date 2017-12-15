package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.Status;

public interface StatusService {
	public List<Status> getStatusByCategory(String category) throws Exception;

}
