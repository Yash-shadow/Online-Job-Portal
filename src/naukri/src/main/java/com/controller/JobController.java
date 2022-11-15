package com.controller;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.model.Candidate;
import com.model.Job;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


import com.dao.EmployerDAO;
import com.dao.JobDAO;
import com.dto.JobDTO;
import com.exception.NoEmployersException;
import com.model.Employer;
import com.model.Job;
import com.service.EmployerService;
import com.service.JobService;

import io.swagger.annotations.ApiOperation;

@RestController
public class JobController {
    @Autowired
    JobDAO jobDAO;
    
    @Autowired
    EmployerDAO employerDAO;
    
    @Autowired
    EmployerService employerService;
    
    @Autowired
    JobService jobService;
    
 
    
//	@PostMapping("/addjob")
//	public ResponseEntity addjob(@RequestBody Job job) {
//		jobDao.save(job);
//		return new ResponseEntity("job added successfuly", HttpStatus.ACCEPTED);
//	}
	
//	@ApiOperation(value = "add a job", notes = "Adding a new job", nickname = "add-job")
//    @PostMapping("/addJob")
//    public ResponseEntity<String> addJob(@RequestBody NewJobDTO jobDTO) {
//        
//        try {
//          List<Employer> employerList = employerService.findAllEmployers();
//          Job job = new Job(jobDTO.getJobDescription(), jobDTO.getIndustry());
////        job.setJobPostStatus(JobPostStatus.OPEN);
//          Employer first = employerList.get(0);
//          job.setCreatedBy(first);
//          jobDAO.save(job);
//            
//          first.getJobList().addAll(Arrays.asList(job));
//          employerDAO.save(first);
//          return new ResponseEntity<>("Job added successfully", HttpStatus.ACCEPTED);
//        } catch (NoEmployersException e) {
//          return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
	
	
	
	@ApiOperation(value = "Add a job", notes = "Adding a new job", nickname = "add-job")
    @PostMapping("/employerAddjob")
    public ResponseEntity<String> addJobManually(@RequestBody JobDTO jobDTO) {
        
		String response = jobService.addJobByEmployer(jobDTO);
		
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
	
	
	
	@PatchMapping("/updateJobDescription/{id}")
	public ResponseEntity updateJob(@PathVariable("id") String jobId, @RequestBody String jobDescription) {
		
		Job j1 = jobDAO.findById(Integer.parseInt(jobId)).get();
		
		j1.setJobDescription(jobDescription);

		jobDAO.save(j1);
		
		return new ResponseEntity("Job updated Successfuly", HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deleteJob/{id}")
	public ResponseEntity deleteJobById(@PathVariable("id") String jobId) {
		
		jobDAO.deleteById(Integer.parseInt(jobId));
		return new ResponseEntity("Job deleted successfully", HttpStatus.FOUND);
	
	}

    
   

}
