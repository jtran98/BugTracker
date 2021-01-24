package com.jtran98.BugTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jtran98.BugTracker.security.UserPrincipalDetailsService;

/**
 * Controller for misc mappings like index and login
 * @author Jacky Tran
 *
 */
@Controller
@RequestMapping("/")
public class HomePageController {
	
	@Autowired
	UserPrincipalDetailsService userPrincipalDetailsService;
	/**
	 * Home page
	 * @return
	 */
	@GetMapping("index")
	public String viewHomePage() {
		return "/index.html";
	}
	/**
	 * Login page
	 * @return
	 */
	@GetMapping("login")
	public String login() {
		return "/login.html";
	}
	/**
	 * Login page failure mapping
	 * @param model
	 * @return
	 */
	@GetMapping("login-failed")
	public String loginFailed(Model model){
		model.addAttribute("loginFailed", true);
		return "/login.html";
	}
	/**
	 * Logs the user out
	 * @return
	 */
	@GetMapping("logout")
	public String logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "/login.html";
	}
}
