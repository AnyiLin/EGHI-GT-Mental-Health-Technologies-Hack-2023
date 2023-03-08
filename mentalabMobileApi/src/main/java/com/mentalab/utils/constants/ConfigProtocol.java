package com.mentalab.utils.constants;

// Fantastic solution to hierarchical enums:
// https://stackoverflow.com/questions/19680418/how-to-use-enum-with-grouping-and-subgrouping-hierarchy-nesting
public enum ConfigProtocol {
  ENVIRONMENT(Type.Module, 0),
  ORN(Type.Module, 1),
  EXG(Type.Module, 2),

  CHANNEL_0(Type.Channel, 0),
  CHANNEL_1(Type.Channel, 1),
  CHANNEL_2(Type.Channel, 2),
  CHANNEL_3(Type.Channel, 3),
  CHANNEL_4(Type.Channel, 4),
  CHANNEL_5(Type.Channel, 5),
  CHANNEL_6(Type.Channel, 6),
  CHANNEL_7(Type.Channel, 7);

  private final Type type;
  private final int id;

  ConfigProtocol(Type t, int id) {
    this.type = t;
    this.id = id;
  }

  public boolean isOfType(Type t) {
    return this.type == t;
  }

  public int getID() {
    return this.id;
  }

  public enum Type {
    Module,
    Channel
  }
}
