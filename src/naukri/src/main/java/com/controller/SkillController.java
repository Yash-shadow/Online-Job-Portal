package com.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dao.SkillDAO;
import com.dto.SkillDTO;
import com.model.Skill;
import com.service.SkillService;

@RestController
public class SkillController {

	@Autowired
	SkillService skillService;
	@Autowired
	SkillDAO dao;
	
	
	/*
	 * Candidate can add the skills
	 * Job can add the Skills
	 * 
	 */
	@PostMapping("/addSkills")
	public ResponseEntity<String> addSkill(@RequestBody Set<SkillDTO> sk) {
		
	      
	      
	    skillService.addingSkills(sk);
		
		return new ResponseEntity("Added Successfully", HttpStatus.OK);
	
	}
	
	@PostMapping("/deleteSkills")
    public ResponseEntity<String> deleteSill(@RequestBody Set<SkillDTO> sk) {
        
          
          
        skillService.removeSkills(sk);
        
        return new ResponseEntity("Added Successfully", HttpStatus.OK);
    
    }
	
}
