package com.jtran98.BugTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtran98.BugTracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	/**
	 * Finds user by their id
	 * @param userId - user id
	 * @return
	 */
	public User findByUserId(Long userId);
	/**
	 * Find user by their login username
	 * @param Username - user's username
	 * @return
	 */
	public User findByUsername(String Username);
	/**
	 * Find users belonging to a project
	 * @param projectId - project id
	 * @return
	 */
	public List<User> findByProjectTeam_projectId(Long projectId);
	
}