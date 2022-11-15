package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

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

import com.controller.JobController;
import com.dto.EmployerDTO;
import com.dto.JobDTO;
import com.enums.JobStatus;
import com.exception.NoSuchEmployerFoundException;
import com.helper.JwtUtil;
import com.model.Candidate;
import com.model.Employer;
import com.model.Job;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.service.CandidateService;
import com.service.EmployerService;
import com.service.JobService;

@SpringBootTest
public class JobControllerTest {
  
	@Autowired
	JobService jobService;
	
	@Autowired
	CandidateService candidateService;
	
	@Autowired
	JwtUtil jwtUtil;

	String commonToken;
	
	@Autowired
	EmployerService employerService;

	EmployerDTO emplDTO = new EmployerDTO();
	Employer emploCurr;
	
	
	/*
	 * Register a Employer and get the token
	 */
	@BeforeEach
	void generateAuthToken() throws NoSuchEmployerFoundException {
		
		employerService.deleteAllEmployer();
		
		emplDTO.setEmployerName("Pankaj");
		emplDTO.setLocation("Mumbai");
		emplDTO.setEmailId("pankaj@gmail.com");
		emplDTO.setPassword("121aaa");
		
		//Register Candidate
		String url = "http://localhost:9989/addEmployer";
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<EmployerDTO> request = new HttpEntity<>(emplDTO, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		//Get The Token to access all the APIS
		JwtRequest jwtRequest = new JwtRequest(emplDTO.getEmailId(), emplDTO.getPassword());
		
		String url1 = "http://localhost:9989/authenticate";
		
		RestTemplate restTemplate1 = new RestTemplate();
		HttpHeaders headers1 = new HttpHeaders();
		HttpEntity<JwtRequest> request1 = new HttpEntity<>(jwtRequest, headers1);
		
		ResponseEntity<JwtResponse> response1 = restTemplate1.postForEntity(url1, request1, JwtResponse.class);
		
		JwtResponse resp = response1.getBody();
		emploCurr = employerService.getEmployerByEmailId(emplDTO.getEmailId());
		
		commonToken = "Bearer " + resp.getJwtToken();
		
		//Add a Job
		jobService.deleteAllJob();
		
		RestTemplate template = new RestTemplate();

		headers.add("Authorization", commonToken);
		  
		JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Need a Skill Java, Web Development and Angular", "Capgemini", "Pune", 120030.45f);
		  
		HttpEntity<JobDTO> entity = new HttpEntity<>(jobDTO, headers);
		  
		//Add a Job by Employer
		url = "http://localhost:9989/employerAddjob";
		  
		String res = template.exchange(url,  HttpMethod.POST, entity, String.class).getBody();
	}
	
	@Test
	public void testAddJobManually() {
		
		
		RestTemplate template = new RestTemplate();
		  
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", commonToken);
		  
		JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skill and Public Speaking Experience", "Capgemini", "Pune", 1246030.45f);
		  
		HttpEntity<JobDTO> entity1 = new HttpEntity<>(jobDTO, headers);
		  
		//Add a Job by Employer
		String url = "http://localhost:9989/employerAddjob";
		  
		String response = template.exchange(url,  HttpMethod.POST, entity1,String.class).getBody();
		  
		assertEquals("Job Added Successfully", response);
	}
	
  
	  @Test
	  public void testGetAllJobs() {
	    
		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
		  
		  JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skill Required", "Capgemini", "Pune", 120030.45f);
		  
		  HttpEntity<JobDTO> entity1 = new HttpEntity<>(jobDTO, headers);
		  
		  //Add a Job by Employer
		  String url = "http://localhost:9989/employerAddjob";
		  
		  String response = template.exchange(url,  HttpMethod.POST, entity1,String.class).getBody();
		  
		  //Get All Jobs
		  url = "http://localhost:9989/getAllJobs";
		  
		  HttpEntity entity = new HttpEntity<>(headers);
		  
		  List<Job> expected = jobService.getAllJob();
		  
		  List<Job> actual = template.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Job>>() {}).getBody();
		  assertEquals(expected.toString(), actual.toString());
		  
	  }
	  
