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

/**
 * Comment Entry POJO. Has IDs linking to the ticket it belongs to, the user who made the comment, and other expected values like description and date created
 * @author Jacky
 *
 */
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
	public CommentEntry(User commenter, Ticket commentOrigin, String description, String date) {
		super();
		this.commenter = commenter;
		this.commentOrigin = commentOrigin;
		this.description = description;
		this.date = date;
	}
	
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
