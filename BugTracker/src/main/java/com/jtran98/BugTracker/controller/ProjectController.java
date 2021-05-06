package com.jtran98.BugTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.model.Project;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.ProjectService;
import com.jtran98.BugTracker.service.TicketService;
import com.jtran98.BugTracker.service.UserService;
import com.jtran98.BugTracker.util.ProjectComparator;
import com.jtran98.BugTracker.util.ProjectEndpointConstants;

@Controller
@RequestMapping(ProjectEndpointConstants.BASE)
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectComparator projectComparator;
	
	/**
	 * Returns a view of all projects that exist
	 * @param model
	 * @return
	 */
	@GetMapping(ProjectEndpointConstants.MANAGE_PROJECTS)
	public String getAllProjects(Model model) {
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("projectComparator", projectComparator);
		return "/project/view-projects.html";
	}
	/**
	 * Loads a form to update a project
	 * @param id - project id
	 * @param model
	 * @return
	 */
	@GetMapping(ProjectEndpointConstants.UPDATE_PROJECT+"/{id}")
	public String renameProject(@PathVariable(value = "id") long id,  Model model) {
		model.addAttribute("modifyProject", projectService.getProjectById(id));
		return "/project/modify-project.html";
	}
	/**
	 * Loads a form to create a new project
	 * @param model
	 * @return
	 */
	@GetMapping(ProjectEndpointConstants.CREATE_NEW_PROJECT)
	public String createNewProject(Model model) {
		Project project = new Project();
		model.addAttribute("modifyProject", project);
		return "/project/modify-project.html";
	}
	/**
	 * Deletes a project, removes all ticket and user associations first
	 * @param id - project id
	 * @param model
	 * @return
	 */
	@GetMapping(ProjectEndpointConstants.DELETE_PROJECT+"/{id}")
	public String deleteProject(@PathVariable(value = "id") long id, Model model) {
		for(Ticket ticket : ticketService.getTicketsOfProject(id)) {
			ticket.setProjectSource(null);
		}
		for(User user : userService.getProjectMembers(id)) {
			user.setProjectTeam(null);
		}
		projectService.deleteProject(id);
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("projectComparator", projectComparator);
		return "/project/view-projects.html";
	}
	/**
	 * Saves a project
	 * @param project
	 * @param model
	 * @return
	 */
	@PostMapping(ProjectEndpointConstants.SAVE_PROJECT)
	public String saveProject(@ModelAttribute("modifyProject") Project project, Model model) {
		projectService.saveProject(project);
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("projectComparator", projectComparator);
		return "/project/view-projects.html";
	}
}
