package com.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginProfile {
	 	@NotNull
	    private String candidateName;
	    @Min(value = 18)
	    private int age;
	    
	    @NotNull
	    private String emailId;
	    
	    @NotNull
	    private String password;
}
