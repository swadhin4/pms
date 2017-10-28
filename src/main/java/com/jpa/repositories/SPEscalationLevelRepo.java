package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.SPEscalationLevels;
import com.jpa.entities.ServiceProviderSLADetails;

public interface SPEscalationLevelRepo extends JpaRepository<SPEscalationLevels, Long> {

	List<SPEscalationLevels> findByServiceProviderServiceProviderId(Long serviceProviderId);

}
