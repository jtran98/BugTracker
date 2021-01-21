package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtran98.BugTracker.model.CommentEntry;

@Repository
public interface CommentEntryRepository extends JpaRepository<CommentEntry, Long>{
	
	/**
	 * Finds all comment entries for a specific ticket
	 * @param ticketId - ticket id
	 * @return
	 */
	public List<CommentEntry> findByCommentOrigin_TicketId(Long ticketId);
	/**
	 * Deletes all COMMENTS by ticket
	 * @param ticketId
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM comments WHERE ticket_id = ?1", nativeQuery = true)
	public void deleteAllCommentsByTicketId(Long ticketId);
}