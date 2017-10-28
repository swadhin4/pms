package com.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.PasswordChangeInfo;

public interface PasswordResetRepo extends JpaRepository<PasswordChangeInfo, Long> {

	public PasswordChangeInfo findByToken(String token);

}
