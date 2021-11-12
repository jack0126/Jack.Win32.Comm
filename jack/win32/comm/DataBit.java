package jack.win32.comm;

public enum  DataBit {
    Five(5),
    Six(6),
    Seven(7),
    Eight(8);
    public final byte value;
    DataBit(int value) {
        this.value = (byte)value;
    }
}
