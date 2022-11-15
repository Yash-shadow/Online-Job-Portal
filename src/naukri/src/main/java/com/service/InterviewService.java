package com.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import com.dao.InterviewDAO;

import com.model.Interview;
import com.model.Job;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.ValidationException;

import com.dto.RatingFeedbackDTO;
import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.NoSuchInterviewFoundException;
import com.exception.feedbackException;



@Service
public class InterviewService {
	@Autowired
	InterviewDAO interviewDAO;

	public Interview getInterviewById(int id) throws NoSuchInterviewFoundException {
	    try {
	      Interview i = interviewDAO.findById(id).get();
	      return i;
	    } catch (Exception e) {
	      throw new NoSuchInterviewFoundException(id);
	    }
	  }
	    
	  public void provideCandidateFeedback(int j, RatingFeedbackDTO dto) throws feedbackException  {
	      try {
	      Interview i = interviewDAO.findById(j).get();
	   
	      if(i.getPostInterviewStatus().equals(PostInterviewStatus.WAITING)||i.getPostInterviewStatus().equals(PostInterviewStatus.INVALID)||(!i.getPreInterviewStatus().equals(PreInterviewStatus.SHORTLISTED))) {
//	          System.out.println("in the if");
	          throw new feedbackException("candidate need to complete his interview or data corrupted");
	      }
	      
	      i.setCandidateRating(dto.getRating());
//	      System.out.println(dto.getRating());
//	      System.out.println(i.getCandidateRating());
	      i.setCandidateFeedback(dto.getFeedback());
	      interviewDAO.save(i);
	      }
	      catch(ValidationException v) {
              throw new feedbackException("check the rating validation error"); 
          }
	     
	      catch(feedbackException f) {
	          throw new feedbackException(f.getMessage());
	      }
	      catch(TransactionSystemException t) {
	          throw new feedbackException("caught transaction exception might be an validation error");
	      }
	      catch(NoSuchElementException k) {
	          throw new feedbackException("no such interview found");
	      }
	     
	    }

  

  public void provideEmployerFeedback(Interview interview, RatingFeedbackDTO dto) throws feedbackException {
      try {
          Interview i = interviewDAO.findById(interview.getInterviewId()).get();
       
          if(i.getPostInterviewStatus().equals(PostInterviewStatus.WAITING)||i.getPostInterviewStatus().equals(PostInterviewStatus.INVALID)) {
              throw new feedbackException("candidate need to complete his interview or data corrupted");
          }
    interview.setEmployerRating(dto.getRating());
    interview.setEmployerFeedback(dto.getFeedback());
    interviewDAO.save(interview);
      }
      catch(NoSuchElementException e) {
          throw new feedbackException("check interview Id");
      }
      catch(feedbackException f) {
          throw new feedbackException(f.getMessage());
      }
  }
  
  

    public List<Interview> getAllInterviews() {
      return interviewDAO.findAll();
    }
    
    public void rejectAllInterviewsForJob(Job job) {
      List<Interview> interviewsNotRejected = interviewDAO.findByJobAndPostInterviewStatusIsNot(job, PostInterviewStatus.REJECTED);
      for (Interview i: interviewsNotRejected) {
        i.setPostInterviewStatus(PostInterviewStatus.REJECTED);
        interviewDAO.save(i);
      }
    }
    
    public List<Interview> getAllShorttlistedCandidate(PreInterviewStatus preStatus, Job job)
    {
    	return interviewDAO.findByPreInterviewStatusAndJob(PreInterviewStatus.SHORTLISTED, job);
    }
  
    public List<Interview> getAllNotShortListedCandidate(PreInterviewStatus preStaus, Job job){
    	
    	return interviewDAO.findByPreInterviewStatusAndJob(PreInterviewStatus.NOT_SHORTLISTED, job);
    }

}
