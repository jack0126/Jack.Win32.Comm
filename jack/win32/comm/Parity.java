package jack.win32.comm;

public enum Parity {
    NO(0),
    Odd(1),
    Even(2),
    Mark(3),
    Space(4);
    public final byte value;
    Parity(int value){
        this.value = (byte)value;
    }
}