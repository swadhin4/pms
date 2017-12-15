package com.pms.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.app.view.vo.CountryVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.repositories.CountryRepo;
import com.pms.web.service.CountryService;



@Service("countryService")
public class CountryServiceImpl implements CountryService{


	@Autowired
	private CountryRepo countryRepo;

	@Override
	public Country findCountry(final Long countryId) {
		return countryRepo.findOne(countryId);
	}

	@Override
	public List<Country> findAllCountries() {
		List<Country> countryList =  countryRepo.findAll();
		return countryList == null?Collections.EMPTY_LIST:countryList;
	}

	@Override
	public CountryVO findCountryBy(final Long countryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Country> findCountryByRegion(Long regionId) {
		List<Country> countryList =  countryRepo.findByRegionId(regionId);
		return countryList == null?Collections.EMPTY_LIST:countryList;
	}


}