package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Employer;

@Repository
public interface EmployerDAO extends JpaRepository<Employer, Integer> {
	List<Employer> findByLocation(String location);
	
	Employer findByEmailId(String emailId);
	
}