	  /*
	   * Will Update the Job that was created in addJob method BeforeEach
	   */
	  @Test 
	  public void testUpdateJobDescription() {
		  
		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
		  
		  //Update Job
		  
		  List<Job> allJobs = jobService.getAllJob();
		  
		  String expcDesc = "Skills Required are Java, Angular and ML";
		  
		  String url = "http://localhost:9989/updateJobDescription/"+allJobs.get(0).getJobId();
		  
		  HttpEntity<String> entity1 = new HttpEntity<>(expcDesc, headers);
		  
		  template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		  String response = template.exchange(url,  HttpMethod.PATCH, entity1, String.class).getBody();
		  
		  allJobs = jobService.getAllJob();
		  
		  String actualDesc = allJobs.get(0).getJobDescription();
		  assertEquals(expcDesc, actualDesc);
 
	  }
	  
	  @Test 
	  public void testGetJobsByIndustry() {
		  
		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
			  
		  //Add a Job
		  
		  JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skill and Public Speaking Experience", "Wipro", "Pune", 1246030.45f);
		  
		  HttpEntity<JobDTO> entity = new HttpEntity<>(jobDTO, headers);
		  
		  //Add a Job by Employer
		  String url = "http://localhost:9989/employerAddjob";
			  
		  String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
		  
		  assertEquals("Job Added Successfully", response);
		  
		  
		  //Get Job By Location
		  
		  String expected = jobService.getAllJob().get(1).toString();
		  
		  url = "http://localhost:9989/getJobsByIndustry/"+ jobDTO.getIndustry();
		  
		  HttpEntity entity1 = new HttpEntity<>(headers);
		  
		  template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		  List<Job> jobs = template.exchange(url,  HttpMethod.GET, entity1, new ParameterizedTypeReference<List<Job>>() {}).getBody();
		  
		  String actual = jobs.get(0).toString();
		  assertEquals(expected, actual);
 
	  }
	  
	  @Test 
	  public void testGetJobsByLocation() {
		  
		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
			  
		  //Add a Job
		  
		  JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skill and Public Speaking Experience", "Capgemini", "Mumbai", 1246030.45f);
		  
		  HttpEntity<JobDTO> entity = new HttpEntity<>(jobDTO, headers);
		  
		  //Add a Job by Employer
		  String url = "http://localhost:9989/employerAddjob";
			  
		  String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
		  
		  assertEquals("Job Added Successfully", response);
		  
		  
		  //Get Job By Location
		  
		  String expected = jobService.getAllJob().get(1).toString();
		  
		  url = "http://localhost:9989/getJobsByLocation/"+ jobDTO.getLocation();
		  
		  HttpEntity entity1 = new HttpEntity<>(headers);
		  
		  template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		  List<Job> jobs = template.exchange(url,  HttpMethod.GET, entity1, new ParameterizedTypeReference<List<Job>>() {}).getBody();
		   
		  String actual = jobs.get(0).toString();
		  assertEquals(expected, actual);
 
	  }
	  
	  @Test
	  public void testGetAllJobBySkill() {

		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
			  
		  //Add a Job
		  
		  JobDTO jobDTO = new JobDTO(emploCurr.getEmployerId(), "Presentation Skill and Public Speaking Experience", "Capgemini", "Mumbai", 1246030.45f);
		  
		  HttpEntity<JobDTO> entity = new HttpEntity<>(jobDTO, headers);
		  
		  //Add a Job by Employer
		  String url = "http://localhost:9989/employerAddjob";
			  
		  String response = template.exchange(url,  HttpMethod.POST, entity,String.class).getBody();
		  
		  assertEquals("Job Added Successfully", response);
		  
		  
		  //Get Job By Location
		  
		  String expected = jobService.getAllJob().get(0).toString();
		  
		  url = "http://localhost:9989/getJobBySkill/";
		  
		  HttpEntity<String> entity1 = new HttpEntity<>("Angular,Java",headers);
		  
		  template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		  List<Job> jobs = template.exchange(url,  HttpMethod.POST, entity1, new ParameterizedTypeReference<List<Job>>() {}).getBody();
		   
		  String actual = jobs.get(0).toString();
		  assertEquals(expected, actual);  
	  }
	  
	  @Test
	  public void testDeleteJob() {
		  
		  RestTemplate template = new RestTemplate();
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.add("Authorization", commonToken);
		  
		  int jobId = jobService.getAllJob().get(0).getJobId();
		  
		  String url = "http://localhost:9989/deleteJob/" + jobId;
		  
		  HttpEntity entity = new HttpEntity<>(headers);
		  
		  template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		  
		  String expected = "Job deleted successfully";
		  String actual = template.exchange(url,  HttpMethod.DELETE, entity, String.class).getBody();

		  assertEquals(expected, actual);  
		  
	  }
	  
}