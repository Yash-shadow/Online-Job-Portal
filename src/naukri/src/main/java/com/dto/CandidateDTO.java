package com.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.model.Project;
import com.model.Skill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
	
  private String candidateName;
  
  private int age;
 
  private String emailId;

	private int experience;
	private String location;
	private String educationQualification;
	private Set<Skill> skillSet = new HashSet<>();
    private List<Project> projectList;

}
