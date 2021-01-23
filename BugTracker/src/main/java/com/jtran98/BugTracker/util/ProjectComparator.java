package com.jtran98.BugTracker.util;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.Project;

/**
 * Comparator util for project objects
 * @author Jacky Tran
 *
 */
@Service
public class ProjectComparator implements Comparator<Project>{
	
	/**
	 * Compares alphabetically
	 */
	@Override
	public int compare(Project o1, Project o2) {
		return o1.getProjectName().compareTo(o2.getProjectName());
	}
	
}
