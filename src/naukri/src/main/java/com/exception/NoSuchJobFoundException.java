package com.exception;

public class NoSuchJobFoundException extends Exception {
  public NoSuchJobFoundException(int id) {
    super("No job with id " + id + " found :(");
  }
}
