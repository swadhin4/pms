package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.Company;
import com.pms.jpa.repositories.CompanyRepo;
import com.pms.web.service.CompanyService;



@Service("companyService")
public class CompanyServiceImpl implements CompanyService{


	@Autowired
	private CompanyRepo companyRepo;

	@Override
	public Company findCompany(final Long companyId) {
		Company company=companyRepo.findOne(companyId);
		if(company!=null){
			return company;
		}
		return null;
	}

	@Override
	public List<Company> findCompanyByType() {
		List<Company> companyList = new ArrayList<Company>();
		companyList=companyRepo.findCompanyByType();
		return companyList == null?Collections.EMPTY_LIST:companyList;
	}

	@Override
	public List<Company> findAssetServiceProvider() {
		List<Company> companyList = new ArrayList<Company>();
		companyList=companyRepo.findAssetServiceProvider();
		return companyList == null?Collections.EMPTY_LIST:companyList;
	}





}
