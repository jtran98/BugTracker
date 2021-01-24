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
	 * Sorts by if active, role, then alphabetically, with first name then last name
	 */
	@Override
	public int compare(User o1, User o2) {
		if(o1.isActive() && !o2.isActive()) {
			return -1;
		}
		else if(!o1.isActive() && o2.isActive()) {
			return 1;
		}
		int secondCompare = o1.getRole().compareTo(o2.getRole());
		if(secondCompare != 0) {
			return secondCompare;
		}
		int thirdCompare = o1.getFirstName().compareTo(o2.getFirstName());
		if(thirdCompare != 0) {
			return thirdCompare;
		}
		return o1.getLastName().compareTo(o2.getLastName());
	}

}
