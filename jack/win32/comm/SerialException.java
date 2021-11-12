package jack.win32.comm;

import java.io.IOException;

public class SerialException extends IOException {
    public SerialException() {
    }

    public SerialException(String message) {
        super(message);
    }
    static void throwWinIOException() throws SerialException {
        int err = Kernel32.INSTANCE.GetLastError();
        throw new SerialException(String.format("win-io-error: %s.", ErrorFlags.getDescription(err)));
    }
}