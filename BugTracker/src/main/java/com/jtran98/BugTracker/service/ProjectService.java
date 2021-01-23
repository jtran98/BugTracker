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
	
	public Project getProjectById(long id) {
		return this.projectRepository.findByProjectId(id);
	}
	/**
	 * Returns all projects
	 * @return
	 */
	public List<Project> getAllProjects(){
		return projectRepository.findAll();
	}
	public void saveProject(Project project) {
		projectRepository.save(project);
	}
	public void deleteProject(long id) {
		projectRepository.deleteById(id);
	}
}
