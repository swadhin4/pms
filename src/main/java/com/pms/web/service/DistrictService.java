package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.DistrictVO;
import com.pms.jpa.entities.District;


public interface DistrictService {

	public List<District> findAllDistricts() throws Exception;

	public List<DistrictVO> findDistrictByCountry(Long countryId) throws Exception;

}
