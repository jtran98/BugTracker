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

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectComparator projectComparator;
	
	@GetMapping("/manage-projects")
	public String getAllProjects(Model model) {
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("projectComparator", projectComparator);
		return "/project/view-projects.html";
	}
	@GetMapping("/update-project/{id}")
	public String renameProject(@PathVariable(value = "id") long id,  Model model) {
		model.addAttribute("modifyProject", projectService.getProjectById(id));
		return "/project/modify-project.html";
	}
	@GetMapping("/create-new-project")
	public String createNewProject(Model model) {
		Project project = new Project();
		model.addAttribute("modifyProject", project);
		return "/project/modify-project.html";
	}
	@GetMapping("/delete-project/{id}")
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
	@PostMapping("/save-project")
	public String saveProject(@ModelAttribute("modifyProject") Project project, Model model) {
		projectService.saveProject(project);
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("projectComparator", projectComparator);
		return "/project/view-projects.html";
	}
}
