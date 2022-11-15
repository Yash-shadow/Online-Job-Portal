package com.model;


import java.util.*;

import javax.persistence.*;

import com.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="job_table")
@Getter
@Setter
@NoArgsConstructor

public class Job {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int jobId;
	
	@Enumerated(EnumType.STRING)
	private JobStatus jobStatus = JobStatus.OPENED;
	
	
	//@NotNull
    private String jobDescription;
    private String industry;
    
    private String location;
    
    @NotNull
    private float salaryPackage;
	
	
	
	@ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		          CascadeType.PERSIST,
		          CascadeType.MERGE
		      },
		      mappedBy = "jobSet")
	@JsonIgnore
	private Set<Candidate>  candidateSet = new HashSet<>();
	
	@JsonManagedReference(value="job_interview")
	    @OneToMany(cascade = CascadeType.ALL,mappedBy = "job")
	private List<Interview> interviewList = new ArrayList<>();
	
	@JsonBackReference(value="employer_job")
	@ManyToOne
    private Employer createdBy;

	public Job(String jobDescription, String industry, String location, @NotNull float salaryPackage) {
		super();
		this.jobDescription = jobDescription;
		this.industry = industry;
		this.location = location;
		this.salaryPackage = salaryPackage;
	}

	@Override
	public String toString() {
		return "Job [jobStatus=" + jobStatus + ", jobDescription=" + jobDescription + ", industry=" + industry
				+ ", location=" + location + ", salaryPackage=" + salaryPackage + "]";
	}
	
	
}
