package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.packets.sensors.exg.EEGPacket;
import com.mentalab.service.impedance.ImpedanceCalculator;
import com.mentalab.utils.constants.Topic;
import java.util.List;

public class ImpedanceSubscriber extends Subscriber<EEGPacket> {

  private final ImpedanceCalculator impedanceCalculator;

  public ImpedanceSubscriber(Topic t, ImpedanceCalculator i) {
    super(t);
    this.impedanceCalculator = i;
  }

  @Override
  public void accept(Packet packet) {
    final List<Float> impedanceData = impedanceCalculator.calculate2(packet.getData());
    packet.overwriteData(impedanceData);
    ContentServer.getInstance().publish(Topic.IMPEDANCE, packet);
  }
}
