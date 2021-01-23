package com.jtran98.BugTracker.util;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.User;

/**
 * Comparator util for User
 * @author Jacky Tran
 *
 */
@Service
public class UserComparator implements Comparator<User>{

	/**
	 * Sorts by role, then alphabetically, with first name then last name
	 */
	@Override
	public int compare(User o1, User o2) {
		int firstCompare = o1.getRole().compareTo(o2.getRole());
		if(firstCompare != 0) {
			return firstCompare;
		}
		int secondCompare = o1.getFirstName().compareTo(o2.getFirstName());
		if(secondCompare != 0) {
			return secondCompare;
		}
		return o1.getLastName().compareTo(o2.getLastName());
	}

}
