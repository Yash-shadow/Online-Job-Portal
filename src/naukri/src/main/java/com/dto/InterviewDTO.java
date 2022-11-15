package com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterviewDTO {
  private int candidateId;
  private int employerId;
  private int jobId;
}
