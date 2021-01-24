package com.jtran98.BugTracker.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	
	@PostMapping("/update-password")
	public String updatePassword(@ModelAttribute("user") User user, Model model) {
		if(user.getPassword().equals("")) {
			model.addAttribute("passwordNull", true);
			return "/user/settings.html";
		}
		else if(user.getPassword().length() < MINIMUM_PASSWORD_LENGTH_ALLOWED) {
			model.addAttribute("passwordTooShort", true);
			model.addAttribute("minimumPasswordLengthValue", MINIMUM_PASSWORD_LENGTH_ALLOWED);
			return "/user/settings.html";
		}
		else if(!user.getPassword().equals(user.getMatchingPassword())) {
			model.addAttribute("passwordNotMatching", true);
			return "/user/settings.html";
		}
		user.setActive(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setMatchingPassword(passwordEncoder.encode(user.getMatchingPassword()));
		userService.saveUser(user);
		
		model.addAttribute("changedSettings", true);
		return "/login.html";
	}
	@PostMapping("/update-username")
	public String updateUsername(@ModelAttribute("user") User user, Model model) {
		if(user.getUsername().equals("")) {
			model.addAttribute("usernameNull", true);
			return "/user/settings.html";
		}
		else if(userService.findUserWithUsername(user.getUsername()) != null && userService.findUserWithUsername(user.getUsername()).getUserId() == user.getUserId()) {
			model.addAttribute("usernameUnchanged", true);
			return "/user/settings.html";
		}
		else if(userService.hasUserWithUsername(user.getUsername())) {
			model.addAttribute("usernameAlreadyExists", true);
			return "/user/settings.html";
		}
		user.setActive(true);
		userService.saveUser(user);
		
		model.addAttribute("changedSettings", true);
		return "/login.html";
	}
	@GetMapping("/settings")
	public String viewSettings(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		model.addAttribute("user", userPrincipal.getUser());
		return "/user/settings.html";
	}
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
	/**
	 * Views user in detail
	 * @param id - user id
	 * @param model
	 * @return
	 */
	@GetMapping("/view-user/{id}")
	public String viewUser(@PathVariable (value = "id") long id, Model model) {
		model.addAttribute("user", userService.getUser(id));
		model.addAttribute("ticketComparator", ticketComparator);
		return "/user/user-details.html";
	}
	/**
	 * Takes you to the form to create a new user
	 * @param model
	 * @return
	 */
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
		
		//TODO: remove these after allowing admin to manage users
		user.setActive(true);
		user.setRole(AuthorityEnum.ADMINISTRATOR);
		userService.saveUser(user);
		return "/login.html";
	}
}
