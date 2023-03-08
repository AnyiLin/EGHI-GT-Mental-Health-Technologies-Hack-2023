package com.mentalab.exception;

public class NoBluetoothException extends MentalabException {

  public NoBluetoothException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public NoBluetoothException(String errorMessage) {
    super(errorMessage);
  }

  public NoBluetoothException(Throwable err) {
    super(err);
  }
}
