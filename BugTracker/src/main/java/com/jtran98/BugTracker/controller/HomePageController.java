package com.jtran98.BugTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jtran98.BugTracker.security.UserPrincipalDetailsService;

@Controller
@RequestMapping("/")
public class HomePageController {
	
	@Autowired
	UserPrincipalDetailsService userPrincipalDetailsService;
	
	@GetMapping("index")
	public String viewHomePage() {
		return "/index";
	}
	@GetMapping("login")
	public String login() {
		return "/login";
	}
	@GetMapping("login-failed")
	public String loginFailed(Model model){
		model.addAttribute("loginFailed", true);
		return "/login";
	}
}
