package com.jtran98.BugTracker.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jtran98.BugTracker.enums.StatusEnum;
import com.jtran98.BugTracker.model.CommentEntry;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.TicketFile;
import com.jtran98.BugTracker.security.UserPrincipal;
import com.jtran98.BugTracker.service.CommentEntryService;
import com.jtran98.BugTracker.service.LogEntryService;
import com.jtran98.BugTracker.service.TicketFileService;
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
	private TicketFileService ticketFileService;
	
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
	@GetMapping("/my-assigned-tickets")
	public String getAssignedTicketsOfCurrentUser(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
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
	@GetMapping("/my-submitted-tickets")
	public String getSubmittedTicketsOfCurrentUser(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
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
	@GetMapping("/my-project-tickets")
	public String getTicketsByProjectId(Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		model.addAttribute("viewTickets", ticketService.getTicketsOfProject(userPrincipal.getProjectId()));
		model.addAttribute("viewProjectTickets", true);
		model.addAttribute("ticketComparator", ticketComparator);
		return "/ticket/view-tickets";
	}
	
	
	/**
	 * Saves ticket to the repository. Changes function depending on if the ticket was new, or already existed and is just being modified
	 * @param ticket - object taken from thymeleaf template
	 * @param model
	 * @param authentication
	 * @return
	 */
	@PostMapping("/save-ticket")
	public String makeNewTicket(@ModelAttribute("modifyTicket") Ticket ticket,  Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
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
				//If status gets changed to either TAKEN or OPEN, adjust assigned user accordingly
				if(ticket.getStatus().equals(StatusEnum.TAKEN)){
					ticket.setAssignedUser(userPrincipal.getUser());
				}
				else if(ticket.getStatus().equals(StatusEnum.OPEN)){
					ticket.setAssignedUser(null);
				}
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
	public String deleteTicket(@PathVariable (value = "id") long id, Model model, Authentication authentication) {
		//Deletes all foreign constraint entities first
		commentEntryService.deleteAllCommentsOfTicket(id);
		logEntryService.deleteAllLogsOfTicket(id);
		ticketFileService.deleteAllFilesOfTicket(id);
		ticketService.deleteTicketByTicketId(id);
		
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
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
		model.addAttribute("ticketFiles", ticketFileService.getFilesOfTicket(id));
		CommentEntry comment = new CommentEntry();
		model.addAttribute("comment", comment);
		return "/ticket/ticket-details";
	}
	/**
	 * Creates a comment on a ticket's detail page
	 * @param id - id of ticket
	 * @param model
	 * @param authentication
	 * @param comment - comment text
	 * @return
	 */
	@PostMapping("/make-comment/{id}")
	public String createComment(@PathVariable (value = "id") long id, Model model, Authentication authentication, CommentEntry comment) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		comment.setCommentOrigin(ticketService.getTicketByTicketId(id));
		comment.setCommenter(userPrincipal.getUser());
		comment.setDate(java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
		commentEntryService.saveComment(comment);
		
		model.addAttribute("ticketDetails", ticketService.getTicketByTicketId(id));
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		model.addAttribute("ticketFiles", ticketFileService.getFilesOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
	@PostMapping("/upload-file/{id}")
	public String submitFile(@PathVariable(value = "id") long id, @RequestParam("file") MultipartFile file, Model model, Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Ticket ticket = ticketService.getTicketByTicketId(id);
		try {
			ticketFileService.saveTicketFile(file, ticket, userPrincipal.getUser());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("ticketDetails", ticket);
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		model.addAttribute("ticketFiles", ticketFileService.getFilesOfTicket(id));
		CommentEntry nextComment = new CommentEntry();
		model.addAttribute("comment", nextComment);
		return "ticket/ticket-details";
	}
	/**
	 * Allows for file download
	 * @param id - id of file
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/download-file/{id}")
    public String downloadFile(@PathVariable(value = "id") long id, HttpServletResponse response, Model model) throws IOException {
		
        TicketFile ticketFile = ticketFileService.getFile(id);
        byte[] byteArray =  ticketFile.getData();
        response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType()); 
        response.setHeader("Content-Disposition", "attachment; filename=" + ticketFile.getName());
        response.setContentLength(byteArray.length);
        OutputStream outputStream = response.getOutputStream();
        try {
            outputStream.write(byteArray, 0, byteArray.length);
        } finally {
        	outputStream.flush();
            outputStream.close();
        }
        
        model.addAttribute("ticketDetails", ticketService.getTicketByTicketId(id));
		model.addAttribute("commentEntries", commentEntryService.getCommentsOfTicket(id));
		model.addAttribute("logEntries", logEntryService.getLogsOfTicket(id));
		model.addAttribute("ticketFiles", ticketFileService.getFilesOfTicket(id));
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
	public String assignTicketToUser(@PathVariable (value = "id") long id, Model model, Authentication authentication) {
		//Updates ticket
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(null);
		ticket.setStatus(StatusEnum.OPEN);
		ticketService.saveTicket(ticket);
		//Makes log for change
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Assigned User",
				userPrincipal.getUser().getFirstName()+" "+userPrincipal.getUser().getLastName(),
				"None", java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
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
	 * @param authentication
	 * @return
	 */
	@GetMapping("/take-ticket/{id}")
	public String dropTicketOfUser(@PathVariable (value = "id") long id, Model model, Authentication authentication) {
		//Updates ticket
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Ticket ticket = ticketService.getTicketByTicketId(id);
		ticket.setAssignedUser(userPrincipal.getUser());
		ticket.setStatus(StatusEnum.TAKEN);
		ticketService.saveTicket(ticket);
		//Makes log for change
		logEntryService.makeLogForChange(userPrincipal.getUser(), ticket, "Assigned User", "None", 
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
