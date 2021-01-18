//package com.jtran98.BugTracker.model;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//@Entity
//public class LogEntry {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private long historyId;
//	
//	private long updaterId;
//	private long ticketId;
//	private String property;
//	private String oldValue;
//	private String newValue;
//	private String date;
//	
//	public LogEntry(long historyId, long updaterId, long ticketId, String property, String oldValue,
//			String newValue, String date) {
//		super();
//		this.historyId = historyId;
//		this.updaterId = updaterId;
//		this.ticketId = ticketId;
//		this.property = property;
//		this.oldValue = oldValue;
//		this.newValue = newValue;
//		this.date = date;
//	}
//	
//	public long getHistoryId() {
//		return historyId;
//	}
//	public void setHistoryId(long historyId) {
//		this.historyId = historyId;
//	}
//	public long getUpdaterId() {
//		return updaterId;
//	}
//	public void setUpdaterId(long updaterId) {
//		this.updaterId = updaterId;
//	}
//	public long getTicketId() {
//		return ticketId;
//	}
//	public void setTicketId(long ticketId) {
//		this.ticketId = ticketId;
//	}
//	public String getProperty() {
//		return property;
//	}
//	public void setProperty(String property) {
//		this.property = property;
//	}
//	public String getOldValue() {
//		return oldValue;
//	}
//	public void setOldValue(String oldValue) {
//		this.oldValue = oldValue;
//	}
//	public String getNewValue() {
//		return newValue;
//	}
//	public void setNewValue(String newValue) {
//		this.newValue = newValue;
//	}
//	public String getDate() {
//		return date;
//	}
//	public void setDate(String date) {
//		this.date = date;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (historyId ^ (historyId >>> 32));
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		LogEntry other = (LogEntry) obj;
//		if (historyId != other.historyId)
//			return false;
//		return true;
//	}
//}
