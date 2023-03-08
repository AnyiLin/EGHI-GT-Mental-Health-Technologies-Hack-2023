package com.mentalab.packets.sensors.exg;

import android.util.Log;

import com.mentalab.packets.PacketDataType;

import java.util.EnumSet;

public class Eeg32Packet extends EEGPacket{
    private static final int NO_CHANNELS = 32;

    public Eeg32Packet(double timeStamp) {
        super(timeStamp, NO_CHANNELS);
        super.type = EnumSet.range(PacketDataType.CH1, PacketDataType.CH32);
    }
}
