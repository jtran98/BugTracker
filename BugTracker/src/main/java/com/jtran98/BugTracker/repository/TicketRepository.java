package com.jtran98.BugTracker.repository;

import java.util.List;

import javax.persistence.Embedded;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jtran98.BugTracker.model.Ticket;
import com.jtran98.BugTracker.model.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	//example of 2 variables for native queries
	//... tickets.developer_id = ?1 and tickets.projectId=?2"
	
	//NOTE: JPA has keywords you can use for method names to do specific mySQL commands automatically.
	//As in, you do not have to actually create queries and can just use specific method names instead
	
	//finds all tickets by assigned_id
	//here is a native query alternative: @Query(value="SELECT * FROM bugtracker.tickets WHERE tickets.assigned_id = ?1", nativeQuery=true)
	public List<Ticket> findByAssignedUser_UserId(Long id);
	
	//finds all tickets by project_id
	public List<Ticket> findByProjectId(Long id);
}
