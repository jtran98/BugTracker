package com.jtran98.BugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Finds user by their user id
	 * @param userId - user id
	 * @return
	 */
	public User getUser(long userId) {
		return this.userRepository.findByUserId(userId);
	}
	/**
	 * Finds all users
	 * @return
	 */
	public List<User> getAllUsers(){
		return this.userRepository.findAll();
	}
	/**
	 * Finds all users belong to a specific project
	 * @param projectId - project id
	 * @return
	 */
	public List<User> getProjectMembers(Long projectId){
		return this.userRepository.findByProjectTeam_projectId(projectId);
	}
	/**
	 * Sees if a user with a username exists
	 * @param username - username to check
	 * @return
	 */
	public boolean hasUserWithUsername(String username) {
		if(this.userRepository.findFirstByUsername(username) != null) {
			return true;
		}
		return false;
	}
	/**
	 * Finds first user with username (should only be one max)
	 * @param username
	 * @return
	 */
	public User findUserWithUsername(String username) {
		return this.userRepository.findFirstByUsername(username);
	}
	/**
	 * Saves user to repository
	 * @param user
	 */
	public void saveUser(User user) {
		this.userRepository.save(user);
	}
	/**
	 * Deletes user by userId
	 * @param userId - user id
	 */
	public void deleteUser(long userId) {
		this.userRepository.deleteById(userId);
	}
}
