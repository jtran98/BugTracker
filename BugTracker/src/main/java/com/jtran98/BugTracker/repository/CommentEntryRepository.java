package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtran98.BugTracker.model.CommentEntry;

@Repository
public interface CommentEntryRepository extends JpaRepository<CommentEntry, Long>{
	
	/**
	 * Finds all comment entries for a specific ticket
	 * @param ticketId - ticket id
	 * @return
	 */
	public List<CommentEntry> findByCommentOrigin_TicketId(Long ticketId);
}