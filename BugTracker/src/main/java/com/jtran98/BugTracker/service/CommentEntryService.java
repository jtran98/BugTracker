package com.jtran98.BugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.CommentEntry;
import com.jtran98.BugTracker.repository.CommentEntryRepository;

@Service
public class CommentEntryService {
	@Autowired
	CommentEntryRepository commentEntryRepository;
	
	/**
	 * Return all ticket entries of a given ticket
	 * @param id - id of ticket
	 * @return
	 */
	public List<CommentEntry> getCommentsOfTicket(Long id){
		return commentEntryRepository.findByCommentOrigin_TicketId(id);
	}
	/**
	 * Saves a comment entity to the database
	 * @param comment
	 */
	public void saveComment(CommentEntry comment) {
		this.commentEntryRepository.save(comment);
	}
}