package com.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.enums.PostInterviewStatus;
import com.enums.PreInterviewStatus;


import com.fasterxml.jackson.annotation.JsonBackReference;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interview_table")
@ToString
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int interviewId;
	
	@Range(min = 0,max = 10)
	private int candidateRating;
	@Range(min = 0,max = 10)
	private int employerRating;
	
	private String candidateFeedback;
	
	private String employerFeedback;
	@Column
	@Enumerated(EnumType.STRING)
	private PreInterviewStatus preInterviewStatus =PreInterviewStatus.APPLIED;
	
	@Column
	@Enumerated(EnumType.STRING)
    private PostInterviewStatus postInterviewStatus = PostInterviewStatus.INVALID;
    
	
	@JsonBackReference(value="candidate_interview")
	@ManyToOne
	private Candidate candidate;
	
	@JsonBackReference(value="employer_interview")
	@ManyToOne(cascade = CascadeType.ALL)
	private Employer employer;

	@JsonBackReference(value="job_interview")
	@ManyToOne(cascade = CascadeType.ALL)
	private Job job;

	
	
	



 


}
