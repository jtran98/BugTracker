//package com.jtran98.BugTracker.model;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
// comment
///**
// * Project model, contains a name, id and list of members
// * @author Jacky
// *
// */
//@Entity
//public class Project {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private long projectId;
//	@OneToMany(mappedBy = "members")
//	private Set<Long> membersList = new HashSet<Long>();
//	@OneToMany(mappedBy = "tickets")
//	private Set<Long> ticketsList = new HashSet<Long>();
//	
//	private String projectName;
//
//	public Project(String projectName) {
//		super();
//		this.projectName = projectName;
//	}
//
//	public long getProjectId() {
//		return projectId;
//	}
//
//	public void setProjectId(long projectId) {
//		this.projectId = projectId;
//	}
//
//	public Set<Long> getMembersList() {
//		return membersList;
//	}
//
//	public void setMembersList(Set<Long> membersList) {
//		this.membersList = membersList;
//	}
//
//	public Set<Long> getTicketsList() {
//		return ticketsList;
//	}
//
//	public void setTicketsList(Set<Long> ticketsList) {
//		this.ticketsList = ticketsList;
//	}
//
//	public String getProjectName() {
//		return projectName;
//	}
//
//	public void setProjectName(String projectName) {
//		this.projectName = projectName;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (projectId ^ (projectId >>> 32));
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
//		Project other = (Project) obj;
//		if (projectId != other.projectId)
//			return false;
//		return true;
//	}
//	
//	
//}
