
package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import com.controller.AuthenticationController;
import com.controller.CandidateController;
import com.controller.EmployerController;
import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.dao.JobDAO;
import com.dto.EmployerDTO;
import com.dto.JobDTO;
import com.dto.RatingFeedbackDTO;
import com.exception.feedbackException;
import com.helper.JwtUtil;
import com.model.Candidate;
import com.model.Employer;
import com.model.Job;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.service.CandidateService;

@SpringBootTest
public class EmployerControllerTests {
	@Autowired
    static CandidateService candidateService;

	@Autowired
	CandidateDAO candidateDAO;
	
	@Autowired
	EmployerDAO employerDAO;
	
	@Autowired
	JobDAO jobDAO;

	@Autowired
	CandidateController candidateController;

	@Autowired
	EmployerController employerController;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	AuthenticationController authController;

	String commonToken;
	
	String emplToken;
	
	@BeforeEach
	void setup() {
	  // delete all
	  candidateDAO.deleteAll();
	  
	  // create candidate
	  Candidate candidate = new Candidate();
	  candidate.setAge(25);
	  candidate.setCandidateName("name");
	  candidate.setEducationQualification("qual");
	  candidate.setExperience(5);
	  candidate.setLocation("Mumbai");
	  candidate.setEmailId("email");
	  candidate.setPassword("password");
	  RestTemplate template = new RestTemplate();
	  HttpHeaders headers = new HttpHeaders();
	  HttpEntity<Candidate> entity = new HttpEntity<>(candidate, headers);
	  ResponseEntity<String> response = template.postForEntity("http://localhost:9989/registerCandidate", entity, String.class);
//	  System.out.println(response.getBody());
//	  System.out.println("=============");
	  assertEquals("Added Successfully", response.getBody());
	  
	  // generate token
	  JwtRequest jwtRequest = new JwtRequest("email", "password");
	  RestTemplate template2 = new RestTemplate();
	  HttpHeaders headers2 = new HttpHeaders();
	  HttpEntity<JwtRequest> entity2 = new HttpEntity<>(jwtRequest, headers2);
	  ResponseEntity<JwtResponse> response2 = template.postForEntity("http://localhost:9989/authenticate", entity2, JwtResponse.class);
	  commonToken = "Bearer " + response2.getBody().getJwtToken();
	}
	

	// -------------------------------------------------
	@Test
	void testAddEmployer() {
	  employerDAO.deleteAll();
	  EmployerDTO employerDTO = new EmployerDTO();
	  employerDTO.setEmployerName("Employer2");
	  employerDTO.setLocation("Pune");
	  employerDTO.setEmailId("naman@gmail.com");
	  employerDTO.setPassword("admin");
	  
	  RestTemplate template = new RestTemplate();
	  HttpHeaders headers = new HttpHeaders();
	  headers.add("Authorization", commonToken);
	  HttpEntity<EmployerDTO> entity = new HttpEntity<>(employerDTO, headers);
	  ResponseEntity<String> response = template.postForEntity("http://localhost:9989/addEmployer", employerDTO, String.class);
//	  System.out.println(response.getBody());
	  
	  
	  JwtRequest jwtRequest1 = new JwtRequest("naman@gmail.com", "admin");
	    try {
	    	JwtResponse jwtResponse = authController.authenticateCand(jwtRequest1);
	    	emplToken = "Bearer " + jwtResponse.getJwtToken();
	    } catch (Exception e) {
	    	
	    }
	  
//	  String addEmployerResponse = employerController.addEmployer(employerDTO).getBody();
	  assertEquals("Employer added successfully", response.getBody());
	}
	
	@Test
	void testGetAllEmployers() {
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", commonToken);
		HttpEntity<List<Employer>> entity = new HttpEntity<>(headers);
		ResponseEntity<List> response = template.exchange("http://localhost:9989/getAllEmployers", HttpMethod.GET, entity, List.class);
		List<Employer> employerList = response.getBody();
//		System.out.println(";;;;;;;;;;;;;;;;;;;;");
//		System.out.println(employerList);
//		System.out.println(";;;;;;;;;;;;;;;;;;;;");
		assertEquals(1, employerList.size());
	}
	


	@Test
	void testShortlistCandidates() {
		RestTemplate templateShortlist = new RestTemplate();
		HttpHeaders headersShortlist = new HttpHeaders();
		headersShortlist.add("Authorization", commonToken);
		HttpEntity<String> entityShortlist = new HttpEntity<>(headersShortlist);
		try {
			List<Candidate> candidateList = candidateDAO.findAll();
			List<Job> jobList = jobDAO.findAll();
			int c1 = candidateList.get(0).getCandidateId();
			int c2 = candidateList.get(1).getCandidateId();
			int c3 = candidateList.get(2).getCandidateId();
			int jb1 = jobList.get(0).getJobId();
			int jb2 = jobList.get(0).getJobId();
			assertEquals("Successfully Updated Shortlisted candidate", employerController.updateShortlistedInterview("6", "1", "1"));
			assertEquals("Successfully Updated Shortlisted candidate", employerController.updateShortlistedInterview("7", "1", "1"));
			assertEquals("Successfully Updated Shortlisted candidate", employerController.updateShortlistedInterview("8", "1", "1"));
			assertEquals("Successfully Updated Shortlisted candidate", employerController.updateShortlistedInterview("6", "1", "2"));
			assertEquals("Successfully Updated Shortlisted candidate", employerController.updateShortlistedInterview("8", "1", "2"));
		} catch (Exception e) {
			
		}
	}
	
	@Test
	void testWaitingCandidate() {
		RestTemplate templateWaiting = new RestTemplate();
		HttpHeaders headersWaiting = new HttpHeaders();
		headersWaiting.add("Authorization", commonToken);
		HttpEntity<String> entityWaiting = new HttpEntity<>(headersWaiting);
		try {			
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.addHeader("Authorization", emplToken);
			assertEquals("Successfully Updated Waiting candidate", employerController.updateSelectedInterview(request, "6", "1"));
			assertEquals("Successfully Updated Waiting candidate", employerController.updateSelectedInterview(request, "7", "1"));
			assertEquals("Successfully Updated Waiting candidate", employerController.updateSelectedInterview(request, "8", "1"));
			assertEquals("Successfully Updated Waiting candidate", employerController.updateSelectedInterview(request, "6", "2"));
			assertEquals("Successfully Updated Waiting candidate", employerController.updateSelectedInterview(request, "8", "2"));
		} catch (Exception e) {
			
		}
	}

	@Test
	void testRejectCandidate() {
		RestTemplate templateReject = new RestTemplate();
		HttpHeaders headersReject = new HttpHeaders();
		headersReject.add("Authorization", commonToken);
		HttpEntity<String> entityReject = new HttpEntity<>(headersReject);
		try {
			assertEquals("Successfully Updated Rejected candidate", employerController.updateSelectedInterview1("8", "1", "1"));
		} catch (Exception e) {
			
		}
	}

	

}
