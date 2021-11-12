package jack.win32.comm;

public enum Handshake {
    None(0),
    XOnXOff(1),
    RequestToSend(2),
    RequestToSendXOnXOff(3);
    final int code;
    Handshake(int code) {
        this.code = code;
    }
}