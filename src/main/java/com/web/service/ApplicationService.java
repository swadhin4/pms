package com.web.service;

import java.util.List;

import com.jpa.entities.Role;
import com.pmsapp.view.vo.LoginUser;
import com.web.util.RestResponse;

public interface ApplicationService {

	public RestResponse checkUserRole(LoginUser user);

	public List<Role> findAllRoles(LoginUser user);

	boolean isSuperUser(LoginUser user);
}
