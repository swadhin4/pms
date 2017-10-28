package com.web.service;

import java.util.List;

import com.jpa.entities.Country;
import com.pmsapp.view.vo.CountryVO;


public interface CountryService {

	public Country findCountry(Long countryId);

	public CountryVO findCountryBy(Long countryId);

	public List<Country> findAllCountries();

	public List<Country> findCountryByRegion(Long regionId);

}
