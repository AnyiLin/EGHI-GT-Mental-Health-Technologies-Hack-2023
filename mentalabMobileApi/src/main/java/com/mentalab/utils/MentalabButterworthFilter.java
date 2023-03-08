package com.mentalab.utils;

import uk.me.berndporr.iirj.Butterworth;

public class MentalabButterworthFilter {
  private static final int notchFreq = 50;
  /**
   * The Butterworth class implements low-pass, high-pass, band-pass and band-stop filter using the
   * Butterworth polynomials. Has the flattest pass-band but a poor roll-off rate. Reference:
   * https://en.wikipedia.org/wiki/Butterworth_filter
   */
  private final double samplingFreq;

  private final double nyquistFreq;
  private final int filterOrder = 5;
  private int channelNumber;
  Butterworth filterManager = new Butterworth();

  /**
   * This constructor initialises the prerequisites required to use Butterworth filter.
   *
   * @param Fs Sampling frequency of input signal
   */

  public MentalabButterworthFilter(double Fs, int channelNumber, boolean isBandpass, double lc, double hc) {
    this.samplingFreq = Fs;
    this.nyquistFreq = samplingFreq / 2;
    this.channelNumber = channelNumber;
    if (isBandpass) {
      filterManager.bandPass(5, 250, (hc + lc)/2, hc - lc);
    } else {
      filterManager.bandStop(5, 250, (hc + lc)/2, hc - lc);
    }
  }

  /**
   * This method implements a low pass filter with given parameters, filters the signal and returns
   * it.
   *
   * @param signal Signal to be filtered
   * @param order Order of the filter
   * @param cutoffFreq The cutoff frequency for the filter in Hz
   * @return double[] Filtered signal
   */
  public double[] lowPassFilter(double[] signal, int order, double cutoffFreq) {
    double[] output = new double[signal.length];
    Butterworth lp = new Butterworth();
    lp.lowPass(order, this.samplingFreq, cutoffFreq);
    for (int i = 0; i < output.length; i++) {
      output[i] = lp.filter(signal[i]);
    }
    return output;
  }

  /**
   * This method implements a high pass filter with given parameters, filters the signal and returns
   * it.
   *
   * @param signal Signal to be filtered
   * @param order Order of the filter
   * @param cutoffFreq The cutoff frequency for the filter in Hz
   * @return double[] Filtered signal
   */
/*  public double[] highPassFilter(double[] signal, int order, double cutoffFreq) {
    double[] output = new double[signal.length];
    hp.highPass(this.filterOrder, this.samplingFreq, cutoffFreq);
    for (int i = 0; i < output.length; i++) {
      output[i] = hp.filter(signal[i]);
    }
    return output;
  }*/

  /**
   * This method implements a band pass filter with given parameters, filters the signal and returns
   * it.
   *
   * @param signal Signal to be filtered
   * @throws IllegalArgumentException The lower cutoff frequency is greater than the
   *     higher cutoff frequency
   * @return double[] Filtered signal
   */
  public double[] bandPassFilter(double[] signal)
      throws IllegalArgumentException {

    double[] output = new double[signal.length];
    for (int i = 0; i < output.length; i++) {
      output[i] = filterManager.filter(signal[i]);
    }
    return output;
  }

  /**
   * This method implements a band stop filter with given parameters, filters the signal and returns
   * it.
   *
   * @param signal Signal to be filtered
   * @throws IllegalArgumentException The lower cutoff frequency is greater than the
   *     higher cutoff frequency
   * @return double[] Filtered signal
   */
  public double[] bandStopFilter(double[] signal) throws IllegalArgumentException {

    double[] output = new double[signal.length];
    for (int i = 0; i < output.length; i++) {
      output[i] = filterManager.filter(signal[i]);
    }
    return output;
  }
}
