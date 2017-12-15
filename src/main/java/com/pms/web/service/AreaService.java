package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.Area;


public interface AreaService {


	public List<Area> findAllAreas(Long districtId) throws Exception;



}
