package com.exception;

import com.model.Interview;

public class JobAlreadyClosedWithCandidateSelectedException extends Exception {
  public JobAlreadyClosedWithCandidateSelectedException() {
    super("Job is already closed. Do not try to select new candidate for this job");
  }
}
