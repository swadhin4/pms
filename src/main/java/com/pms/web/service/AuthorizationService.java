package com.pms.web.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.pms.app.view.vo.LoginUser;
import com.pms.web.util.RestResponse;

public interface AuthorizationService {

	public RestResponse authorizeUserAccess(LoginUser user) throws Exception;

	public void  check(UserDetails user) ;
}
