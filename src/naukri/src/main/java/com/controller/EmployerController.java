package com.controller;

import java.util.List;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dao.EmployerDAO;
import com.dao.InterviewDAO;
import com.dao.JobDAO;
import com.dto.CandidateDTO;
import com.dto.EmployerDTO;
import com.dto.RatingFeedbackDTO;



import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;
import com.exception.AllInterviewsNotCompletedException;
import com.exception.CandidateNotFoundException;
import com.exception.JobAlreadyClosedWithCandidateSelectedException;
import com.exception.NoEmployersException;
import com.exception.NoSuchEmployerFoundException;
import com.exception.NoSuchInterviewFoundException;
import com.exception.NoSuchJobFoundException;
import com.exception.feedbackException;
import com.helper.DecryptUserDetails;
import com.exception.NotShortlistedException;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;
import com.model.Skill;
import com.service.CandidateService;
import com.service.EmployerService;
import com.service.InterviewService;
import com.service.JobService;

import io.swagger.annotations.ApiOperation;

@RestController
public class EmployerController {

    @Autowired
    EmployerService employerService;
    
    @Autowired
    JobService jobService;
    
    @Autowired
    InterviewService interviewService;
  
	@Autowired
	EmployerDAO employerDAO;

	@Autowired
	JobDAO jobDAO;

	@Autowired
	InterviewDAO interviewDAO;
	
	@Autowired
	CandidateService candidateService;
	
	@Autowired
	DecryptUserDetails descryptUser;

	@ApiOperation(value = "add an employer", notes = "Adding a new employer", nickname = "add-employer")
	@PostMapping("/addEmployer")
	public ResponseEntity<String> addEmployer(@RequestBody EmployerDTO employerDTO) {
		
	    employerService.addEmployer(employerDTO);
		return new ResponseEntity<>("Employer added successfully", HttpStatus.ACCEPTED);
	
	}
	
	@ApiOperation(value = "get all empolyees", notes = "get all empolyees", nickname = "get all empolyees")
	@GetMapping("/getAllEmployers")
	public ResponseEntity<?> getAllEmployers() {
	  try {
	    return new ResponseEntity<>(employerService.findAllEmployers(), HttpStatus.OK);	    
	  } catch (NoEmployersException e) {
	    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	  }
	}
	
