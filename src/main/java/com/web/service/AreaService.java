package com.web.service;

import java.util.List;

import com.jpa.entities.Area;


public interface AreaService {


	public List<Area> findAllAreas(Long districtId) throws Exception;



}
