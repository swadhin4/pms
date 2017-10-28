package com.web.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.pmsapp.view.vo.LoginUser;
import com.web.util.RestResponse;

public interface AuthorizationService {

	public RestResponse authorizeUserAccess(LoginUser user) throws Exception;

	public void  check(UserDetails user) ;
}
