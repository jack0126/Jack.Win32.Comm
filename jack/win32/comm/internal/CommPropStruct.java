package jack.win32.comm.internal;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class CommPropStruct extends Structure {
    public short wPacketLength;
    public short wPacketVersion;
    public int dwServiceMask;
    public int dwReserved1;
    public int dwMaxTxQueue;
    public int dwMaxRxQueue;
    public int dwMaxBaud;
    public int dwProvSubType;
    public int dwProvCapabilities;
    public int dwSettableParams;
    public int dwSettableBaud;
    public short wSettableData;
    public short wSettableStopParity;
    public int dwCurrentTxQueue;
    public int dwCurrentRxQueue;
    public int dwProvSpec1;
    public int dwProvSpec2;
    public final char[] wcProvChar = new char[1];
    @Override
    protected List<String> getFieldOrder() {
        return FIELDS;
    }
    private static final List<String> FIELDS = Arrays.asList(
            "wPacketLength",
            "wPacketVersion",
            "dwServiceMask",
            "dwReserved1",
            "dwMaxTxQueue",
            "dwMaxRxQueue",
            "dwMaxBaud",
            "dwProvSubType",
            "dwProvCapabilities",
            "dwSettableParams",
            "dwSettableBaud",
            "wSettableData",
            "wSettableStopParity",
            "dwCurrentTxQueue",
            "dwCurrentRxQueue",
            "dwProvSpec1",
            "dwProvSpec2",
            "wcProvChar"
    );
}