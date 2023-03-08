package com.mentalab.service.impedance;

import android.util.Log;
import com.mentalab.ExploreDevice;
import com.mentalab.utils.MentalabButterworthFilter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImpedanceCalculator {

  private final double slope;
  private final double offset;
  private final int channelCount;
  ArrayList<MentalabButterworthFilter> notchFilterList = new ArrayList<>();
  ArrayList<MentalabButterworthFilter> noiseFilterList = new ArrayList<>();
  ArrayList<MentalabButterworthFilter> impedanceFilterList = new ArrayList<>();

  public ImpedanceCalculator(ExploreDevice device) {
    this.slope = device.getSlope();
    this.offset = device.getOffset();
    this.channelCount = device.getChannelCount().getAsInt();
    initializeFilters();
  }

  public ImpedanceCalculator() {
    this.slope = 223660;
    this.offset = 42.218;
    this.channelCount = 4;
    // The following code assumes there no change in ADS mask.
    // TODO adapt code to dynamic channel mask
    initializeFilters();
  }

  private static double[] toDoubleArray(List<Float> floats) {
    final double[] doubleArray = new double[floats.size()];
    for (int i = 0; i < floats.size(); i++) {
      doubleArray[i] = floats.get(i).doubleValue();
    }
    return doubleArray;
  }

  private static List<Float> toFloatList(double[] doubles) {
    ArrayList<Float> list = new ArrayList<>();
    for (double v : doubles) {
      list.add((float) v);
    }
    return list;
  }

  private double[] transpose(List<Float> data) {
    int count = 0;
    final double[] doubleArray = new double[data.size()];
    for (int i = 0; i < channelCount; i++) {
      for (int j = i; j < data.size(); j += channelCount) {
        doubleArray[count] =
            new BigDecimal(data.get(j)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        count++;
      }
    }
    return doubleArray;
  }

  public List<Float> calculate2(List<Float> data) {
    //return Arrays.asList(new Float[]{1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F});
    double[] transposedData = transpose(data);
    double[] notchedValues = applyFilterByChannels(transposedData, "notch");
    double[] noisedata = applyFilterByChannels(notchedValues, "noise");
    final double[] noiseLevel = getPeakToPeak(noisedata);
    double[] impedanceSignal = applyFilterByChannels(notchedValues, "impedance");
    final double[] impPeakToPeak = getPeakToPeak(impedanceSignal);
    final double[] impedanceValue = calculateImpedance(impPeakToPeak, noiseLevel);
    return toFloatList(impedanceValue);
  }

  public double[] getPeakToPeak(double[] values) {
    int sampleNumbers = values.length / channelCount;
    double[] peakToPeakValues = new double[channelCount];

    for (int i = 0; i < channelCount; i++) {

      final double[] sorted =
          Arrays.stream(
                  Arrays.copyOfRange(values, i * sampleNumbers, i * sampleNumbers + sampleNumbers))
              .sorted()
              .toArray();
      peakToPeakValues[i] = sorted[sorted.length - 1] - sorted[0];
    }
    return peakToPeakValues;
  }

  private double[] calculateImpedance(double[] first, double[] second) {
    final int length = first.length;
    final double[] result = new double[length];

    for (int i = 0; i < length; i++) {
      double diff = first[i] - second[i];
      result[i] = Math.round((diff * (slope / Math.pow(10, 6))) - offset);
    }
    return result;
  }

  private double[] applyFilterByChannels(double[] data, String type) {

    int sampleNumbers = data.length / channelCount;
    double[] result = new double[data.length];
    for (int i = 0; i < channelCount; i++) {
      double[] channelData =
          Arrays.stream(
                  Arrays.copyOfRange(data, i * sampleNumbers, i * sampleNumbers + sampleNumbers))
              .toArray();
      double[] channelFiltered = new double[channelData.length];
      if (type.equals("notch")) {
        channelFiltered = notchFilterList.get(i).bandPassFilter(channelData);
      } else if (type.equals("noise")) {
        channelFiltered = noiseFilterList.get(i).bandPassFilter(channelData);
      } else if (type.equals("impedance")) {
        channelFiltered = impedanceFilterList.get(i).bandPassFilter(channelData);
      }

      System.arraycopy(channelFiltered, 0, result, i * sampleNumbers, channelFiltered.length);
    }
    return result;
  }

  private void initializeFilters() {
    notchFilterList.clear();
    noiseFilterList.clear();
    impedanceFilterList.clear();
    for (int i = 0; i < channelCount; i++) {
      notchFilterList.add(new MentalabButterworthFilter(250, channelCount, false, 48, 52));
      noiseFilterList.add(new MentalabButterworthFilter(250, channelCount, true, 65, 68));
      impedanceFilterList.add(new MentalabButterworthFilter(250, channelCount, true, 61, 64));
    }
  }
}
