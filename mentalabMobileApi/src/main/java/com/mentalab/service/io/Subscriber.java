package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.utils.constants.Topic;
import java.util.function.Consumer;

public abstract class Subscriber<T> implements Consumer<Packet> {

  Topic t;

  public Subscriber(Topic t) {
    this.t = t;
  }

  public Topic getTopic() {
    return t;
  }
}
