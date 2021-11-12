package jack.win32.comm;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

class ComStatStruct extends Structure {
    /*
    DWORD fCtsHold : 1;
    DWORD fDsrHold : 1;
    DWORD fRlsdHold : 1;
    DWORD fXoffHold : 1;
    DWORD fXoffSent : 1;
    DWORD fEof : 1;
    DWORD fTxim : 1;
    DWORD fReserved : 25;
     */
    public int flags;
    public int cbInQue;
    public int cbOutQue;
    @Override
    protected List<String> getFieldOrder() {
        return FIELDS;
    }
    private static final List<String> FIELDS = Arrays.asList("flags", "cbInQue", "cbOutQue");
    public int getFlag(int whichFlag)
    {
        int num = whichFlag != 7 ? 1 : 33554431;
        int num2 = flags & num << whichFlag;
        return num2 >> whichFlag;
    }
    public void setFlag(int whichFlag, int setting)
    {
        setting <<= whichFlag;
        int num = whichFlag != 7 ? 1 : 33554431;
        flags &= ~(num << whichFlag);
        flags |= setting;
    }

}