/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.web.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.entities.User;
import com.jpa.entities.UserRole;
import com.pmsapp.view.vo.LoginUser;
import com.web.service.AuthorizationService;
import com.web.service.UserService;
import com.web.util.RestResponse;

/**
 * The Class CustomUserDetailServiceImpl.
 *
 */
@Service("userAuthorizationService")
@Transactional(readOnly = true)
public class AuthorizedUserDetailServiceImpl implements AuthorizationService,UserDetailsService {

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Load user by username.
	 *
	 * @param username
	 *          the username
	 * @return the user details
	 * @throws UsernameNotFoundException
	 *           the username not found exception
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 *      loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		try {
			User user = userService.findByUserName(username);
			if(user==null){
				System.out.println("User not found");
				throw new UsernameNotFoundException("Username not found"); 
			}else{
				boolean accountNonExpired = true;
				boolean credentialsNonExpired = true;
				boolean accountNonLocked = true;
				AuthorizedUserDetails authorizedUserDetails =
						new AuthorizedUserDetails(user.getEmailId(), user.getPassword(), true, accountNonExpired,
								credentialsNonExpired, accountNonLocked, getAuthorities(user.getUserRoles()));
				authorizedUserDetails.setUser(user);
				return authorizedUserDetails;
			}
		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

	/**
	 * Gets the authorities.
	 *
	 * @param userRoleList
	 *          the user role
	 * @return the authorities
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(final List<UserRole> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(UserRole role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole().getRoleName()));
		}

		return authorities;
	}

	@Override
	public RestResponse authorizeUserAccess(LoginUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void check(UserDetails user){
		if (!user.isAccountNonLocked()) {
			throw new LockedException("User account is locked");
		}
		if (!user.isEnabled()) {
			throw new DisabledException("User is disabled");
		}
		if (!user.isAccountNonExpired()) {
			throw new AccountExpiredException("User account has expired");
		}
	}





}
