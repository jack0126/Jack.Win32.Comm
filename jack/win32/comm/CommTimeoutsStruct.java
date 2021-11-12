package jack.win32.comm;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

class CommTimeoutsStruct extends Structure {
    public int ReadIntervalTimeout;          /* Maximum time between read chars. */
    public int ReadTotalTimeoutMultiplier;   /* Multiplier of characters.        */
    public int ReadTotalTimeoutConstant;     /* Constant in milliseconds.        */
    public int WriteTotalTimeoutMultiplier;  /* Multiplier of characters.        */
    public int WriteTotalTimeoutConstant;    /* Constant in milliseconds.        */
    @Override
    protected List<String> getFieldOrder() {
        return FIELDS;
    }
    private static final List<String> FIELDS = Arrays.asList(
            "ReadIntervalTimeout",
            "ReadTotalTimeoutMultiplier",
            "ReadTotalTimeoutConstant",
            "WriteTotalTimeoutMultiplier",
            "WriteTotalTimeoutConstant"
    );
}