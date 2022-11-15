package com.exception;

public class NoEmployersException extends Exception {
  public NoEmployersException() {
    super("There are no employers... :(");
  }
}
