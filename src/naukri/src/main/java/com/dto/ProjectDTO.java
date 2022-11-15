package com.dto;

import com.model.Candidate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {

	private String projectName;
	private String projectDescription;
    public ProjectDTO(String projectName, String projectDescription) {
    
        
        
        super();
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }
	
  
}
