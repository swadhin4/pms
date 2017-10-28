package com.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpa.entities.Area;
import com.jpa.repositories.AreaRepo;
import com.web.service.AreaService;



@Service("areaService")
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaRepo areaRepo;

	@Override
	public List<Area> findAllAreas(Long districtId) throws Exception {
		return areaRepo.findAreaBy(districtId);
	}


}
