package com.jtran98.BugTracker.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.TicketFile;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.TicketFileRepository;

@Service
public class TicketFileService {
	
	@Autowired
	private TicketFileRepository ticketFileRepository;
	
	public TicketFile getFile(Long id) {
		return this.ticketFileRepository.findByFileId(id);
	}
	public List<TicketFile> getFilesOfTicket(Long id) {
		return ticketFileRepository.findByFileOrigin_TicketId(id);
	}
	public void saveTicketFile(MultipartFile file, Ticket fileOrigin, User uploader) throws IOException{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		TicketFile ticketFile = new TicketFile(fileName, file.getContentType(), file.getBytes(), fileOrigin, uploader);
		ticketFileRepository.save(ticketFile);
	}
	public void deleteFile(Long id) {
		this.ticketFileRepository.deleteByFileId(id);
	}
	public void deleteAllFilesOfTicket(Long id) {
		this.ticketFileRepository.deleteAllFilesByTicketId(id);
	}
}