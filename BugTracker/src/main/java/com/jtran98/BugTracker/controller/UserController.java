package com.jtran98.BugTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.UserService;
import com.jtran98.BugTracker.util.TicketComparator;
import com.jtran98.BugTracker.util.UserComparator;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserComparator userComparator;
	@Autowired
	private TicketComparator ticketComparator;
	
	/**
	 * Views project team mates of currently authenticated user
	 * @param model
	 * @return
	 */
	@GetMapping("/my-project-members")
	public String viewProjectMembers(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		model.addAttribute("viewProjectMembers", true);
		model.addAttribute("projectMembers", userService.getProjectMembers(userPrincipal.getProjectId()));
		model.addAttribute("userComparator", userComparator);
		return "/user/view-users";
	}
	@GetMapping("/view-user/{id}")
	public String viewUser(@PathVariable (value = "id") long id, Model model) {
		model.addAttribute("user", userService.getUser(id));
		model.addAttribute("ticketComparator", ticketComparator);
		return "/user/user-details";
	}
}
