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

import com.jtran98.BugTracker.enums.StatusEnum;
import com.jtran98.BugTracker.exception.FileStorageException;
import com.jtran98.BugTracker.model.CommentEntry;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.TicketFile;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.TicketRepository;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.CommentEntryService;
import com.jtran98.BugTracker.service.LogEntryService;
import com.jtran98.BugTracker.service.TicketService;
import com.jtran98.BugTracker.util.TicketComparator;

/**
 * Controller for all mappings relating to tickets
 * @author Jacky Tran
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
	 * Saves ticket to the repository. Changes function depending on if the ticket was new, or already existed and is just being modified
	 * @param ticket - object taken from thymeleaf template
	 * @param model
	 * @param auth
	 * @return
	 */
	@PostMapping("/save-ticket")
	public String makeNewTicket(@ModelAttribute("modifyTicket") Ticket ticket,  Model model, Authentication auth) {
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		//If ticket is new, set the creation date, submitter id, and project source as the submitter's current project. Otherwise, log the changes made to the ticket
		if(ticket.getCreationDate() == null || ticket.getCreationDate().equals("")) {
			ticket.setCreationDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			ticket.setMostRecentUpdateDate("N/A");
			ticket.setProjectSource(userPrincipal.getProjectTeam());
			ticket.setSubmitter(userPrincipal.getUser());;
			if(ticket.getStatus().toString().equalsIgnoreCase(StatusEnum.TAKEN.toString())) {
				ticket.setAssignedUser(userPrincipal.getUser());
			}
		}
		else {
			ticket.setMostRecentUpdateDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			Ticket oldTicket = ticketService.getTicketByTicketId(ticket.getTicketId());
			/**
			 * This is disgusting but I can't think of any other way to check 5 values at once to make 5 different entries
			 */
			if(!oldTicket.getTitle().equals(ticket.getTitle())) {
				logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Title", oldTicket.getTitle(),
						ticket.getTitle(), java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			}
			if(!oldTicket.getDescription().equals(ticket.getDescription())) {
				logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Description", oldTicket.getDescription(),
						ticket.getDescription(), java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			}
			if(!oldTicket.getType().toString().equals(ticket.getType().toString())) {
				logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Type", oldTicket.getType().toString(),
						ticket.getType().toString(), java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			}
			if(!oldTicket.getPriority().toString().equals(ticket.getPriority().toString())) {
				logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Priority", oldTicket.getPriority().toString(),
						ticket.getPriority().toString(), java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			}
			if(!oldTicket.getStatus().toString().equals(ticket.getStatus().toString())) {
				logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Status", oldTicket.getStatus().toString(),
						ticket.getStatus().toString(), java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
			}
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
	 * Deletes ticket and all accompanying comments
	 * @param id - id of ticket
	 * @return
	 */
	@GetMapping("/delete-ticket/{id}")
	public String deleteTicket(@PathVariable (value = "id") long id, Model model, Authentication auth) {
		commentEntryService.deleteAllCommentsOfTicket(id);
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
	public String assignTicketToUser(@PathVariable (value = "id") long id, Model model, Authentication auth) {
		//Updates ticket
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(null);
		ticket.setStatus(StatusEnum.OPEN);
		ticketService.saveTicket(ticket);
		//Makes log for change
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Assigned User",
				userPrincipal.getUser().getFirstName()+" "+userPrincipal.getUser().getLastName(),
				"No one", java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		//Passes values to reload the page
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
		//Updates ticket
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(userPrincipal.getUser());
		ticket.setStatus(StatusEnum.TAKEN);
		ticketService.saveTicket(ticket);
		//Makes log for change
		logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Assigned User", "No one", 
				userPrincipal.getUser().getFirstName()+" "+userPrincipal.getUser().getLastName(),
				java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		//Passes values to reload the page
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
}
