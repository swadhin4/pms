package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.Role;
import com.pms.web.util.RestResponse;

public interface ApplicationService {

	public RestResponse checkUserRole(LoginUser user);

	public List<Role> findAllRoles(LoginUser user);

	boolean isSuperUser(LoginUser user);
}
