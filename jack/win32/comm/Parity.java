package jack.win32.comm;

/**
 * SerialPort 对象的奇偶校验位
 */
public enum Parity {
    /**
     * 不发生奇偶校验检查。
     */
    NO(0),
    /**
     * 设置奇偶校验位，使位数等于奇数。
     */
    Odd(1),
    /**
     * 设置奇偶校验位，使位数等于偶数。
     */
    Even(2),
    /**
     * 将奇偶校验位保留为 1。
     */
    Mark(3),
    /**
     * 将奇偶校验位保留为 0。
     */
    Space(4);
    final byte value;
    Parity(int value){
        this.value = (byte)value;
    }
}