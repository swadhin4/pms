package com.web.service;

import java.util.List;

import com.jpa.entities.District;
import com.pmsapp.view.vo.DistrictVO;


public interface DistrictService {

	public List<District> findAllDistricts() throws Exception;

	public List<DistrictVO> findDistrictByCountry(Long countryId) throws Exception;

}
