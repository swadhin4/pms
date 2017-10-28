package com.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.Region;
import com.jpa.repositories.RegionRepo;
import com.web.service.RegionService;

@Service("regionService")
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepo regionRepo;

	@Override
	public List<Region> findAllRegions() throws Exception {
		List<Region> regionList = regionRepo.findAll();
		return  regionList== null? Collections.EMPTY_LIST:regionList;
	}

}
