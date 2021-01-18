package com.jtran98.BugTracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.UserRepository;

@Service
public class UserPrincipalDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	public UserPrincipalDetailsService(){}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserPrincipal userPrincipal = new UserPrincipal(user);
		return userPrincipal;
	}

}
