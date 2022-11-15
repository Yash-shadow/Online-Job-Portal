package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Candidate;
import com.model.Project;

public interface ProjectDAO extends JpaRepository<Project, Integer> {
	
	public Project findByProjectNameAndCandidate(String projectName,Candidate candidate);
 

}
