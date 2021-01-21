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
	public void makeLogForChange(User updater, Ticket logOrigin, String property, String oldValue, String newValue, String date) {
		LogEntry entry = new LogEntry(updater, logOrigin, property, oldValue, newValue, date);
		this.logEntryRepository.save(entry);
	}
}
