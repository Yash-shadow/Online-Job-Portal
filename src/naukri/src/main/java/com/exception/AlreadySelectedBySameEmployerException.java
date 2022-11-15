package com.exception;

public class AlreadySelectedBySameEmployerException extends Exception {
  public AlreadySelectedBySameEmployerException(String employerName) {
    super("You have already been hired by " + employerName);
  }
}
