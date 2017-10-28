package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.entities.ServiceProvider;

public interface ServiceProviderRepo extends JpaRepository<ServiceProvider, Long> {

	@Query("from ServiceProvider sp where sp.email=:email and sp.company.companyId=:customerId")
	public ServiceProvider findByEmailAndCompanyCompanyId(@Param(value="email") String email, @Param(value="customerId") Long customerId);

	@Query("from ServiceProvider sp where sp.company.companyId=:customerId")
	public List<ServiceProvider> findByCompany(@Param(value="customerId") Long customerId);

	public List<ServiceProvider> findByCompanyCompanyIdIn(List<Long> companyId);
	
	public ServiceProvider findBySpUsername(String username);
}
