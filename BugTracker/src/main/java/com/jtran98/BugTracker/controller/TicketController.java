package com.jtran98.BugTracker.controller;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jtran98.BugTracker.exception.FileStorageException;
import com.jtran98.BugTracker.model.CommentEntry;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.TicketFile;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.CommentEntryService;
import com.jtran98.BugTracker.service.LogEntryService;
import com.jtran98.BugTracker.service.TicketService;
import com.jtran98.BugTracker.util.TicketComparator;

/**
 * Controller for all mappings relating to tickets
 * @author Jacky
 *
 */
@Controller
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	@Autowired
	private LogEntryService logEntryService;
	@Autowired
	private CommentEntryService commentEntryService;
	@Autowired
	private TicketComparator ticketComparator;
	
	/**
	 * Gets all tickets
	 * @param model
	 * @return
	 */
	@GetMapping("/all")
	public String viewHomePage(Model model) {
		model.addAttribute("viewTickets", ticketService.getAllTickets());
		return "/ticket/view-tickets";
	}
	
	/**
	 * Gets all tickets the currently authenticated user is assigned to
	 * @param model
	 * @return
	 */
	@GetMapping("/assigned-tickets")
	public String getAssignedTicketsOfCurrentUser(Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("viewTickets", ticketService.getTicketsOfAssignedUser(userPrincipal.getUserId()));
		model.addAttribute("viewAssignedTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	/**
	 * Gets all tickets the currently authenticated user submitted
	 * @param model
	 * @return
	 */
	@GetMapping("/submitted-tickets")
	public String getSubmittedTicketsOfCurrentUser(Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("viewTickets", ticketService.getTicketsUserSubmitted(userPrincipal.getUserId()));
		model.addAttribute("viewSubmittedTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	
	/**
	 * Gets all tickets the of the currently authenticated user's project
	 * @param model
	 * @return
	 */
	@GetMapping("/project-tickets")
	public String getTicketsByProjectId(Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("viewTickets", ticketService.getTicketsOfProject(userPrincipal.getProjectId()));
		model.addAttribute("viewProjectTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	
	
	/**
	 * Saves ticket to the repository
	 * @param ticket - object taken from thymeleaf template
	 * @param model
	 * @param auth
	 * @return
	 */
	@PostMapping("/save-ticket")
	public String makeNewTicket(Ticket ticket,  Model model, Authentication auth) {
		//Date is changed based on whether the ticket is new or is an existing one being updated
		if(ticket.getCreationDate() == null || ticket.getCreationDate().equals("")) {
			ticket.setCreationDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			ticket.setMostRecentUpdateDate("N/A");
		}
		else {
			ticket.setMostRecentUpdateDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		}
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		//If ticket is new, set submitter id and project id to the ones of the currently logged in user
		if(ticket.getAssignedUser() == null) {
			ticket.setProjectSource(userPrincipal.getProjectTeam());
			ticket.setSubmitter(userPrincipal.getUser());
		}
		ticketService.saveTicket(ticket);
		model.addAttribute("viewTickets", ticketService.getTicketsUserSubmitted(userPrincipal.getUserId()));
		model.addAttribute("viewSubmittedTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	/**
	 * Takes user to ticket modification page
	 * @param model
	 * @return
	 */
	@GetMapping("/submit-new-ticket")
	public String createNewTicketForm(Model model) {
		Ticket ticket = new Ticket();
		model.addAttribute("modifyTicket", ticket);
		return "/ticket/modify-ticket";
	}
	/**
	 * Takes user to ticket modification page, with the ticket's existing values prefilled
	 * @param id - id of ticket
	 * @param model
	 * @return
	 */
	@GetMapping("/update-ticket/{id}")
	public String updateTicket(@PathVariable (value = "id") long id, Model model) {
		Ticket ticket = ticketService.getTicketByTicketId(id);
		model.addAttribute("modifyTicket", ticket);
		return "/ticket/modify-ticket";
	}
	/**
	 * Deletes ticket
	 * @param id - id of ticket
	 * @return
	 */
	@GetMapping("/delete-ticket/{id}")
	public String deleteTicket(@PathVariable (value = "id") long id, Model model, Authentication auth) {
		ticketService.deleteTicketByTicketId(id);
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		model.addAttribute("viewTickets", ticketService.getTicketsOfAssignedUser(userPrincipal.getUserId()));
		model.addAttribute("viewAssignedTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	/**
	 * Views a ticket in more detail
	 * @param id - id of ticket
	 * @param model
	 * @return
	 */
	@GetMapping("/view-details/{id}")
	public String viewTicketDetails(@PathVariable (value = "id") long id, Model model) {
		Ticket ticket = ticketService.getTicketByTicketId(id);
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		CommentEntry comment = new CommentEntry();
		model.addAttribute("comment", comment);
		return "/ticket/ticket-details";
	}
	/**
	 * Creates a comment on a ticket's detail page
	 * @param id - id of ticket
	 * @param model
	 * @param auth
	 * @param comment - comment text
	 * @return
	 */
	@PostMapping("/make-comment/{id}")
	public String createComment(@PathVariable (value = "id") long id, Model model, Authentication auth, CommentEntry comment) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		comment.setCommentOrigin(ticketService.getTicketByTicketId(id));
		comment.setCommenter(userPrincipal.getUser());
		comment.setDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		commentEntryService.saveComment(comment);
		
		Ticket ticket = ticketService.getTicketByTicketId(id);
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
	/**
	 * Unassigns a ticket from the logged in user
	 * @param id - id of ticket
	 * @param model
	 * @return
	 */
	@GetMapping("/drop-ticket/{id}")
	public String assignTicketToUser(@PathVariable (value = "id") long id, Model model) {
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(null);
		ticketService.saveTicket(ticket);
		
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
	/**
	 * Assigns a ticket to the logged in user
	 * @param id - id of ticket
	 * @param model
	 * @param auth
	 * @return
	 */
	@GetMapping("/take-ticket/{id}")
	public String dropTicketOfUser(@PathVariable (value = "id") long id, Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(userPrincipal.getUser());
		ticketService.saveTicket(ticket);
		
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
}
