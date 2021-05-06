package com.jtran98.BugTracker.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.enums.StatusEnum;

/**
 * User object. Holds user's login credentials as well as information such as the project team they belong to, and tickets they're working on/have submitted
 * @author Jacky Tran
 *
 */
@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="user_id", nullable = false)
	private long userId;
	
	@OneToMany(mappedBy = "assignedUser")
	private Set<Ticket> assignedTickets;
	@OneToMany(mappedBy = "submitter")
	private Set<Ticket> submittedTickets;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project projectTeam;
	
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String matchingPassword;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private boolean isActive;
	@Column(nullable = false)
	private AuthorityEnum role;
	
	public User() {}
	
	public long getUserId() {
		return userId;
	}
	/**
	 * @return - list of tickets user has submitted and have not yet been completed (does not include tickets the user has both submitted and taken)
	 */
	public Set<Ticket> getSubmittedTicketsInProgressAndNotAssigned(){
		if(submittedTickets == null) {
			return null;
		}
		Set<Ticket> submittedInProgress = new HashSet<Ticket>();
		for(Ticket ticket : submittedTickets) {
			if(ticket.getAssignedUser() != null) {
				if(!ticket.getStatus().equals(StatusEnum.DONE) && ticket.getAssignedUser().getUserId() != this.userId) {
					submittedInProgress.add(ticket);
				}
			}
		}
		return submittedInProgress;
	}
	/**
	 * @return - list of tickets user has currently taken
	 */
	public Set<Ticket> getAssignedTicketsInProgress() {
		if(assignedTickets == null) {
			return null;
		}
		Set<Ticket> assignedInProgress = new HashSet<Ticket>();
		for(Ticket ticket : assignedTickets) {
			if(ticket.getStatus().equals(StatusEnum.TAKEN)) {
				assignedInProgress.add(ticket);
			}
		}
		return assignedInProgress;
		
	}
	/**
	 * Titles of all the tickets user has taken
	 * @return
	 */
	public String getAssignedTicketTitles() {
		if(assignedTickets == null) {
			return null;
		}
		String titles = "";
		for(Ticket ticket : assignedTickets) {
			if(ticket.getStatus().equals(StatusEnum.TAKEN))
			titles += "\""+ticket.getTitle() + "\", ";
		}
		//trim final ", " if user had any tickets
		if(titles.length() > 0) {
			return titles.substring(0, titles.length()-2);
		}
		return "Not currently assigned to any tickets";
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getMatchingPassword() {
		return matchingPassword;
	}
	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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
	public Project getProjectTeam() {
		return projectTeam;
	}
	public void setProjectTeam(Project projectTeam) {
		this.projectTeam = projectTeam;
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
	/**
	 * IMPORTANT:
	 * Both getters are required due to how spring works
	 */
	public boolean isActive() {
		return isActive;
	}
	public boolean getIsActive() {
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
