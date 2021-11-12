package jack.win32.comm;

interface PurgeFunctionFlags {
    int PURGE_TXABORT = 0x0001;  // Kill the pending/current writes to the comm port.
    int PURGE_RXABORT = 0x0002;  // Kill the pending/current reads to the comm port.
    int PURGE_TXCLEAR = 0x0004;  // Kill the transmit queue if there.
    int PURGE_RXCLEAR = 0x0008;  // Kill the typeahead buffer if there.

//#define LPTx                0x80    // Set if ID is for LPT device
}
