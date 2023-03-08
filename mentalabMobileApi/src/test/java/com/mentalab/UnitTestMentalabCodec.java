package com.mentalab;

import com.mentalab.exception.InvalidDataException;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTestMentalabCodec {

  @Test(expected = InvalidDataException.class)
  public void nullCheckDecodeIsCorrect() throws InvalidDataException {
    // MentalabCodec.startDecode(null);
  }
}
