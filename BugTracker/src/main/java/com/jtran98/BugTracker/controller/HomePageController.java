package com.jtran98.BugTracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {
		
	@GetMapping("login")
	public String login() {
		return "/login";
	}
	
	@GetMapping("index")
	public String viewHomePage() {
		return "/index";
	}
}
