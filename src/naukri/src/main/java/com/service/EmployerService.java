package com.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;




import com.dao.EmployerDAO;
import com.dao.InterviewDAO;
import com.dto.EmployerDTO;


import com.enums.JobStatus;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.AllInterviewsNotCompletedException;
import com.exception.JobAlreadyClosedWithCandidateSelectedException;
import com.exception.NoEmployersException;
import com.exception.NoSuchEmployerFoundException;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;

@Service
public class EmployerService {

  @Autowired
  EmployerDAO employerDAO;
  
  @Autowired
  InterviewDAO interviewDAO;
  
  public void addEmployer(EmployerDTO dto) {
    Employer employer = new Employer();
    employer.setEmployerName(dto.getEmployerName());
    employer.setLocation(dto.getLocation());
    employer.setEmailId(dto.getEmailId());
    employer.setPassword(dto.getPassword());
    employerDAO.save(employer);
  }
  
  public List<Employer> findAllEmployers() throws NoEmployersException {
    List<Employer> employerList = employerDAO.findAll();
    if (employerList.size() == 0) throw new NoEmployersException();
    return employerList;
  }
  
  public Employer getEmployerById(int id) throws NoSuchEmployerFoundException {
    try {      
      Employer employer = employerDAO.findById(id).get();
      return employer;
    } catch (Exception e) {
      throw new NoSuchEmployerFoundException(id);
    }
  }
  
  public Employer getEmployerByEmailId(String emailId) throws NoSuchEmployerFoundException{
	  try {
		  Employer employer = employerDAO.findByEmailId(emailId);
		  return employer;
	  }
	  catch(Exception e) {
		  throw new NoSuchEmployerFoundException(emailId);
	  }
  }
  
  public void selectCandidateForJobAfterInterview(Interview interview) throws JobAlreadyClosedWithCandidateSelectedException, AllInterviewsNotCompletedException {
    Job j = interview.getJob();
    if (j.getJobStatus().equals(JobStatus.CLOSED)) {
      throw new JobAlreadyClosedWithCandidateSelectedException();
    }
    List<Interview> pendingInterviewsForJob = interviewDAO.findByJobAndPreInterviewStatusAndPostInterviewStatus(j, PreInterviewStatus.SHORTLISTED, PostInterviewStatus.INVALID);
    if (pendingInterviewsForJob.size() != 0) {
      List<String> candidateList = pendingInterviewsForJob.stream().map((i) -> i.getCandidate().getCandidateName()).collect(Collectors.toList());
      throw new AllInterviewsNotCompletedException(j.getJobId(), candidateList);
    }

    // set the interview status for this interview as 'selected'
    interview.setPostInterviewStatus(PostInterviewStatus.SELECTED);
    interviewDAO.save(interview);
    
    // set the interview status for the remaining interviews for this job as 'rejected'
    List<Interview> waitingCandidatesAfterInterview = interviewDAO.findByJobAndPostInterviewStatus(j, PostInterviewStatus.WAITING);
    for (Interview i: waitingCandidatesAfterInterview) {
      i.setPostInterviewStatus(PostInterviewStatus.REJECTED);
      interviewDAO.save(i);
    }      
  }
  
  public String deleteAllEmployer() {
	  employerDAO.deleteAll();
	  return "Successfully deleted all the Employer";
  }

}
