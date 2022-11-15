package com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;

public interface InterviewDAO extends JpaRepository<Interview, Integer> {
  Interview findByCandidateAndEmployerAndJob(Candidate candidate, Employer employer, Job job);
  List<Interview> findByJobAndPostInterviewStatus(Job job, PostInterviewStatus status);
  List<Interview> findByJobAndPreInterviewStatusAndPostInterviewStatus(Job job, PreInterviewStatus preInterviewStatus, PostInterviewStatus postInterviewStatus);
  List<Interview> findByJobAndPostInterviewStatusIsNot(Job job, PostInterviewStatus status);
  List<Interview> findByJobAndInterviewIdIsNot(Job job, int id);
  List<Interview> findByJob(Job job);
  
  Interview findByJobAndCandidateAndEmployer(Job job, Candidate candidate, Employer employer);
 List<Interview> findByPreInterviewStatusAndJob(PreInterviewStatus preStatus, Job job);
}
