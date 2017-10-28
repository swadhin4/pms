package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.ServiceProviderSLADetails;

public interface ServiceProviderSLARepo extends JpaRepository<ServiceProviderSLADetails, Long> {

	public List<ServiceProviderSLADetails> findByServiceProviderServiceProviderId(Long spId);
	
	public ServiceProviderSLADetails findByServiceProviderServiceProviderIdAndPriorityId(Long spId, Long priorityId);

}
