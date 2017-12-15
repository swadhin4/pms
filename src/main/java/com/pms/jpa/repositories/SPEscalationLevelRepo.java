package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.SPEscalationLevels;

public interface SPEscalationLevelRepo extends JpaRepository<SPEscalationLevels, Long> {

	List<SPEscalationLevels> findByServiceProviderServiceProviderId(Long serviceProviderId);

}
