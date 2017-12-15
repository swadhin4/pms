package com.pms.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.Region;
import com.pms.jpa.repositories.RegionRepo;
import com.pms.web.service.RegionService;

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
