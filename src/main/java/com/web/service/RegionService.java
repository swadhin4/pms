package com.web.service;

import java.util.List;

import com.jpa.entities.Region;


public interface RegionService {

	public List<Region> findAllRegions() throws Exception;

}
