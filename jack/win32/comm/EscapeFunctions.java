package jack.win32.comm;

enum  EscapeFunctions {
    SetXOff(1),    // Simulate XOFF received
    SetXOn(2),     // Simulate XON received
    SetRts(3),     // Set RTS high
    ClrRts(4),     // Set RTS low
    SetDtr(5),     // Set DTR high
    ClrDtr(6),     // Set DTR low
    ResetDev(7),   // Reset device if possible
    SetBreak(8),   // Set the device break line.
    ClrBreak(9);   // Clear the device break line.
    public final int func;
    EscapeFunctions(int func) {
        this.func = func;
    }
}
