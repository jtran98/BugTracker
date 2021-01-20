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
	 */
	public void saveTicket(Ticket ticket) {
		this.ticketRepository.save(ticket);
	}
	
	/**
	 * Get a ticket based on its id
	 */
	public Ticket getTicketByTicketId(long id) {
		return ticketRepository.findByTicketId(id);
	}
	
	/**
	 * Deletes a specific ticket based on its id
	 */
	public void deleteTicketByTicketId(long id) {
		ticketRepository.deleteById(id);
	}
	
	/**
	 * Get all tickets assigned to a user
	 */
	public List<Ticket> getTicketsOfAssignedUser(long id){
		return ticketRepository.findByAssignedUser_UserId(id);
	}
	
	/**
	 * Get all tickets that belong to a specific project
	 */
	public List<Ticket> getTicketsOfProject(long id){
		return ticketRepository.findByProjectSource_ProjectId(id);
	}
	
	public List<Ticket> getTicketsUserSubmitted(Long userId) {
		return ticketRepository.findBySubmitter_UserId(userId);
	}
	
}
