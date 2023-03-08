package com.mentalab.service.io;

import com.mentalab.packets.Packet;
import com.mentalab.utils.constants.Topic;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public final class ContentServer {

  // it is a good idea to provide a size estimate as an optional initialCapacity
  private final Map<Topic, Set<Subscriber<?>>> topicSubscribers = new ConcurrentHashMap<>(5);

  private ContentServer() {}

  public static ContentServer getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void publish(Topic topic, Packet message) {
    final Set<Subscriber<?>> subscribers = this.topicSubscribers.get(topic);
    if (subscribers == null) {
      return;
    }

    for (Subscriber<?> s : subscribers) {
      s.accept(message);
    }
  }

  public void registerSubscriber(Subscriber<?> sub) {
    this.topicSubscribers
        .computeIfAbsent(sub.getTopic(), k -> ConcurrentHashMap.newKeySet())
        .add(sub);
  }

  public void deRegisterSubscriber(Subscriber<?> sub) {
    final Set<Subscriber<?>> topicSubscribers = this.topicSubscribers.get(sub.getTopic());
    if (topicSubscribers != null) {
      topicSubscribers.remove(sub);
    }
  }

  private static class InstanceHolder { // Initialization-on-demand synchronization
    private static final ContentServer INSTANCE = new ContentServer();
  }
}
