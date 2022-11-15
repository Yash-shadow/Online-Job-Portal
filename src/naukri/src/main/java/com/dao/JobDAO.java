package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Job;

@Repository
public interface JobDAO extends JpaRepository<Job, Integer> {
	
	List<Job> findByIndustry(String industry);
	List<Job> findByLocation(String location);
	
}
