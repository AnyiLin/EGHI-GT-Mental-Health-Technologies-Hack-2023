package com.mentalab.exception;

public class InitializationFailureException extends MentalabException {

  public InitializationFailureException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public InitializationFailureException(String errorMessage) {
    super(errorMessage);
  }
}
