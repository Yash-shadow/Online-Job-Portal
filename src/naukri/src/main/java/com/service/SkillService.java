package com.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.SkillDAO;
import com.dto.SkillDTO;
import com.model.Candidate;
import com.model.Skill;

@Service
public class SkillService {
	@Autowired
	SkillDAO skillDao;
	
	public Skill convertSkillDtoToSkill(SkillDTO dto) {
        Skill skill = new Skill();
        skill.setSkillName(dto.getSkillName());
        return skill;
    }

	public void addSkill(Skill s) {
		s.setSkillName(s.getSkillName().toUpperCase());
        Skill withSameNameFromSkillSet = skillDao.findBySkillNameIgnoreCase(s.getSkillName());
        Skill add = new Skill();
        if(withSameNameFromSkillSet!=null) {
            skillDao.save(withSameNameFromSkillSet);
        }
        skillDao.save(s);
	}

	   public Set<Skill> addingSkillsWithNoRepetation ( Set<Skill> skillSet) {
	       
	        List<Skill> alreadyExistList = skillDao.findAll();
	        Set<Skill> NoRepteation = new HashSet<>();
	        
	        for(Skill s : skillSet ) {
	            String tempSkillName = s.getSkillName();
	            Skill withSameNameFromSkillSet = skillDao.findBySkillNameIgnoreCase(tempSkillName);
	            if(withSameNameFromSkillSet!=null) {
	                NoRepteation.add(withSameNameFromSkillSet);
	            }
	            else {
	                s.setSkillName(s.getSkillName().toUpperCase());
	                
	                NoRepteation.add(s);
	            }
	            
	        }
	        
	        return NoRepteation;
	        
	        
	    
	    }

    public void addingSkills(Set<SkillDTO> sk) {
        Set<Skill> skillSet = new HashSet<>();
        
        for (SkillDTO pdt : sk) {
            skillSet.add(convertSkillDtoToSkill(pdt));
           }
        Set<Skill> noRepSkills = addingSkillsWithNoRepetation(skillSet);

        for(Skill s : noRepSkills) {
            skillDao.save(s);
        }
        
        
    }

    public void removeSkills(Set<SkillDTO> sk) {
Set<Skill> skillSet = new HashSet<>();
        
        for (SkillDTO pdt : sk) {
            skillSet.add(convertSkillDtoToSkill(pdt));
           }
        Set<Skill> noRepSkills = addingSkillsWithNoRepetation(skillSet);

        for(Skill s : noRepSkills) {
            skillDao.delete(s);
        }
        
    }
}
