package com.jtran98.BugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.LogEntry;
import com.jtran98.BugTracker.repository.LogEntryRepository;

@Service
public class LogEntryService {
	@Autowired
	private LogEntryRepository logEntryRepository;
	
	public List<LogEntry> getLogsOfTicket(Long id){
		return logEntryRepository.findByLogOrigin_TicketId(id);
	}
}
