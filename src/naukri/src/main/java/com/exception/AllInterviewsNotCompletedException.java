package com.exception;

import java.util.List;

public class AllInterviewsNotCompletedException extends Exception {
  public AllInterviewsNotCompletedException(int jobId, List<String> candidateIdList) {
    super("You have not completed all the interviews for job " + jobId + ". Candidates remaining: " + candidateIdList);
  }
}
