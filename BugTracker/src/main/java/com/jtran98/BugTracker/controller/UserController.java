package com.jtran98.BugTracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.model.Project;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.security.UserPrincipalDetailsService;
import com.jtran98.BugTracker.service.ProjectService;
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
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final int MINIMUM_PASSWORD_LENGTH_ALLOWED = 6;
	
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
		return "/user/view-users.html";
	}
	@GetMapping("/view-user/{id}")
	public String viewUser(@PathVariable (value = "id") long id, Model model) {
		model.addAttribute("user", userService.getUser(id));
		model.addAttribute("ticketComparator", ticketComparator);
		return "/user/user-details.html";
	}
	@GetMapping("/create-new-user")
	public String createUser(Model model) {
		model.addAttribute("newUser", new User());
		return "/user/create-user.html";
	}
	/**
	 * Checks if user filled all fields properly, then saves new user if true
	 * @param user - user to be created
	 * @param model
	 * @return
	 */
	@PostMapping("/save-user")
	public String saveUser(@ModelAttribute("newUser") User user, Model model) {
		if(user.getFirstName().equals("")) {
			model.addAttribute("firstNameNull", true);
			return "/user/create-user.html";
		}
		else if(!user.getFirstName().chars().allMatch(Character :: isLetter)) {
			model.addAttribute("firstNameInvalidCharacters", true);
			return "/user/create-user.html";
		}
		else if(user.getLastName().equals("")) {
			model.addAttribute("lastNameNull", true);
			return "/user/create-user.html";
		}
		else if(!user.getLastName().chars().allMatch(Character :: isLetter)) {
			model.addAttribute("lastNameInvalidCharacters", true);
			return "/user/create-user.html";
		}
		else if(user.getUsername().equals("")) {
			model.addAttribute("usernameNull", true);
			return "/user/create-user.html";
		}
		else if(userService.hasUserWithUsername(user.getUsername())) {
			model.addAttribute("usernameAlreadyExists", true);
			return "/user/create-user.html";
		}
		else if(user.getPassword().equals("")) {
			model.addAttribute("passwordNull", true);
			return "/user/create-user.html";
		}
		else if(user.getPassword().length() < MINIMUM_PASSWORD_LENGTH_ALLOWED) {
			model.addAttribute("passwordTooShort", true);
			model.addAttribute("minimumPasswordLengthValue", MINIMUM_PASSWORD_LENGTH_ALLOWED);
			return "/user/create-user.html";
		}
		else if(!user.getPassword().equals(user.getMatchingPassword())) {
			model.addAttribute("passwordNotMatching", true);
			return "/user/create-user.html";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setMatchingPassword(passwordEncoder.encode(user.getMatchingPassword()));
		userService.saveUser(user);
		return "/login.html";
	}
}
