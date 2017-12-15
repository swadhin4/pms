package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.CountryVO;
import com.pms.jpa.entities.Country;


public interface CountryService {

	public Country findCountry(Long countryId);

	public CountryVO findCountryBy(Long countryId);

	public List<Country> findAllCountries();

	public List<Country> findCountryByRegion(Long regionId);

}
