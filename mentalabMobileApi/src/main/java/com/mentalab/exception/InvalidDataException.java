package com.mentalab.exception;

public class InvalidDataException extends MentalabException {

  public InvalidDataException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public InvalidDataException(String errorMessage) {
    super(errorMessage);
  }
}
