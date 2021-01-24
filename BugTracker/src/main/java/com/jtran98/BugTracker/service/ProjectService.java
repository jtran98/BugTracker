package com.jtran98.BugTracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtran98.BugTracker.model.Project;
import com.jtran98.BugTracker.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	/**
	 * Finds project by project id
	 * @param projectId - project id
	 * @return
	 */
	public Project getProjectById(long projectId) {
		return this.projectRepository.findByProjectId(projectId);
	}
	/**
	 * Returns all projects
	 * @return
	 */
	public List<Project> getAllProjects(){
		return projectRepository.findAll();
	}
	/**
	 * Saves project to repository
	 * @param project
	 */
	public void saveProject(Project project) {
		projectRepository.save(project);
	}
	/**
	 * Deletes project by project id
	 * @param id
	 */
	public void deleteProject(long projectId) {
		projectRepository.deleteById(projectId);
	}
}
