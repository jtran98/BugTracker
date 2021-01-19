package com.jtran98.BugTracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
public class CommentEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="comment_id", nullable = false)
	private long commentId;
	
	@ManyToOne
	@JoinColumn(name = "commenter_id", nullable = false)
	private User commenter;
	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket commentOrigin;
	
	private String description;
	private String date;
	
	public CommentEntry() {}
	
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public User getCommenter() {
		return commenter;
	}
	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}
	public Ticket getCommentOrigin() {
		return commentOrigin;
	}
	public void setCommentOrigin(Ticket commentOrigin) {
		this.commentOrigin = commentOrigin;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commentId ^ (commentId >>> 32));
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
		CommentEntry other = (CommentEntry) obj;
		if (commentId != other.commentId)
			return false;
		return true;
	}
}
