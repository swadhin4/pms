package com.pms.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.PasswordChangeInfo;

public interface PasswordResetRepo extends JpaRepository<PasswordChangeInfo, Long> {

	public PasswordChangeInfo findByToken(String token);

}
