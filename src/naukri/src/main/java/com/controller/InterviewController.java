package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.dao.EmployerDAO;
import com.dao.InterviewDAO;
import com.dao.JobDAO;
import com.dto.InterviewDTO;
import com.exception.NoSuchInterviewFoundException;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;
import com.service.InterviewService;

@RestController
public class InterviewController {
  
  @Autowired
  InterviewDAO interviewDAO;
  
  @Autowired
  EmployerDAO employerDAO;
  
  @Autowired
  JobDAO jobDAO;
  
  @Autowired
  InterviewService interviewService;
  
//  @PostMapping("/addInterview")
//  public ResponseEntity<?> addInterview(@RequestBody InterviewDTO interviewDTO) {
//    
//    try {
//      Interview interview = new Interview();
//  //    Candidate c = new Candidate();
//      Employer e = new Employer();
//      Job j = new Job();
//      
//      interviewDAO.save(interview);
//      return new ResponseEntity<>("Interview added successfully", HttpStatus.ACCEPTED);  
//    } catch (Exception e) {
//      return new ResponseEntity<>("not found...", HttpStatus.NOT_FOUND);
//    }
//    
//    
//  }
  
  @GetMapping("/getAllInterviews")
  public ResponseEntity<List<Interview>> getAllInterviews() {
	  
    return new ResponseEntity<>(interviewDAO.findAll(), HttpStatus.OK);
  
  }
  
  @GetMapping("/getInterviewById/{id}")
  public ResponseEntity<?> getInterview(@PathVariable String id) {
	  
    try {
    	
      Interview i = interviewService.getInterviewById(Integer.parseInt(id));
      return new ResponseEntity<>(i, HttpStatus.OK);
      
    } catch (NoSuchInterviewFoundException e) {
    	
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    
    }
    
  }
  
  
}
