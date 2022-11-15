package com.controller;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dao.CandidateDAO;
import com.dao.EmployerDAO;
import com.dto.EmployerDTO;
import com.dto.LoginProfile;
import com.helper.JwtUtil;
import com.model.Candidate;
import com.model.Employer;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.service.CandidateJwtService;
import com.service.CandidateService;

import io.swagger.annotations.ApiOperation;


@RestController
public class AuthenticationController {

	@Autowired
	CandidateDAO candDao;
	@Autowired
	CandidateService service;
	@Autowired
	EmployerDAO employerDao;
	
	@Autowired
	private JwtUtil jwtUtility; 
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CandidateJwtService candidateService;
	
	@PostMapping("/")
	public String login() {
		return "Successfully Landed to the Page";
	}
	
	@RequestMapping(value = "/authenticate", headers = "Accept=application/json", method = RequestMethod.POST)
	public JwtResponse authenticateCand(@RequestBody JwtRequest jwtRequest) throws UsernameNotFoundException, Exception {
		
//		System.out.println(jwtRequest);
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(), 
							jwtRequest.getPassword()
							)
					);
		}
		catch (BadCredentialsException e) {
			// TODO: handle exception
//			System.out.println(e.getMessage());
			throw new BadCredentialsException("INVALID_CREDENTIALS");
		}
//		catch(Exception e) {
//			throw new Exception(e.getMessage());
//		}
		
		final UserDetails userDetails = candidateService.loadUserByUsername(jwtRequest.getUsername());
		
		final String token = jwtUtility.generateToken(userDetails);
		
		return new JwtResponse(token);
		
	}
	//user Story 1 -able to register and login into to the site 
	@ApiOperation(value = "Register",notes="Register using full User Details",nickname = "Register Profile" )
	@PostMapping("/registerCandidate")
	public ResponseEntity<String> registerCandidate(@RequestBody Candidate candidate) {
		
	    candDao.save(candidate);
		return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
		
	}
//	@ApiOperation(value = "Register Candidate",notes="Register using necessary User Details",nickname = "Register Profile" )
//	@PostMapping("/registerUserProfile")
//	public ResponseEntity<String> registerCandidate(@RequestBody LoginProfile profile) {
//		Candidate c = new Candidate();
//		c.setAge(profile.getAge());
//		c.setCandidateName(profile.getCandidateName());
//		c.setEmailId(profile.getEmailId());
//		c.setPassword(profile.getPassword());
//	    candDao.save(c);
//		return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
//		
//	}
//	
//	@ApiOperation(value = "Register Employee",notes="Register using necessary User Details",nickname = "Register Profile" )
//	@PostMapping("/registerEmployer")
//	public ResponseEntity<String> registerEmployer(@RequestBody  EmployerDTO employer) {
//		Employer e = new Employer();
//		
//		e.setEmployerName(employer.getEmployerName());
//		e.setEmailId(employer.getPassword());
//		e.setPassword(employer.getPassword());
//	    e.setLocation(employer.getLocation());
//		employerDao.save(e);
//		return new ResponseEntity<>("Added Successfully", HttpStatus.OK);
//		
//	}
	
	
	
}
