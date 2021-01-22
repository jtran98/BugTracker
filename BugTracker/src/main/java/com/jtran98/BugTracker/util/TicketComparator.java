package com.jtran98.BugTracker.util;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.Ticket;

/**
 * Simple comparator util for Ticket objects. Sorts By priority, then type (if the priority level is the same), and then finally status.
 * Note: Do not modify the enums compared, otherwise the sorts will break.
 * @author Jacky Tran
 *
 */
@Service
public class TicketComparator implements Comparator<Ticket>{
	
	public TicketComparator() {}
	@Override
	public int compare(Ticket o1, Ticket o2) {
		int firstCompare = o1.getStatus().compareTo(o2.getStatus());
		if(firstCompare != 0) {
			return firstCompare;
		}
		int secondCompare = o1.getPriority().compareTo(o2.getPriority());
		if(secondCompare != 0) {
			return secondCompare;
		}
		return o1.getType().compareTo(o2.getType());
	}
}