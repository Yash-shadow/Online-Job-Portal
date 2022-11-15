package com.exception;

public class NotShortlistedException extends Exception{

	public NotShortlistedException(String msg) {
		
		super(msg);
	}
	
	public NotShortlistedException(int candidateId) {
		super("Candidate id " + candidateId + " is not yet Shortlisted for this Job");
	}
}
