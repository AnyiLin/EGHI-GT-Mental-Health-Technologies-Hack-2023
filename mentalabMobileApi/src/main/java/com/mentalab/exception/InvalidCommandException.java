package com.mentalab.exception;

public class InvalidCommandException extends MentalabException {

  public InvalidCommandException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public InvalidCommandException(String errorMessage) {
    super(errorMessage);
  }
}
