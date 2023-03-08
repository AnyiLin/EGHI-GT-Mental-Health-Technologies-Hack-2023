package com.mentalab.exception;

// changed access modifier public to private
public class MentalabException extends Exception {

  public MentalabException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public MentalabException(Throwable err) {
    super(err);
  }

  public MentalabException(String errorMessage) {
    super(errorMessage);
  }
}
