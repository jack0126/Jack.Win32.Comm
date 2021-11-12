package jack.win32.comm;

interface ErrorFlags {
    int CE_RXOVER = 0x0001;// Receive Queue overflow
    int CE_OVERRUN = 0x0002;// Receive Overrun Error
    int CE_RXPARITY = 0x0004;// Receive Parity Error
    int CE_FRAME = 0x0008;// Receive Framing error
    int CE_BREAK = 0x0010;// Break Detected
    int CE_TXFULL = 0x0100;// TX Queue is full
    int CE_PTO = 0x0200;// LPTx Timeout
    int CE_IOE = 0x0400;// LPTx I/O Error
    int CE_DNS = 0x0800;// LPTx Device not selected
    int CE_OOP = 0x1000;// LPTx Out-Of-Paper
    int CE_MODE = 0x8000;// Requested mode unsupported

    int IE_BADID = -1;// Invalid or unsupported id
    int IE_OPEN = -2;// Device Already Open
    int IE_NOPEN = -3;// Device Not Open
    int IE_MEMORY = -4;// Unable to allocate queues
    int IE_DEFAULT = -5;// Error in default parameters
    int IE_HARDWARE = -10;// Hardware Not Present
    int IE_BYTESIZE = -11;// Illegal Byte Size
    int IE_BAUDRATE = -12;// Unsupported BaudRate

    static String getDescription(int err) {
        switch (err) {
            case CE_RXOVER: return "receive queue overflow";
            case CE_OVERRUN: return "receive overrun error";
            case CE_RXPARITY: return "receive parity error";
            case CE_FRAME: return "receive framing error";
            case CE_BREAK: return "break detected";
            case CE_TXFULL: return "TX queue is full";
            case CE_MODE: return "requested mode unsupported";
            case IE_BADID: return "invalid or unsupported id";
            case IE_OPEN: return "device already open";
            case IE_NOPEN: return "device not open";
            case IE_MEMORY: return "unable to allocate queues";
            case IE_DEFAULT: return "error in default parameters";
            case IE_HARDWARE: return "hardware not present";
            case IE_BYTESIZE: return "illegal byte size";
            case IE_BAUDRATE: return "unsupported baud rate";
            default: return "no description";
        }
    }
}
