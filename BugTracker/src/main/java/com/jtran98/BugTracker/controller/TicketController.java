package com.jtran98.BugTracker.controller;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtran98.BugTracker.enums.StatusEnum;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	//get all tickets (admins)
	@GetMapping("/all")
	public String viewHomePage(Model model) {
		model.addAttribute("listTickets", ticketService.getAllTickets());
		return "/ticket/all-tickets";
	}
	
	//Get tickets by assignedId, (mainly for devs)
	@GetMapping("/my-tickets/{id}")
	public String getTicketsByAssignedId(@PathVariable (value = "id") long id, Model model) {
		model.addAttribute("myTickets", ticketService.getTicketsOfAssignedUser(id));
		return "/ticket/my-tickets";
	}
	
	//get tickets by project ID (mainly for PMs)
	@GetMapping("/project-tickets/{id}")
	public String getTicketsByProjectId(@PathVariable (value = "id") long id, Model model) {
		model.addAttribute("projectTickets", ticketService.getTicketsOfProject(id));
		return "/ticket/project-tickets";
	}
	
	@GetMapping("/submit-new-ticket")
	public String createNewTicketForm(Model model) {
		Ticket ticket = new Ticket();
		model.addAttribute("newTicket", ticket);
		return "/ticket/new-ticket";
	}
	
	@PostMapping("/save-ticket")
	public String makeNewTicket(@ModelAttribute("newTicket") Ticket ticket, Model model) {
		
		//automatically fills some fields that do not appear in the form
		//if new ticket, set creation date and status to OPEN. otherwise update the update date
		if(ticket.getCreationDate() == null) {
			ticket.setCreationDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			ticket.setStatus(StatusEnum.OPEN);
			ticket.setUpdateDate("N/A");
			
			/**
			 * TAKE THIS CODE OUT AFTER ROLE AUTH IS ADDED
			 */
			User tempUser = new User();
			tempUser.setUserId(1);
			ticket.setAssignedUser(tempUser);
			ticket.setProjectId(1);
			ticket.setSubmitter(tempUser);
		}
		else {
			ticket.setUpdateDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		}
		//TODO: Add these values after adding authentication (make them tie to user's id/project/etc UNLESS THEY'RE ADMIN
//		ticket.setAssignedId(1);
//		ticket.setProjectId(1);
//		ticket.setSubmitterId(1);
		
		ticketService.saveTicket(ticket);
		//TODO: make this redirect back to user's tickets after adding auth
		return "redirect:/index";
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
