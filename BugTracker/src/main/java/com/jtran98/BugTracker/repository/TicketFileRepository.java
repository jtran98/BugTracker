package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtran98.BugTracker.model.TicketFile;

@Repository
public interface TicketFileRepository extends JpaRepository<TicketFile, String>{
	
	/**
	 * Finds ticket file by file_id
	 * @param fileId - file id
	 * @return
	 */
	public TicketFile findByFileId(Long fileId);
	/**
	 * Finds all ticket files by ticket_id
	 * @param ticketId - ticket id
	 * @return
	 */
	public List<TicketFile> findByFileOrigin_TicketId(Long ticketId);
	/**
	 * Deletes a ticket file by file id
	 * @param fileId - file id
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ticket_files WHERE file_id = ?1", nativeQuery = true)
	public void deleteByFileId(Long fileId);
	/**
	 * Deletes all TICKET FILES by ticket
	 * @param ticketId
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ticket_files WHERE ticket_id = ?1", nativeQuery = true)
	public void deleteAllFilesByTicketId(Long ticketId);
}