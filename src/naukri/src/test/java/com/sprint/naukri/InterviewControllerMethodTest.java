package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

//import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.controller.InterviewController;
import com.controller.JobController;
import com.dao.InterviewDAO;
import com.dto.EmployerDTO;
import com.dto.InterviewDTO;
import com.dto.JobDTO;
import com.enums.JobStatus;
import com.exception.CandidateNotFoundException;
import com.exception.NoSuchEmployerFoundException;
import com.helper.JwtUtil;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.service.CandidateService;
import com.service.EmployerService;
import com.service.InterviewService;
import com.service.JobService;

@SpringBootTest
public class InterviewControllerMethodTest {
	

	@Autowired
	JobService jobService;
	
	@Autowired
	CandidateService candidateService;
	
	@Autowired
	InterviewController interviewController;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	EmployerService employerService;
	
	@Autowired
	InterviewService interviewService;
	
	@Autowired
	InterviewDAO interviewDao;
	
	String commonToken;

	EmployerDTO emplDTO = new EmployerDTO();
	Employer emploCurr;

	Candidate cand1 = new Candidate(); 

	/*
	 * Register candidate and get token
	 */
	
	@BeforeEach
	void startConnection() throws NoSuchEmployerFoundException, CandidateNotFoundException {
		
		candidateService.deleteAllCandidate();
		interviewDao.deleteAll();
		
		cand1.setAge(30);
		cand1.setCandidateName("om");
		cand1.setEducationQualification("be");
		cand1.setExperience(12);
		cand1.setLocation("ngp");
		cand1.setEmailId("om");
		cand1.setPassword("pass");
		
		// Register Candidate
		String url = "http://localhost:9989/registerCandidate";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Candidate> request = new HttpEntity<>(cand1, headers);
	
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		Candidate candy = candidateService.getAllCandidates().get(0);
		// Authenticate and get token
		JwtRequest jwtRequest = new JwtRequest("om", "pass");
		String url1 = "http://localhost:9989/authenticate";
		RestTemplate restTemplate1 = new RestTemplate();
		HttpHeaders headers1 = new HttpHeaders();
		HttpEntity<JwtRequest> request1 = new HttpEntity<>(jwtRequest, headers1);
		ResponseEntity<JwtResponse> response1 = restTemplate1.postForEntity(url1, request1, JwtResponse.class);
		JwtResponse resp = response1.getBody();
		commonToken = "Bearer " + resp.getJwtToken();
		
		// Add employer
		employerService.deleteAllEmployer();

		emplDTO.setEmployerName("Pankaj");
		emplDTO.setLocation("Mumbai");
		emplDTO.setEmailId("pankaj@gmail.com");
		emplDTO.setPassword("121aaa");
		
		url = "http://localhost:9989/addEmployer";
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		HttpEntity<EmployerDTO> request2 = new HttpEntity<>(emplDTO, headers);
		ResponseEntity<String> response2 = restTemplate.postForEntity(url, request2, String.class);
		
		// Add job
		jobService.deleteAllJob();
		RestTemplate template = new RestTemplate();
		headers.add("Authorization", commonToken);
		
		emploCurr = employerService.getEmployerByEmailId(emplDTO.getEmailId());
		JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Need a Skill Java, Web Development and Angular", "Capgemini", "Pune", 120030.45f);
	
		HttpEntity<JobDTO> entity = new HttpEntity<>(jobDTO, headers);
		url = "http://localhost:9989/employerAddjob";		  
		String res = template.exchange(url,  HttpMethod.POST, entity, String.class).getBody();
		
//		System.out.println(res);
		
		Job jobAct = jobService.getAllJob().get(0);
		
		
		// Candidate apply for interview
		RestTemplate template3 = new RestTemplate();
		HttpHeaders headers3= new HttpHeaders();
		headers.add("Authorization", commonToken);
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());	
		HttpEntity<Object> entity3 = new HttpEntity(headers);		
	
		ResponseEntity<String> responseC1J1 = template3.exchange("http://localhost:9989/candidateApplicationForJob?candidateId="+candy.getCandidateId()+"&jobId="+jobAct.getJobId(), HttpMethod.POST, entity3, String.class);

	}
	
	  
	  @Test 
	  @Transactional
	  public void testGetInterviewById() throws NoSuchEmployerFoundException {
			

		int iid = interviewDao.findAll().get(0).getInterviewId();

		Interview expected = interviewService.getAllInterviews().get(0);
		  
		expected.setCandidate(null);
		expected.setEmployer(null);
		expected.setJob(null);
  
		Interview interview = (Interview) interviewController.getInterview(""+iid).getBody();
		assertEquals(expected, interview);
			
		assertEquals("No interview with id 990 found:( ", interviewController.getInterview("990").getBody());
	  }
	  
	  @Test
	  public void testGetAllInterviews() throws NoSuchEmployerFoundException {

		  List<Interview> expected = interviewService.getAllInterviews();
		  
		  expected.get(0).setCandidate(null);
		  expected.get(0).setEmployer(null);
		  expected.get(0).setJob(null);
		  
			List<Interview> interviewList = (List<Interview>) interviewController.getAllInterviews().getBody();
			assertEquals(1, interviewList.size());
		  
	  }	
	  
}
