package com.jtran98.BugTracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jtran98.BugTracker.enums.PriorityEnum;
import com.jtran98.BugTracker.enums.StatusEnum;
import com.jtran98.BugTracker.enums.TypeEnum;

/**
 * Ticket model, contains a variety of properties, and is tied to many other entities. Refer to the ERD for more details
 * @author Jacky Tran
 *
 */
@Entity
@Table(name = "Tickets")
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ticket_id", nullable = false)
	private long ticketId;
	
	@OneToMany(mappedBy = "logOrigin")
	private Set<LogEntry> logEntryList;
	@OneToMany(mappedBy = "commentOrigin")
	private Set<CommentEntry> commentList;
	@OneToMany(mappedBy = "fileOrigin")
	private Set<TicketFile> ticketFiles;
	
	@ManyToOne
	@JoinColumn(name = "assigned_id")
	private User assignedUser;
	@ManyToOne
	@JoinColumn(name = "submitter_id", nullable = false)
	private User submitter;
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project projectSource;
	
	private String title;
	private String description;
	private PriorityEnum priority;
	private StatusEnum status;
	private TypeEnum type;
	private String creationDate;
	private String mostRecentUpdateDate;
	
	public Ticket() {
	}
	public long getTicketId() {
		return ticketId;
	}
	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}
	public Set<TicketFile> getTicketFiles() {
		return ticketFiles;
	}
	public void setTicketFiles(Set<TicketFile> ticketFiles) {
		this.ticketFiles = ticketFiles;
	}
	public Set<LogEntry> getLogEntryList() {
		return logEntryList;
	}
	public void setLogEntryList(Set<LogEntry> logEntryList) {
		this.logEntryList = logEntryList;
	}
	public Set<CommentEntry> getCommentList() {
		return commentList;
	}
	public void setCommentList(Set<CommentEntry> commentList) {
		this.commentList = commentList;
	}
	public User getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}
	public User getSubmitter() {
		return submitter;
	}
	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}
	public Project getProjectSource() {
		return projectSource;
	}
	public void setProjectSource(Project projectSource) {
		this.projectSource = projectSource;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PriorityEnum getPriority() {
		return priority;
	}
	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public TypeEnum getType() {
		return type;
	}
	public void setType(TypeEnum type) {
		this.type = type;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getMostRecentUpdateDate() {
		return mostRecentUpdateDate;
	}
	public void setMostRecentUpdateDate(String mostRecentUpdateDate) {
		this.mostRecentUpdateDate = mostRecentUpdateDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ticketId ^ (ticketId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (ticketId != other.ticketId)
			return false;
		return true;
	}
}