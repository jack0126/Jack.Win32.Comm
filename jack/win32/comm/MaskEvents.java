package jack.win32.comm;

interface MaskEvents {
    int EV_RXCHAR           = 0x0001;  // Any Character received
    int EV_RXFLAG           = 0x0002;  // Received certain character
    int EV_TXEMPTY          = 0x0004;  // Transmitt Queue Empty
    int EV_CTS              = 0x0008;  // CTS changed state
    int EV_DSR              = 0x0010;  // DSR changed state
    int EV_RLSD             = 0x0020;  // RLSD changed state
    int EV_BREAK            = 0x0040;  // BREAK received
    int EV_ERR              = 0x0080;  // Line status error occurred
    int EV_RING             = 0x0100;  // Ring signal detected
    int EV_PERR             = 0x0200;  // Printer error occured
    int EV_RX80FULL         = 0x0400;  // Receive buffer is 80 percent full
    int EV_EVENT1           = 0x0800;  // Provider specific event 1
    int EV_EVENT2           = 0x1000;  // Provider specific event 2
}
