package jack.win32.comm;

/**
 * SerialPort 对象建立串行端口通信时使用的控制协议
 */
public enum Handshake {
    /**
     * 没有用于握手的控件。
     */
    None(0),
    /**
     * 使用 XON/XOFF 软件控制协议。
     * 发送 XOFF 控制以停止数据传输。
     * 发送 XON 控制以继续传输。
     * 使用这些软件控制，而不是使用请求发送 (RTS) 和清除发送 (CTS) 硬件控制。
     */
    XOnXOff(1),
    /**
     * 使用请求发送 (RTS) 硬件流控制。
     * RTS 发出信号，指出数据可用于传输。
     * 如果输入缓冲区已满，RTS 行将被设置为 false。
     * 当输入缓冲区中有更多可用空间时，RTS 行将被设置为 true。
     */
    RequestToSend(2),
    /**
     * 同时使用请求发送 (RTS) 硬件控制和 XON/XOFF 软件控制。
     */
    RequestToSendXOnXOff(3);
    final int code;
    Handshake(int code) {
        this.code = code;
    }
}
