//package com.jtran98.BugTracker.repository;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.jtran98.BugTracker.enums.AuthorityEnum;
//import com.jtran98.BugTracker.model.Ticket;
//import com.jtran98.BugTracker.model.User;
//
//@Service
//public class dbInit implements CommandLineRunner{
//	@Autowired
//	private UserRepository userRepository;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	public dbInit() {}
//	public void run(String... args) throws Exception{
//		
//		this.userRepository.deleteAll();
//		User admin = new User(1, new HashSet<Ticket>(), new HashSet<Ticket>(), "jtran", passwordEncoder.encode("jtran123"), 1, "Jacky", "Tran", AuthorityEnum.ADMINISTRATOR);
//		User pm = new User(2, new HashSet<Ticket>(), new HashSet<Ticket>(), "pm", passwordEncoder.encode("pm123"), 1, "Jo", "Ez", AuthorityEnum.PROJECTMANAGER);
//		User dev = new User(3, new HashSet<Ticket>(), new HashSet<Ticket>(), "dev", passwordEncoder.encode("dev123"), 1, "Banana", "Man", AuthorityEnum.DEVELOPER);
//		User sub = new User(4, new HashSet<Ticket>(), new HashSet<Ticket>(), "sub", passwordEncoder.encode("sub123"), 1, "Super", "Sentai", AuthorityEnum.SUBMITTER);
//		
//		List<User> users = Arrays.asList(admin, pm, dev, sub);
//		this.userRepository.saveAll(users);
//	}
//}
