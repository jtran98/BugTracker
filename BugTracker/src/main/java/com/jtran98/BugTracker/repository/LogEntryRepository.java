package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtran98.BugTracker.model.LogEntry;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long>{
	
	/**
	 * Finds all log entries of a specific ticket
	 * @param ticketId - ticket id
	 * @return
	 */
	public List<LogEntry> findByLogOrigin_TicketId(Long ticketId);
}
