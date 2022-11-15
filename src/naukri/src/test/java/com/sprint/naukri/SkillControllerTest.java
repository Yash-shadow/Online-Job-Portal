package com.sprint.naukri;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.helper.JwtUtil;
import com.model.Candidate;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.model.Skill;
import com.service.CandidateService;

@SpringBootTest
class SkillControllerTest {

	@Autowired
	CandidateService candidateService;

	String commonToken;
	
	Candidate cand1 = new Candidate(); 
	
	@BeforeEach
	void startConnection() {
		
		candidateService.deleteAllCandidate();
		
		cand1.setAge(22);
		cand1.setCandidateName("yash");
		cand1.setEducationQualification("B.tech");
		cand1.setExperience(2);
		cand1.setLocation("sfs");
		cand1.setEmailId("suraj@gmail.com");
		cand1.setPassword("121aaa");
		
		//Register Candidate
		String url = "http://localhost:9989/registerCandidate";
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<Candidate> request = new HttpEntity<>(cand1, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		//Get The Token to access all the APIS
		JwtRequest jwtRequest = new JwtRequest("suraj@gmail.com", "121aaa");
		
		String url1 = "http://localhost:9989/authenticate";
		RestTemplate restTemplate1 = new RestTemplate();
		HttpHeaders headers1 = new HttpHeaders();
		HttpEntity<JwtRequest> request1 = new HttpEntity<>(jwtRequest, headers1);
		
		ResponseEntity<JwtResponse> response1 = restTemplate1.postForEntity(url1, request1, JwtResponse.class);
		
		JwtResponse resp = response1.getBody();
		
		commonToken = "Bearer " + resp.getJwtToken();
		
	}
	
	@Test
	void testAddSkill() {
		
		Skill sk = new Skill();
		sk.setSkillName("Java");
		
		
		String url = "http://localhost:9989/addSkills";
		
		Set<Skill> skills = new HashSet<>();
		
		skills.add(sk);
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", commonToken);
		
		HttpEntity<Set<Skill>> request = new HttpEntity<>(skills, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		assertEquals("Added Successfully", response.getBody());
	}

}
