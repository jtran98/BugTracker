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
	
	/**
	 * Gets file by file id
	 * @param id - file id
	 * @return
	 */
	public TicketFile getFile(Long id) {
		return this.ticketFileRepository.findByFileId(id);
	}
	/**
	 * Gets a list of files tied to ticket
	 * @param id - ticket id
	 * @return
	 */
	public List<TicketFile> getFilesOfTicket(Long id) {
		return ticketFileRepository.findByFileOrigin_TicketId(id);
	}
	/**
	 * Saves a ticket file
	 * @param file
	 * @param fileOrigin
	 * @param uploader
	 * @throws IOException
	 */
	public void saveTicketFile(MultipartFile file, Ticket fileOrigin, User uploader) throws IOException{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		TicketFile ticketFile = new TicketFile(fileName, file.getContentType(), file.getBytes(), fileOrigin, uploader);
		ticketFileRepository.save(ticketFile);
	}
	/**
	 * Deletes a ticket file by file id
	 * @param id - file id
	 */
	public void deleteFile(Long id) {
		this.ticketFileRepository.deleteByFileId(id);
	}
	/**
	 * Deletes all files related to a ticket
	 * @param id - ticket id
	 */
	public void deleteAllFilesOfTicket(Long id) {
		this.ticketFileRepository.deleteAllFilesByTicketId(id);
	}
}