package com.mentalab.packets.info;

import com.mentalab.exception.InvalidDataException;
import com.mentalab.packets.PacketDataType;
import com.mentalab.packets.PacketUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;

public class DeviceInfoPacketV2 extends DeviceInfoPacket {

    private String boardId;
    private int memoryInfo;

    public DeviceInfoPacketV2(double timeStamp) {
        super(timeStamp);
        super.type = EnumSet.range(PacketDataType.ADS_MASK, PacketDataType.MEMORY_INFO);
    }

    @Override
    public void populate(byte[] data) throws InvalidDataException, IOException {
        this.boardId = new String(Arrays.copyOfRange(data, 0, 15), StandardCharsets.UTF_8);
        final int adsSamplingRateCode = PacketUtils.bytesToInt(data[18]);
        this.samplingRate = PacketUtils.adsCodeToSamplingRate(adsSamplingRateCode);
        this.adsMask = data[19] & 0xFF;
        this.memoryInfo = data[20];
    }

    @Override
    public int getDataCount() {
        return 4;
    }

    public String getBoardId() { return boardId; }

    public int getMemoryInfo() { return memoryInfo; }
}