	@ApiOperation(value = "get empolyees", notes = "get empolyee  details", nickname = "get empolye detailss")
	@GetMapping("/getEmployeDetails")
	public ResponseEntity<?> getEmployer(HttpServletRequest request) throws NoSuchEmployerFoundException {
	  String emailId = descryptUser.decryptEmailId(request);
	  
	  Employer employee = employerService.getEmployerByEmailId(emailId);
	   
	return new ResponseEntity<>(employee, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "get all candidates by experience", notes = "Employer get all candidates by experience", nickname = "get all empolyees")
	@GetMapping("/getAllCandidatesByExperience/{experience_lb}/{experience_ub}")
    public ResponseEntity<List<CandidateDTO>> getAllCandidatesByExperience(@PathVariable Integer experience_lb, @PathVariable Integer experience_ub){
    	return new ResponseEntity<>(candidateService.getAllCandidatesByExperience(experience_lb, experience_ub),HttpStatus.OK);
	}
    



	@ApiOperation(value = "Close Job",notes="Employer Id fetched from token",nickname = "Close Job and select the candidate")
	@GetMapping("/closeJob")
	public ResponseEntity<?> selectCandidateAndCloseJob(HttpServletRequest request,
	  @RequestParam("candidateId") String selectedCandidateId,
	  @RequestParam("jobId") String jobId
	) throws NumberFormatException, CandidateNotFoundException {
	  try {
		  
		 String employerEmailId = descryptUser.decryptEmailId(request);
		 int employerId = employerService.getEmployerByEmailId(employerEmailId).getEmployerId();
  	    // employer will execute this method, so employerId is available
  	    // the job for which a candidate is selected should be provided
		  
  	    Employer employer = employerService.getEmployerById(employerId);
  	    
  	    // the job corresponding to the interview
  	    Job job = jobService.getJobById(Integer.parseInt(jobId));
  	    
  	    // the candidate who is selected should be provided
  	    Candidate candidate = candidateService.getCandidateById(Integer.parseInt(selectedCandidateId));
  
  	    // find the interview conducted for THIS particular candidate for THIS particular job and employer
  	    Interview interview = interviewDAO.findByCandidateAndEmployerAndJob(candidate, employer, job);

	    // select the candidate
	    employerService.selectCandidateForJobAfterInterview(interview);
	    
	    // close the job
	    jobService.closeJob(job);

	    return new ResponseEntity<>("Candidate selected successfully", HttpStatus.OK);
	  } catch (NoSuchEmployerFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
      } catch (NoSuchJobFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
      } catch (AllInterviewsNotCompletedException exception) {
	    return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
	  } catch (JobAlreadyClosedWithCandidateSelectedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
      }
	}


	
	@ApiOperation(value = "feedBack", notes = "Employer feedBack", nickname = "employer feedback")
	@PostMapping("/feedbackRating/{interviewId}")
	public ResponseEntity<String> feedbackRating(@PathVariable("interviewId") String id, @RequestBody RatingFeedbackDTO dto) throws feedbackException {
	  try {
	    Interview i = interviewService.getInterviewById(Integer.parseInt(id));
	      interviewService.provideEmployerFeedback(i, dto);
	      return new ResponseEntity<>("Feedback and rating by employer saved", HttpStatus.OK);
	  } catch (NoSuchInterviewFoundException e) {
	    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	  }
	}
	
	
	//Pawanesh
	@ApiOperation(value = "shortlistCandidate", notes = "Employer shortlists the candidate", nickname = "get all empolyees")
	@PatchMapping("/shortlistCandidate/{candidateId}/{employerId}/{jobId}")
	public ResponseEntity<String> updateShortlistedInterview(@PathVariable String candidateId, @PathVariable String employerId,@PathVariable String jobId) throws NumberFormatException, NoSuchJobFoundException, NoSuchEmployerFoundException, CandidateNotFoundException
	{
		
		Candidate candidate = candidateService.getCandidateById(Integer.parseInt(candidateId));
		Job job = jobService.getJobById(Integer.parseInt(jobId));
		Employer employer = employerService.getEmployerById(Integer.parseInt(employerId));
		
		Interview interview = interviewDAO.findByCandidateAndEmployerAndJob(candidate, employer, job);
		
		interview.setPreInterviewStatus(PreInterviewStatus.SHORTLISTED);
		
		interviewDAO.save(interview);
		
		return new ResponseEntity<>("Successfully Updated Shortlisted candidate", HttpStatus.OK);
		
		
	}
	
	@ApiOperation(value = "Shorlisted Candidates", notes = "for sending notification Shorlisted Candidates", nickname = "Shorlisted Candidates")
	@GetMapping("getAllShortListedCandidate/{jobId}")
	public ResponseEntity<List> notificationforShortlisted(@PathVariable String jobId) throws NumberFormatException, NoSuchJobFoundException 
	{
		Job job = jobService.findJobById(Integer.parseInt(jobId));
		List<Interview> interviews = interviewService.getAllShorttlistedCandidate(PreInterviewStatus.SHORTLISTED, job);
		
		List<Candidate> shortListCandidate = new ArrayList();
		for(Interview interview : interviews) {
			shortListCandidate.add(interview.getCandidate());
		}
		
		return new ResponseEntity<> (shortListCandidate, HttpStatus.OK);
		
	}
	
	
	@ApiOperation(value = "updating candidates status to waiitng", notes = "updating candidates status to waiitng", nickname = "updating candidates status to waiitng")
	@PatchMapping("/waitingCandidate/{candidateId}/{employerId}/{jobId}")
	public ResponseEntity<String> updateSelectedInterview(HttpServletRequest request, @PathVariable String candidateId, @PathVariable String jobId)
			throws NumberFormatException, NoSuchJobFoundException, NoSuchEmployerFoundException, NotShortlistedException, CandidateNotFoundException

	{
		
		String employerEmailId = descryptUser.decryptEmailId(request);
		int employerId = employerService.getEmployerByEmailId(employerEmailId).getEmployerId();
		
		Candidate candidate = candidateService.getCandidateById(Integer.parseInt(candidateId));
		
		Job job = jobService.getJobById(Integer.parseInt(jobId));
		Employer employer= employerService.getEmployerById(employerId);
		
		Interview interview = interviewDAO.findByCandidateAndEmployerAndJob(candidate, employer, job);
		
		if(interview.getPreInterviewStatus() == PreInterviewStatus.APPLIED) {
			
			throw new NotShortlistedException(candidate.getCandidateId());
		
		}
		if(interview.getPreInterviewStatus() == PreInterviewStatus.NOT_SHORTLISTED) {
			throw new NotShortlistedException("Candidate is not Shortlisted for this Job ");
		}
		
		interview.setPostInterviewStatus(PostInterviewStatus.WAITING);
		
		interviewDAO.save(interview);
		
		return new ResponseEntity<> ("Successfully Updated Waiting candidate",HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Rejecting Candidates", notes = "Employer Rejecting Candidates", nickname = "RC")
	@PatchMapping("/rejectedCandidate/{candidateId}/{employerId}/{jobId}")
	public ResponseEntity<String> updateSelectedInterview1(@PathVariable String candidateId, @PathVariable String employerId, @PathVariable String jobId) throws NumberFormatException, NoSuchJobFoundException, NoSuchEmployerFoundException, CandidateNotFoundException
	{
		Candidate candidate = candidateService.getCandidateById(Integer.parseInt(candidateId));
		Job job = jobService.getJobById(Integer.parseInt(jobId));
		Employer employer= employerService.getEmployerById(Integer.parseInt(employerId));
		
		Interview interview = interviewDAO.findByCandidateAndEmployerAndJob(candidate, employer, job);
		
		
		if(interview.getPreInterviewStatus().equals(PreInterviewStatus.APPLIED)) {
			
			interview.setPreInterviewStatus(PreInterviewStatus.NOT_SHORTLISTED);
		
		}
		interview.setPostInterviewStatus(PostInterviewStatus.REJECTED);
		
		interviewDAO.save(interview);
		
		return new ResponseEntity<> ("Successfully Updated Rejected candidate",HttpStatus.OK);
		
	}
	
//	@GetMapping("getAllNotShortListedCandidate/{jobId}")
//	public ResponseEntity<List> notificationforNotShortListed(@PathVariable String jobId) throws NumberFormatException, NoSuchJobFoundException
//	{
//		Job job =jobService.findJobById(Integer.parseInt(jobId));
//		List<Interview> interviews = interviewService.getAllNotShortListedCandidate(PreInterviewStatus.NOT_SHORTLISTED,job);
//		
//		List<Candidate> notShortListed = new ArrayList();
//		for(Interview interview : interviews)
//		{
//			notShortListed.add(interview.getCandidate());
//		}
//		
//		return new ResponseEntity<> (notShortListed, HttpStatus.OK);
//	}
	
	//Pawanesh
	
	
	// the method for conducting interview should handle exceptions such that
	// if the candidate has not been short-listed, and even then if the employer
	// tries to conduct an interview with that candidate, it should throw error
	
	
	// before conducting even one interview for a particular job, there has to be
	// a check for the list of candidates who applied for that job. if even one
	// of them has pre-interview status as 'invalid', it means that the employer
	// has not yet decided whether to short-list him/her or not. In such case,
	// commencing interviews is not allowed and doing so should throw error
	
	
	// for both an employer and a candidate, participating in an interview
	// should happen only if they have provided rating and feedback to all
	// their previous interviews. If even one interview corresponding to
	// the employer or candidate is missing their rating or feedback,
	// conducting a new interview should throw error
	
	@ApiOperation(value = "Closing job", notes = "Employer closes job without selecting anyone", nickname = "RC")
	@GetMapping("/closeJobPostingForcefully")
	public ResponseEntity<?> closeJobPosting(@RequestParam("jobId") String jobId) {
	  // employer doesn't wish to interview candidates any longer, nor does he/she wish to hire anyone
	  // so the job should be closed without selecting any candidates, and they all are rejected
	  
	  try {
	    // find the job object
	    Job job = jobService.getJobById(Integer.parseInt(jobId));
	    
	    // close the job
	    jobService.closeJob(job);
	    
	    // reject all the candidates for this job
	    interviewService.rejectAllInterviewsForJob(job);
	    
	    return new ResponseEntity<>("Job posting forcefully closed", HttpStatus.OK);
	    
	  } catch (NoSuchJobFoundException e) {
	    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	  }
	}
	
	
	@ApiOperation(value = "getCandidateApplicationStatus", notes = "Employer getCandidateApplicationStatus", nickname = "RC")
	@GetMapping("/getCandidateApplicationStatus/{id}")
    public List<Interview> getCandidateInterviews(@PathVariable  int id) throws CandidateNotFoundException {
        Candidate candidate = candidateService.getCandidateById(id);
        if (candidate==null) {
            throw new CandidateNotFoundException("id: "+id +"not found");
        }
        List<Interview> interviewStatus = candidate.getInterviewList();
        return  interviewStatus;
    }
    
	
	@ApiOperation(value = "getAllCandidatesByQualification", notes = "Employer gets All Candidates By Qualification", nickname = "RC")
    @GetMapping("/getAllCandidatesByQualification/{qualification}")
    public ResponseEntity<List<CandidateDTO>> getAllCandidatesByQualification(@PathVariable String qualification){
        return new ResponseEntity<>(candidateService.getAllCandidatesByQualification(qualification),HttpStatus.OK);
    }
    
	@ApiOperation(value = "getAllCandidatesBySkillSet", notes = "Employer getAllCandidatesBySkillSet", nickname = "RC")
    @PostMapping("/getAllCandidatesBySkillSet")
    public ResponseEntity<List<CandidateDTO>> getAllCandidatesBySkillSet(@RequestBody String skills){
        return new ResponseEntity<>(candidateService.getAllCandidatesBySkillSet(skills), HttpStatus.OK);
    }
  
    
	@ApiOperation(value = "getAllCandidatesByJobId", notes = "Employer gets All Candidates By SkillSet", nickname = "RC")
    @GetMapping("/getAllCandidatesByJobId/{jobId}")
    public ResponseEntity<Set<CandidateDTO>> getAllCandidatesByJobId(@PathVariable Integer jobId) throws NoSuchJobFoundException{
        Job job= null;
        try {
            job = jobService.findJobById(jobId);
            return new ResponseEntity<>(candidateService.candidateSetToDTO(job.getCandidateSet()), HttpStatus.OK);
        } catch (NoSuchJobFoundException e) {
            throw e;
        }
    }
	
	
	
	
		
}
