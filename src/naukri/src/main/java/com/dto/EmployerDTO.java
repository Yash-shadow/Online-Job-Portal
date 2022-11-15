package com.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployerDTO {
	
  private String employerName;
  
  private String location;
  
  @NotNull
  private String emailId;
  
  @NotNull
  private String password;

}
