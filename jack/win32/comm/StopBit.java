package jack.win32.comm;

/**
 * SerialPort 对象上使用的停止位的数目
 */
public enum StopBit {
    /**
     * 使用一个停止位。
     */
    One(0),
    /**
     * 使用 1.5 个停止位。
     */
    OnePointFive(1),
    /**
     * 使用两个停止位。
     */
    Two(2);
    final int value;
    StopBit(int value) {
        this.value = (byte) value;
    }
}
