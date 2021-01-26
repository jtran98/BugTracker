package com.jtran98.BugTracker.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.ProjectService;
import com.jtran98.BugTracker.service.TicketService;
import com.jtran98.BugTracker.service.UserService;
import com.jtran98.BugTracker.util.TicketComparator;
import com.jtran98.BugTracker.util.UserComparator;
import com.jtran98.BugTracker.util.UserEndpointConstants;

@Controller
@RequestMapping(UserEndpointConstants.BASE)
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserComparator userComparator;
	@Autowired
	private TicketComparator ticketComparator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final int MINIMUM_PASSWORD_LENGTH_ALLOWED = 6;
	
	private long userDetailsId;
	private String viewUsersPage;
	private List<User> usersToLoad;
	private final String VIEW_USERS_PROJECT_PAGE = "viewProjectUsers";
	private final String VIEW_USERS_ALL_PAGE = "viewAllUsers";
	private final String CHANGED_SETTINGS = "changedSettings";
	
	/**
	 * Redirect for user details page
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.REDIRECT_VIEW_USER)
	public String viewUserDetails(Model model) {
		model.addAttribute("user", userService.getUser(userDetailsId));
		model.addAttribute("userProjectUpdate", userService.getUser(userDetailsId));
		model.addAttribute("userRoleUpdate", userService.getUser(userDetailsId));
		model.addAttribute("projectList", projectService.getAllProjects());
		model.addAttribute("ticketComparator", ticketComparator);
		return "/user/user-details.html";
	}
	/**
	 * Redirect for viewing a list of users
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.REDIRECT_VIEW_USERS)
	public String viewUsers(Model model) {
		model.addAttribute(viewUsersPage, true);
		model.addAttribute("viewUsers", usersToLoad);
		model.addAttribute("userComparator", userComparator);
		model.addAttribute("projectList", projectService.getAllProjects());
		return "/user/view-users.html";
	}
	/**
	 * Makes sure user's new password is valid before updating it, encrypts the password before saving to DB
	 * @param user - user DTO that will be updated
	 * @param model
	 * @return
	 */
	@PostMapping(UserEndpointConstants.UPDATE_PASSWORD)
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
		
		model.addAttribute(CHANGED_SETTINGS, true);
		return "/login.html";
	}
	/**
	 * Makes sure user's new username is valid before updating it
	 * @param user - user DTO to be updated
	 * @param model
	 * @return
	 */
	@PostMapping(UserEndpointConstants.UPDATE_USERNAME)
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
		
		model.addAttribute(CHANGED_SETTINGS, true);
		return "/login.html";
	}
	/**
	 * Takes user to their settings page
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping(UserEndpointConstants.SETTINGS)
	public String viewSettings(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		model.addAttribute("user", userPrincipal.getUser());
		return "/user/settings.html";
	}
	/**
	 * Enables a view of all users, attributes passed through allow admins to enable/disable accounts
	 * @param model
	 * @param authentication
	 * @return
	 */
	@GetMapping(UserEndpointConstants.MANAGE_USERS)
	public String viewAllUsers(Model model, Authentication authentication) {
		viewUsersPage = VIEW_USERS_ALL_PAGE;
		usersToLoad = userService.getAllUsers();
		return "redirect:/users/view-users";
	}
	/**
	 * Views project team mates of currently authenticated user
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.PROJECT_MEMBERS)
	public String viewProjectMembers(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		model.addAttribute(VIEW_USERS_PROJECT_PAGE, true);
		model.addAttribute("viewUsers", userService.getProjectMembers(userPrincipal.getProjectId()));
		model.addAttribute("userComparator", userComparator);
		model.addAttribute("projectList", projectService.getAllProjects());
		return "/user/view-users.html";
	}
	/**
	 * Disables user account. Effectively acts like archiving, as none of their actions or items are deleted
	 * @param id - user id
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.DISABLE_ACCOUNT+"/{id}")
	public String disableUserAccount(@PathVariable(value = "id") long id, Model model) {
		User user = userService.getUser(id);
		user.setActive(false);
		ticketService.removeAllTicketAssignedAssociation(id);
		user.setProjectTeam(null);
		userService.saveUser(user);
		
		usersToLoad = userService.getAllUsers();
		viewUsersPage = VIEW_USERS_ALL_PAGE;
		return "redirect:/user/view-users";
	}
	/**
	 * Re-enables account. Does not reassign any tickets or the project they lost
	 * @param id - user id
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.ENABLE_ACCOUNT+"/{id}")
	public String enableUserAccount(@PathVariable(value = "id") long id, Model model) {
		User user = userService.getUser(id);
		user.setActive(true);
		userService.saveUser(user);
		
		usersToLoad = userService.getAllUsers();
		viewUsersPage = VIEW_USERS_ALL_PAGE;
		return "redirect:/users/view-users";
	}
	/**
	 * Assigns user to project
	 * @param user - user DTO to be modified
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.ASSIGN_USER_TO_PROJECT)
	public String assignUserToProject(User user, Model model) {
		user.setActive(true);
		userService.saveUser(user);
		
		userDetailsId = user.getUserId();
		return "redirect:/users/user-details";
	}
	/**
	 * Assigns user to role
	 * @param user - user DTO to be modified
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.ASSIGN_USER_TO_ROLE)
	public String assignUserToRole(User user, Model model) {
		user.setActive(true);
		userService.saveUser(user);
		
		userDetailsId = user.getUserId();
		return "redirect:/users/user-details";
	}
	
	/**
	 * Views user in detail
	 * @param id - user id
	 * @param model
	 * @return
	 */
	@GetMapping(UserEndpointConstants.USER_DETAILS+"/{id}")
	public String viewUser(@PathVariable (value = "id") long id, Model model) {
		
		userDetailsId = id;
		return "redirect:/users/user-details";
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
	@PostMapping(UserEndpointConstants.SAVE_USER)
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
		
		user.setActive(true);
		user.setRole(AuthorityEnum.SUBMITTER);
		userService.saveUser(user);
		return "/login.html";
	}
}
