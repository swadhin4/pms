package com.pmsapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {


	@RequestMapping(value="/manage")
	public String getRoles(){
		return "role.entry";
	}

}
