package jack.win32.comm.internal;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class DCBStruct extends Structure {
    public int DCBlength;      /* sizeof(DCB)                     */
    public int BaudRate;       /* Baudrate at which running       */
    /*
    DWORD fBinary: 1;      Binary Mode (skip EOF check)
    DWORD fParity: 1;      Enable parity checking
    DWORD fOutxCtsFlow:1;  CTS handshaking on output
    DWORD fOutxDsrFlow:1;  DSR handshaking on output
    DWORD fDtrControl:2;   DTR Flow control
    DWORD fDsrSensitivity:1;  DSR Sensitivity
    DWORD fTXContinueOnXoff: 1;  Continue TX when Xoff sent
    DWORD fOutX: 1;        Enable output X-ON/X-OFF
    DWORD fInX: 1;         Enable input X-ON/X-OFF
    DWORD fErrorChar: 1;   Enable Err Replacement
    DWORD fNull: 1;        Enable Null stripping
    DWORD fRtsControl:2;   Rts Flow control
    DWORD fAbortOnError:1;  Abort all reads and writes on Error
    DWORD fDummy2:17;      Reserved
    */
    public int flags;
    public short wReserved;       /* Not currently used              */
    public short XonLim;          /* Transmit X-ON threshold         */
    public short XoffLim;         /* Transmit X-OFF threshold        */
    public byte ByteSize;        /* Number of bits/byte, 4-8        */
    public byte Parity;          /* 0-4=None,Odd,Even,Mark,Space    */
    public byte StopBits;        /* 0,1,2 = 1, 1.5, 2               */
    public byte XonChar;         /* Tx and Rx X-ON character        */
    public byte XoffChar;        /* Tx and Rx X-OFF character       */
    public byte ErrorChar;       /* Error replacement char          */
    public byte EofChar;         /* End of Input character          */
    public byte EvtChar;         /* Received Event character        */
    public short wReserved1;      /* Fill for now.                   */

    @Override
    protected List<String> getFieldOrder() {
        return FIELDS;
    }

    private static final List<String> FIELDS = Arrays.asList(
            "DCBlength",
            "BaudRate",
            "flags",
            "wReserved",
            "XonLim",
            "XoffLim",
            "ByteSize",
            "Parity",
            "StopBits",
            "XonChar",
            "XoffChar",
            "ErrorChar",
            "EofChar",
            "EvtChar",
            "wReserved1"
    );

    public int getFlag(int whichFlag) {
        int num;
        switch (whichFlag) {
            case 4:
            case 12:
                num = 3;
                break;
            case 15:
                num = 131071;
                break;
            default:
                num = 1;
                break;
        }
        int num2 = flags & num << whichFlag;
        return num2 >> whichFlag;
    }

    public void setFlag(int whichFlag, int setting) {
        setting <<= whichFlag;
        int num;
        switch (whichFlag) {
            case 4:
            case 12:
                num = 3;
                break;
            case 15:
                num = 131071;
                break;
            default:
                num = 1;
                break;
        }
        flags &= ~(num << whichFlag);
        flags |= setting;
    }

}