package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;




import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
//@NamedQuery()
@Table(name ="skill_table")
public class Skill {
//	static{
//		CandidateSkill java= new CandidateSkill();
//		java.setSkillId(1);
//		java.setSkillName("Java");
//		
//		CandidateSkill python = new CandidateSkill();
//		python.setSkillId(2);
//		python.setSkillName("Python");
//		
//	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int skillId;

	@ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		          CascadeType.PERSIST,
		          CascadeType.MERGE
		      },
		      mappedBy = "skillSet")
	
	@JsonIgnore
	private Set<Candidate>  candidateSet = new HashSet<>();
	
	@Column(unique=true)
	private String skillName;

	public Skill(String skillName) {
		super();
		this.skillName = skillName;
	}
	
	
	
}
