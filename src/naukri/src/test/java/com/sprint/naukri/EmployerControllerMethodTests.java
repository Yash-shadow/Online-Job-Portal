
package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import com.controller.JobController;
import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.dao.InterviewDAO;
import com.dao.JobDAO;
import com.dto.EmployerDTO;
import com.dto.JobDTO;
import com.dto.RatingFeedbackDTO;
import com.model.Candidate;
import com.model.Employer;
import com.model.Interview;
import com.model.Job;
import com.model.JwtRequest;
import com.model.JwtResponse;

@SpringBootTest
public class EmployerControllerMethodTests {
	
	@Autowired
	CandidateDAO candidateDAO;
	
	@Autowired
	EmployerDAO employerDAO;
	
	@Autowired
	JobDAO jobDAO;
	
	@Autowired
	InterviewDAO interviewDAO;
	
	@Autowired
	AuthenticationController authController;
	
	@Autowired
	EmployerController employerController;
	
	@Autowired
	JobController jobController;
	
	@Autowired
	CandidateController candidateController;
	
	String commonToken;
	String emplToken;
	
	void setup() {
		// delete all
//		candidateDAO.deleteAll();
//		employerDAO.deleteAll();
//		jobDAO.deleteAll();
//		interviewDAO.deleteAll();
		
		// create candidate
		Candidate candidate = new Candidate();
		candidate.setAge(25);
		candidate.setCandidateName("name");
		candidate.setEducationQualification("qual");
		candidate.setExperience(5);
		candidate.setLocation("Mumbai");
		candidate.setEmailId("email");
	    candidate.setPassword("password");
	    ResponseEntity<String> response = authController.registerCandidate(candidate);
	    
	    // generate token
	    JwtRequest jwtRequest = new JwtRequest("email", "password");
	    try {
	    	JwtResponse jwtResponse = authController.authenticateCand(jwtRequest);
	    	commonToken = "Bearer " + jwtResponse.getJwtToken();
//			System.out.println(commonToken);
	    } catch (Exception e) {
	    	
	    }
	    
	    
	}
	@Test
	void test1() {
		this.setup();
//		System.out.println(commonToken);
		assertTrue(true);
	}
	
	@Test
	void testAddEmployer() {
		// generate token
	    JwtRequest jwtRequest = new JwtRequest("email", "password");
	    try {
	    	JwtResponse jwtResponse = authController.authenticateCand(jwtRequest);
	    	commonToken = "Bearer " + jwtResponse.getJwtToken();
//			System.out.println(commonToken);
	    } catch (Exception e) {
	    	
	    }
		employerDAO.deleteAll();
		EmployerDTO employerDTO = new EmployerDTO();
		employerDTO.setEmployerName("Employer1");
		employerDTO.setLocation("Pune");
		employerDTO.setEmailId("naman@gmail.com");
		employerDTO.setPassword("admin");
		
		JwtRequest jwtRequest1 = new JwtRequest("naman@gmail.com", "admin");
	    try {
	    	JwtResponse jwtResponse = authController.authenticateCand(jwtRequest1);
	    	emplToken = "Bearer " + jwtResponse.getJwtToken();
	    } catch (Exception e) {
	    	
	    }
		
		ResponseEntity<String> response = employerController.addEmployer(employerDTO);
		assertEquals("Employer added successfully", response.getBody());
	}
	
	@Test
	void testGetAllEmployers() {
		// generate token
	    JwtRequest jwtRequest = new JwtRequest("email", "password");
	    try {
	    	JwtResponse jwtResponse = authController.authenticateCand(jwtRequest);
	    	commonToken = "Bearer " + jwtResponse.getJwtToken();
//			System.out.println(commonToken);
	    } catch (Exception e) {
	    	
	    }
		employerDAO.deleteAll();
		EmployerDTO employerDTO1 = new EmployerDTO();
		employerDTO1.setEmployerName("Employer1");
		employerDTO1.setLocation("Mumbai");
		
		EmployerDTO employerDTO2 = new EmployerDTO();
		employerDTO2.setEmployerName("Employer2");
		employerDTO2.setLocation("Pune");
		
		employerController.addEmployer(employerDTO1);
		employerController.addEmployer(employerDTO2);
//		System.out.println(commonToken);
		
		List<Employer> employerList = (List<Employer>) employerController.getAllEmployers().getBody();
		assertEquals(2, employerList.size());
	}
	


}
