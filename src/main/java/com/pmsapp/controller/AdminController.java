/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pmsapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pmsapp.view.vo.LoginUser;
import com.web.service.ApplicationService;
import com.web.service.UserService;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/admin")
@Controller
public class AdminController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationService applicationService;



	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = "application/json")
	public String userHome(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			boolean isSessionEnabled=request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled +""+ request.getSession().getId());
			model.put("user", loginUser);
			return "admin.home";
		} else {
			return "redirect:/login";
		}
	}

}
