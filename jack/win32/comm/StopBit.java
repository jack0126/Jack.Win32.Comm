package jack.win32.comm;

public enum StopBit {
    One(0),
    OnePointFive(1),
    Two(2);
    final int value;
    StopBit(int value) {
        this.value = (byte) value;
    }
}
