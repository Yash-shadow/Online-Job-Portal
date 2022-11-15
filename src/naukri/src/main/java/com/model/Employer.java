package com.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name="employer_table")
@NoArgsConstructor
@ToString
public class Employer {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private int employerId;
	
	private String employerName;
	
	
	private String location;
	
	@Column(unique = true)
	private String emailId;
	private String password;
	
	@JsonManagedReference(value="employer_job")
	@OneToMany(cascade = {CascadeType.ALL},mappedBy = "createdBy")
	private List<Job> jobList = new ArrayList<>();
	
	@JsonManagedReference(value="employer_interview")
	@OneToMany(cascade = {CascadeType.ALL},mappedBy = "employer")
	private List<Interview> interviewList = new ArrayList<>();
	
	
	
	
	
	
	

}
