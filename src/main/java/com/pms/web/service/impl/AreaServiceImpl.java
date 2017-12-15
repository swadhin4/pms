package com.pms.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.Area;
import com.pms.jpa.repositories.AreaRepo;
import com.pms.web.service.AreaService;



@Service("areaService")
public class AreaServiceImpl implements AreaService{

	@Autowired
	private AreaRepo areaRepo;

	@Override
	public List<Area> findAllAreas(Long districtId) throws Exception {
		return areaRepo.findAreaBy(districtId);
	}


}
