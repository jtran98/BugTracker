package com.jtran98.BugTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jtran98.BugTracker.enums.AuthorityEnum;
import com.jtran98.BugTracker.model.User;
import com.jtran98.BugTracker.repository.UserRepository;

@Component
@ConditionalOnProperty(name = "app.init-db", havingValue = "true")
public class DatabaseInitializer implements CommandLineRunner{
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void run(String... args) throws Exception {
		User defaultAdmin = new User();
		defaultAdmin.setActive(true);
		defaultAdmin.setUsername("admin");
		defaultAdmin.setPassword(passwordEncoder.encode("admin"));
		defaultAdmin.setMatchingPassword(passwordEncoder.encode("admin"));
		defaultAdmin.setRole(AuthorityEnum.ADMINISTRATOR);
		//needed for userComparator util
		defaultAdmin.setFirstName("Hello");
		defaultAdmin.setLastName("World");
		this.userRepository.save(defaultAdmin);
	}
	
}
