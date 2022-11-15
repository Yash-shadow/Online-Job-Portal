package com.exception;

public class NoSuchEmployerFoundException extends Exception {
  public NoSuchEmployerFoundException(int id) {
    super("No employer with id " + id + " found :(");
  }
  
  public NoSuchEmployerFoundException(String emailId) {
	    super("No employer with Email Id " + emailId + " found :(");
  }
}
