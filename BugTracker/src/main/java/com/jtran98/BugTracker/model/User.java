package com.jtran98.BugTracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jtran98.BugTracker.enums.AuthorityEnum;

@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="user_id", nullable = false)
	private long userId;
	@OneToMany(mappedBy = "assignedUser")
	private Set<Ticket> assignedTickets;
	@OneToMany(mappedBy = "assignedUser")
	private Set<Ticket> submittedTickets;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	
	private long assignedProject;
	private String firstName;
	private String lastName;
	private boolean isActive;
	private AuthorityEnum role;
	
	public User() {
	}	
	public User(long userId, Set<Ticket> assignedTickets, Set<Ticket> submittedTickets, String username,
			String password, long assignedProject, String firstName, String lastName, boolean isActive, AuthorityEnum role) {
		super();
		this.userId = userId;
		this.assignedTickets = assignedTickets;
		this.submittedTickets = submittedTickets;
		this.username = username;
		this.password = password;
		this.assignedProject = assignedProject;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = isActive;
		this.role = role;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Set<Ticket> getAssignedTickets() {
		return assignedTickets;
	}
	public void setAssignedTickets(Set<Ticket> assignedTickets) {
		this.assignedTickets = assignedTickets;
	}
	public Set<Ticket> getSubmittedTickets() {
		return submittedTickets;
	}
	public void setSubmittedTickets(Set<Ticket> submittedTickets) {
		this.submittedTickets = submittedTickets;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getAssignedProject() {
		return assignedProject;
	}
	public void setAssignedProject(long assignedProject) {
		this.assignedProject = assignedProject;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean getActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public AuthorityEnum getRole() {
		return role;
	}
	public void setRole(AuthorityEnum role) {
		this.role = role;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
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
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
}
