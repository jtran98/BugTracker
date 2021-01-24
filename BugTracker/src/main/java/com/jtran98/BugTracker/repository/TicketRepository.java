package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtran98.BugTracker.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	/**
	 * Find all tickets by user_id
	 * @param userId - user id
	 * @return
	 */
	public List<Ticket> findByAssignedUser_UserId(Long userId);
	/**
	 * Finds all tickets by project_id
	 * @param projectId - project id
	 * @return 
	 */
	public List<Ticket> findByProjectSource_ProjectId(Long projectId);
	/**
	 * Finds ticket by ticket id
	 * @param ticketId - ticket id
	 * @return
	 */
	public Ticket findByTicketId(Long ticketId);
	/**
	 * Finds ticket by submitter_id
	 * @param userId - submitter id
	 * @return
	 */
	public List<Ticket> findBySubmitter_UserId(Long userId);
	/**
	 * Deletes ticket by ticket id
	 * @param ticketId - ticket id
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM tickets WHERE ticket_id = ?1", nativeQuery = true)
	public void deleteByTicketId(Long ticketId);
}
