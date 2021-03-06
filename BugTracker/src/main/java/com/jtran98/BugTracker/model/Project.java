package com.jtran98.BugTracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Project model, contains a name, id and list of members
 * @author Jacky Tran
 *
 */
@Entity
@Table(name = "Projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "project_id", nullable = false)
	private long projectId;
	
	@OneToMany(mappedBy = "projectTeam")
	private Set<User> membersList;
	@OneToMany(mappedBy = "projectSource")
	private Set<Ticket> ticketsList;
	@Column(nullable = false)
	private String projectName;
	@Column(nullable = false)
	private String projectDescription;
	
	public Project() {
	}
	
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public Set<User> getMembersList() {
		return membersList;
	}
	public void setMembersList(Set<User> membersList) {
		this.membersList = membersList;
	}
	public Set<Ticket> getTicketsList() {
		return ticketsList;
	}
	public void setTicketsList(Set<Ticket> ticketsList) {
		this.ticketsList = ticketsList;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (projectId ^ (projectId >>> 32));
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
		Project other = (Project) obj;
		if (projectId != other.projectId)
			return false;
		return true;
	}
	
	
}
