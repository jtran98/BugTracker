package com.jtran98.BugTracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jtran98.BugTracker.enums.PriorityEnum;
import com.jtran98.BugTracker.enums.StatusEnum;
import com.jtran98.BugTracker.enums.TypeEnum;

/**
 * Ticket model, contains a name, description, list of developers working on ticket, a submitter, a project the ticket belongs to, and a priority level
 * @author Jacky
 *
 */
@Entity
@Table(name = "Tickets")
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ticket_id", nullable = false)
	private long ticketId;
	@ManyToOne
	@JoinColumn(name = "assigned_user")
	private User assignedUser;
	@ManyToOne
	@JoinColumn(name = "submitter")
	private User submitter;
//	private Set<LogEntry> historyList;
//	private Set<CommentEntry> commentList;
	
	private long projectId;
	private String title;
	private String description;
	private PriorityEnum priority;
	private StatusEnum status;
	private TypeEnum type;
	private String creationDate;
	//most recent update date
	private String updateDate;
	
	public Ticket() {
	}
	public Ticket(long ticketId, User assignedUser, User submitter, long projectId, String title, String description,
			PriorityEnum priority, StatusEnum status, TypeEnum type, String creationDate, String updateDate) {
		super();
		this.ticketId = ticketId;
		this.assignedUser = assignedUser;
		this.submitter = submitter;
		this.projectId = projectId;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.type = type;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
	}
	public long getTicketId() {
		return ticketId;
	}
	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
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
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
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
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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