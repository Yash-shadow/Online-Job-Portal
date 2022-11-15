package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.util.NoSuchElementException;

import com.dao.ProjectDAO;
import com.exception.ProjectNotFoundException;
import com.model.Candidate;
import com.model.Project;
@Service
public class ProjectService {
	@Autowired
	ProjectDAO projectDao;

    public void remove(Project project) throws IllegalArgumentException,NoSuchElementException {
       Project p = projectDao.findById(project.getProjectId()).get();
       projectDao.delete(project);
        
    }

    public void addProject(Project p1) {
       projectDao.save(p1);
        
    }

   

    public void removeProjectById(int projectid) throws ProjectNotFoundException {
        try {
            projectDao.deleteById(projectid);
            }
            catch(EmptyResultDataAccessException e) {
                throw new ProjectNotFoundException("the provided id is not in this world");
            }
    }

	public void removeProjectByNameAndCandidate(Candidate candidate,String projectName) throws ProjectNotFoundException {
		Project project = projectDao.findByProjectNameAndCandidate( projectName,candidate);
		projectDao.delete(project);
		if( project==null) {
			throw new ProjectNotFoundException(projectName);
		}
		
	}

}
