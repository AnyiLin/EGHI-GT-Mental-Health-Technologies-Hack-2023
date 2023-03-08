package com.mentalab.utils;

import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedExceptionSupplier<T> extends Supplier<T> {

  @Override
  default T get() {
    try {
      return accept();
    } catch (Exception e) {
      throw new CompletionException(e);
    }
  }

  T accept() throws Exception;
}
