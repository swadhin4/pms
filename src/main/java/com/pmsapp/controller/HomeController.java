package com.pmsapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pmsapp.view.vo.LoginUser;
import com.web.service.ApplicationService;
import com.web.service.UserService;
import com.web.util.RestResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationService applicationService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(final Locale locale, final ModelMap model,final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		String message = (String) model.get("message");
		if(loginUser.getLoginStatus() == 0){
			model.put("message", null);
		}
		else{
			model.put("message", loginUser.getMessage());
			session.invalidate();
		}
		return "default";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(final Locale locale, final ModelMap model,final HttpServletRequest request) {
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(final Locale locale, final ModelMap model,final HttpServletRequest request) {
		return "register";
	}

	@RequestMapping(value = "/appdashboard", method = RequestMethod.GET)
	public String loginSuccess(final Locale locale, final Model model, final HttpSession session) {
		LOGGER.info("Validating currently logged in user.");
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if(loginUser!=null) {
			if(StringUtils.isEmpty(loginUser.getUsername())) {
				return "redirect:/";
			}else {
				LOGGER.info("Logged in User : " + loginUser.getUsername());
				model.addAttribute("user", loginUser);
				RestResponse response=applicationService.checkUserRole(loginUser);
				if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
					return "redirect:/user/profile";
				}else{
					return response.getRedirectUrl();
				}
			}
		}else {
			model.addAttribute("message", "Invalid username or password");
			return "redirect:/";
		}
	}


}
