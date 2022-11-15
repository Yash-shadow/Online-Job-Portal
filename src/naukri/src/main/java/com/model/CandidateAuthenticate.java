package com.model;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

public class CandidateAuthenticate {
    private String userName;
    private String password; 


   // @OneToOne(cascade = CascadeType.ALL)
    private Candidate candidate;
}
