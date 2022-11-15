package com.dao;
import com.model.Skill;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Candidate;
@Repository
public interface CandidateDAO extends JpaRepository<Candidate, Integer>{

	public Candidate findByCandidateName(String candidateName);
	Candidate findByEmailId(String emailId);
	List<Candidate> findByExperienceBetween(int experience_lb, int experience_ub);
	List<Candidate> findAllByEducationQualification(String qualification);
}

