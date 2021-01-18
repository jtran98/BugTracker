package com.jtran98.BugTracker.controller;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	/**
	 * Gets all tickets
	 * @param model
	 * @return
	 */
	@GetMapping("/all")
	public String viewHomePage(Model model) {
		model.addAttribute("listTickets", ticketService.getAllTickets());
		return "/ticket/all-tickets";
	}
	
	/**
	 * Gets all tickets the currently authenticated user is assigned to
	 * @param model
	 * @return
	 */
	@GetMapping("/my-tickets")
	public String getAssignedTicketsOfCurrentUser(Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("myTickets", ticketService.getTicketsOfAssignedUser(userPrincipal.getUserId()));
		return "/ticket/my-tickets";
	}
	
	/**
	 * Gets all tickets the of the currently authenticated user's project
	 * @param model
	 * @return
	 */
	@GetMapping("/project-tickets")
	public String getTicketsByProjectId(Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("projectTickets", ticketService.getTicketsOfProject(userPrincipal.getProjectId()));
		return "/ticket/project-tickets";
	}
	
	@GetMapping("/submit-new-ticket")
	public String createNewTicketForm(Model model) {
		Ticket ticket = new Ticket();
		model.addAttribute("newTicket", ticket);
		return "/ticket/new-ticket";
	}
	
	@PostMapping("/save-ticket")
	public String makeNewTicket(@ModelAttribute("newTicket") Ticket ticket, Model model, Authentication auth) {
		if(ticket.getCreationDate() == null) {
			ticket.setCreationDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			ticket.setMostRecentUpdateDate("N/A");
		}
		else {
			ticket.setMostRecentUpdateDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		}
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		ticket.setProjectSource(userPrincipal.getProjectTeam());
		ticket.setSubmitter(userPrincipal.getUser());
		ticketService.saveTicket(ticket);
		return "redirect:/tickets/my-tickets";
	}
	
	@GetMapping("/update-ticket/{id}")
	public String updateTicket(@PathVariable (value = "id") long id, Model model) {
		Ticket ticket = ticketService.getTicketByTicketId(id);
		model.addAttribute("updateTicket", ticket);
		return "/ticket/update-ticket";
	}
	
	@GetMapping("/delete-ticket/{id}")
	public String deleteTicket(@PathVariable (value = "id") long id) {
		ticketService.deleteTicketByTicketId(id);
		return "redirect:/index";
	}
}
