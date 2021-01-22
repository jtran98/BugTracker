package com.jtran98.BugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.LogEntry;
import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.LogEntryRepository;

@Service
public class LogEntryService {
	@Autowired
	private LogEntryRepository logEntryRepository;
	
	/**
	 * Gets all logs of a given ticket
	 * @param id - id of ticket
	 * @return
	 */
	public List<LogEntry> getLogsOfTicket(Long id){
		return logEntryRepository.findByLogOrigin_TicketId(id);
	}
	/**
	 * Deletes all logs tied to a specific ticket
	 * @param id - id of ticket
	 */
	public void deleteAllLogsOfTicket(Long id) {
		this.logEntryRepository.deleteAllLogsByTicketId(id);
	}
	/**
	 * Creates a new log entry
	 * @param updater - user who changed the ticket
	 * @param logOrigin - ticket being modified
	 * @param property - property being modified (eg: changing the description, or lowering the priority from HIGH to LOW)
	 * @param oldValue - old value
	 * @param newValue - new value
	 * @param date - date the change occurred
	 */
	public void makeLogForChange(User updater, Ticket logOrigin, String property, String oldValue, String newValue, String date) {
		LogEntry entry = new LogEntry(updater, logOrigin, property, oldValue, newValue, date);
		this.logEntryRepository.save(entry);
	}
}
