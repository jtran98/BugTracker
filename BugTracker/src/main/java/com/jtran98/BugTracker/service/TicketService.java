package com.jtran98.BugTracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.TicketRepository;

@Service
public class TicketService{
	
	@Autowired
	private TicketRepository ticketRepository;
	
	
	/**
	 * Finds all tickets
	 */
	public List<Ticket> getAllTickets(){
		return ticketRepository.findAll();
	}
	/**
	 * Saves a ticket to the repository
	 * @param ticket - ticket
	 */
	public void saveTicket(Ticket ticket) {
		this.ticketRepository.save(ticket);
	}
	/**
	 * Get a ticket based on its id
	 * @param id - ticket id
	 */
	public Ticket getTicketByTicketId(long id) {
		return ticketRepository.findByTicketId(id);
	}
	
	/**
	 * Deletes a specific ticket based on its id
	 * @param id - ticket id
	 */
	public void deleteTicketByTicketId(long id) {
		ticketRepository.deleteByTicketId(id);
	}
	
	/**
	 * Get tickets of assigned user
	 * @param id - user id
	 * @return
	 */
	public List<Ticket> getTicketsOfAssignedUser(long id){
		return ticketRepository.findByAssignedUser_UserId(id);
	}
	
	/**
	 * Get all tickets that belong to a specific project
	 * @param id - project id
	 * @return
	 */
	public List<Ticket> getTicketsOfProject(long id){
		return ticketRepository.findByProjectSource_ProjectId(id);
	}
	/**
	 * Gets all tickets the user submitted
	 * @param userId - id of user
	 * @return
	 */
	public List<Ticket> getTicketsUserSubmitted(long userId) {
		return ticketRepository.findBySubmitter_UserId(userId);
	}
	/**
	 * Sets all tickets to have null values for their assigned user ids
	 * @param userId - id of user
	 */
	public void removeAllTicketAssignedAssociation(long userId) {
		for(Ticket ticket : ticketRepository.findByAssignedUser_UserId(userId)) {
			ticket.setAssignedUser(null);
			ticketRepository.save(ticket);
		}
	}
}
