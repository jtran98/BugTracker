//package com.jtran98.BugTracker;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.jtran98.BugTracker.model.CommentModel;
//import com.jtran98.BugTracker.model.PriorityEnum;
//import com.jtran98.BugTracker.model.ProjectModel;
//import com.jtran98.BugTracker.model.RoleEnum;
//import com.jtran98.BugTracker.model.StatusEnum;
//import com.jtran98.BugTracker.model.TicketHistoryModel;
//import com.jtran98.BugTracker.model.Ticket;
//import com.jtran98.BugTracker.model.TypeEnum;
//import com.jtran98.BugTracker.model.UserModel;
//
//@Component
//public class BootStrapData implements CommandLineRunner{
//	
//	private final CommentRepositoryController commentRepositoryController;
//	private final TicketRepositoryController ticketRepositoryController;
//	private final TicketHistoryRepositoryController ticketHistoryRepositoryController;
//	private final ProjectRepositoryController projectRepositoryController;
//	private final UserRepositoryController userRepositoryController;
//	
//	public BootStrapData(CommentRepositoryController commentRepositoryController,
//			TicketRepositoryController ticketRepositoryController,
//			TicketHistoryRepositoryController ticketHistoryRepositoryController,
//			ProjectRepositoryController projectRepositoryController,
//			UserRepositoryController userRepositoryController) {
//		super();
//		this.commentRepositoryController = commentRepositoryController;
//		this.ticketRepositoryController = ticketRepositoryController;
//		this.ticketHistoryRepositoryController = ticketHistoryRepositoryController;
//		this.projectRepositoryController = projectRepositoryController;
//		this.userRepositoryController = userRepositoryController;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		ProjectModel projectOne = new ProjectModel("Bug Tracker Back End");
//		ProjectModel projectTwo = new ProjectModel("Bug Tracker Front End");
//		UserModel userOne = new UserModel(new HashSet<Ticket>(), projectOne.getProjectId(), "John", "Smith", AuthorityEnum.DEVELOPER);
//		UserModel userTwo = new UserModel(new HashSet<Ticket>(), projectOne.getProjectId(), "Casey", "Barton", AuthorityEnum.PROJECTMANAGER);
//		UserModel userThree = new UserModel(new HashSet<Ticket>(), projectOne.getProjectId(), "Taylor", "Castilo", AuthorityEnum.DEVELOPER);
//		UserModel userFour = new UserModel(new HashSet<Ticket>(), projectTwo.getProjectId(), "Bernard", "Waters", AuthorityEnum.DEVELOPER);
//		UserModel userFive = new UserModel(new HashSet<Ticket>(), projectTwo.getProjectId(), "Pablo", "Wong", AuthorityEnum.PROJECTMANAGER);
//		UserModel userSix = new UserModel("Danny", "Fenton", AuthorityEnum.ADMINISTRATOR);
//		Ticket ticketOne = new Ticket(userTwo.getUserId(), projectOne.getProjectId(), "Finish the Bug Tracker",
//			"Let's go!", PriorityEnum.MEDIUM, StatusEnum.TAKEN, TypeEnum.EPIC, "2021-01-12T22:31:06.113", "2021-01-12T22:31:06.113");
//		projectOne.getTicketsList().add(ticketOne.getId());
//		userThree.getTicketsList().add(ticketOne);
//		Ticket ticketTwo = new Ticket(userThree.getUserId(), projectOne.getProjectId(), "Add OAuth to the",
//				"We really should get some security added.", PriorityEnum.HIGH, StatusEnum.TAKEN, TypeEnum.TASK, "2021-01-4T13:6:06.113", "2021-01-4T13:6:06.113");
//		userOne.getTicketsList().add(ticketTwo);
//		Ticket ticketThree = new Ticket(userFive.getUserId(), projectTwo.getProjectId(), "Can someone get me a coffee?",
//				"I forgot to get some this morning and I have a meeting with a client soon.", PriorityEnum.LOW, StatusEnum.OPEN, TypeEnum.BUG, java.time.LocalDateTime.now().toString(),
//				java.time.LocalDateTime.now().toString());
//	}
//}
