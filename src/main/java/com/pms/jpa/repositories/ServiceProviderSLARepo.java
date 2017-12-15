package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.ServiceProviderSLADetails;

public interface ServiceProviderSLARepo extends JpaRepository<ServiceProviderSLADetails, Long> {

	public List<ServiceProviderSLADetails> findByServiceProviderServiceProviderId(Long spId);
	
	public ServiceProviderSLADetails findByServiceProviderServiceProviderIdAndPriorityId(Long spId, Long priorityId);

}
