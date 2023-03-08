package com.mentalab.packets.info;

import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.Packet;
import com.mentalab.packets.PacketUtils;
import com.mentalab.utils.constants.Topic;

import java.util.EnumSet;

import static com.mentalab.packets.PacketDataType.OFFSET;
import static com.mentalab.packets.PacketDataType.SLOPE;

public class ImpedanceInfoPacket extends Packet {
    protected float slope;
    protected double offset;

    public ImpedanceInfoPacket(double timeStamp) {
        super(timeStamp);
        super.type = EnumSet.of(SLOPE, OFFSET);
    }

    @Override
    public void populate(byte[] byteBuffer) throws InvalidDataException {
        this.slope = PacketUtils.bytesToInt(byteBuffer[0], byteBuffer[1]) * 10f;
    }

    public float getSlope() {
        return slope;
    }

    public double getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "PACKET: ImpedanceInfo";
    }

    @Override
    public Topic getTopic() {
        return Topic.DEVICE_INFO;
    }
}
