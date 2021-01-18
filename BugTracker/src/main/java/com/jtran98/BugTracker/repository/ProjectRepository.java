package com.jtran98.BugTracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jtran98.BugTracker.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
}