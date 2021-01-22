package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtran98.BugTracker.model.LogEntry;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long>{
	
	/**
	 * Finds all log entries of a specific ticket
	 * @param ticketId - ticket id
	 * @return
	 */
	public List<LogEntry> findByLogOrigin_TicketId(Long ticketId);
	/**
	 * Deletes all ENTRIES by ticket
	 * @param ticketId - ticket id
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM log_entries WHERE ticket_id = ?1", nativeQuery = true)
	public void deleteAllLogsByTicketId(Long ticketId);
}
