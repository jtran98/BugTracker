//package com.jtran98.BugTracker.model;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@Entity
//public class CommentEntry {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private long commentId;
//	
//	private long commenterId;
//	private long ticketId;
//	private String commenterFirstName;
//	private String commenterLastName;
//	private String description;
//	private String date;
//	public CommentEntry(long commentId, long commenterId, long ticketId, String commenterFirstName,
//			String commenterLastName, String description, String date) {
//		super();
//		this.commentId = commentId;
//		this.commenterId = commenterId;
//		this.ticketId = ticketId;
//		this.commenterFirstName = commenterFirstName;
//		this.commenterLastName = commenterLastName;
//		this.description = description;
//		this.date = date;
//	}
//	public long getCommentId() {
//		return commentId;
//	}
//	public void setCommentId(long commentId) {
//		this.commentId = commentId;
//	}
//	public long getCommenterId() {
//		return commenterId;
//	}
//	public void setCommenterId(long commenterId) {
//		this.commenterId = commenterId;
//	}
//	public long getTicketId() {
//		return ticketId;
//	}
//	public void setTicketId(long ticketId) {
//		this.ticketId = ticketId;
//	}
//	public String getCommenterFirstName() {
//		return commenterFirstName;
//	}
//	public void setCommenterFirstName(String commenterFirstName) {
//		this.commenterFirstName = commenterFirstName;
//	}
//	public String getCommenterLastName() {
//		return commenterLastName;
//	}
//	public void setCommenterLastName(String commenterLastName) {
//		this.commenterLastName = commenterLastName;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//	public String getDate() {
//		return date;
//	}
//	public void setDate(String date) {
//		this.date = date;
//	}
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (commentId ^ (commentId >>> 32));
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		CommentEntry other = (CommentEntry) obj;
//		if (commentId != other.commentId)
//			return false;
//		return true;
//	}
//	
//	
//}
