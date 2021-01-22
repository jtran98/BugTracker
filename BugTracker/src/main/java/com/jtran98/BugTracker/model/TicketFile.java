package com.jtran98.BugTracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * File (if uploaded) tied to a specific ticket. Each ticket can only have 1 ticket file
 * @author Jacky Tran
 *
 */
@Entity
@Table(name = "Ticket_Files")
public class TicketFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="file_id", nullable = false)
	private long fileId;
	
	@ManyToOne
	@JoinColumn(name = "ticket_id")
	private Ticket fileOrigin;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User uploader;
	
	@Lob
	@Column(length = 65535)
	private byte[] data;
	
	private String name;
	private String fileType;
	
	public TicketFile() {
	}
	public TicketFile(String name, String fileType, byte[] data, Ticket fileOrigin, User uploader) {
		this.name = name;
		this.fileType = fileType;
		this.data = data;
		this.fileOrigin = fileOrigin;
		this.uploader = uploader;
	}
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public Ticket getFileOrigin() {
		return fileOrigin;
	}
	public void setFileOrigin(Ticket fileOrigin) {
		this.fileOrigin = fileOrigin;
	}
	public User getUploader() {
		return uploader;
	}
	public void setUploader(User uploader) {
		this.uploader = uploader;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fileId ^ (fileId >>> 32));
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
		TicketFile other = (TicketFile) obj;
		if (fileId != other.fileId)
			return false;
		return true;
	}
}