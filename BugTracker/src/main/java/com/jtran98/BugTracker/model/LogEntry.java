package com.jtran98.BugTracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Comment entry POJO. Linked to a ticket, and the user who made the change.
 * @author Jacky Tran
 *
 */
@Entity
@Table(name = "Log_Entries")
public class LogEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="log_id", nullable = false)
	private long logId;
	
	@ManyToOne
	@JoinColumn(name = "updater_id", nullable = false)
	private User updater;
	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket logOrigin;
	
	private String property;
	private String oldValue;
	private String newValue;
	private String date;
	
	public LogEntry() {}
	
	public LogEntry(User updater, Ticket logOrigin, String property, String oldValue, String newValue, String date) {
		super();
		this.updater = updater;
		this.logOrigin = logOrigin;
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.date = date;
	}

	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public User getUpdater() {
		return updater;
	}
	public void setUpdater(User updater) {
		this.updater = updater;
	}
	public Ticket getLogOrigin() {
		return logOrigin;
	}
	public void setLogOrigin(Ticket logOrigin) {
		this.logOrigin = logOrigin;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
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
		result = prime * result + (int) (logId ^ (logId >>> 32));
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
		LogEntry other = (LogEntry) obj;
		if (logId != other.logId)
			return false;
		return true;
	}
}
