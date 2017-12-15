package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.Region;


public interface RegionService {

	public List<Region> findAllRegions() throws Exception;

}
