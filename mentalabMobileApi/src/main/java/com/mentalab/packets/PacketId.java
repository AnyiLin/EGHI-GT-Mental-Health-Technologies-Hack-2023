package com.mentalab.packets;

import com.mentalab.packets.command.CmdReceivedPacket;
import com.mentalab.packets.command.CmdStatusPacket;
import com.mentalab.packets.info.DeviceInfoPacketV1;
import com.mentalab.packets.info.DeviceInfoPacketV2;
import com.mentalab.packets.info.ImpedanceInfoPacket;
import com.mentalab.packets.info.ImpedanceInfoPacketV1;
import com.mentalab.packets.info.ImpedanceInfoPacket_USBC;
import com.mentalab.packets.sensors.EnvironmentPacket;
import com.mentalab.packets.sensors.MarkerPacket;
import com.mentalab.packets.sensors.OrientationPacket;
import com.mentalab.packets.sensors.exg.Eeg32Packet;
import com.mentalab.packets.sensors.exg.Eeg94Packet;
import com.mentalab.packets.sensors.exg.Eeg98Packet;

public enum PacketId {
  ORIENTATION(13) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new OrientationPacket(timeStamp);
    }
  },
  ENVIRONMENT(19) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EnvironmentPacket(timeStamp);
    }
  },
  TIMESTAMP(27) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  },
  DISCONNECT(25) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  },
  INFO(99) {
    @Override
    public Packet createInstance(double timeStamp) { return new DeviceInfoPacketV1(timeStamp); }
  },
  INFO_V2(97) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new DeviceInfoPacketV2(timeStamp);
    }
  },
  EEG94(144) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new Eeg94Packet(timeStamp);
    }
  },
  EEG98(146) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new Eeg98Packet(timeStamp);
    }
  },
  EEG99S(30) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  },
  EEG99(62) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  },
  EEG94R(208) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  },
  EEG98R(210) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new Eeg98Packet(timeStamp);
    }
  },
  EEG32(148) {
    // TODO check ID value
    @Override
    public Packet createInstance(double timeStamp) {
      return new Eeg32Packet(timeStamp);
    }
  },
  CMDRCV(192) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new CmdReceivedPacket(timeStamp);
    }
  },
  CMDSTAT(193) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new CmdStatusPacket(timeStamp);
    }
  },
  MARKER(194) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new MarkerPacket(timeStamp);
    }
  },
  CALIBINFO(195) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new ImpedanceInfoPacketV1(timeStamp);
    }
  },
  CALIBINFO_USBC(197) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new ImpedanceInfoPacket_USBC(timeStamp);
    }
  },
  TRIGGER_OUT(177) {
    @Override
    public Packet createInstance(double timeStamp) {
      return new EmptyPacket(timeStamp);
    }
  };

  private final int value;

  PacketId(int value) {
    this.value = value;
  }

  public int getNumVal() {
    return value;
  }

  public abstract Packet createInstance(double timeStamp);
}
