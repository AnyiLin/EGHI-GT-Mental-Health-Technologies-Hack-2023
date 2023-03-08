package com.mentalab.packets.info;

import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.utils.constants.SamplingRate;
import com.mentalab.utils.constants.Topic;

import java.io.IOException;

import androidx.annotation.NonNull;

public abstract class DeviceInfoPacket extends Packet {
    protected SamplingRate samplingRate;
    protected int adsMask;

    protected DeviceInfoPacket(double timeStamp) {
        super(timeStamp);
    }

    @Override
    public abstract void populate(byte[] data) throws InvalidDataException, IOException;

    @NonNull
    @Override
    public String toString() {
        return "PACKET: DeviceInfo";
    }

    @Override
    public Topic getTopic() {
        return Topic.DEVICE_INFO;
    }

    public int getChannelMask() {
        return this.adsMask;
    }

    public SamplingRate getSamplingRate() {
        return samplingRate;
    }
}
