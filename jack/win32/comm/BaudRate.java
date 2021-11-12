package jack.win32.comm;

//
// Baud rates at which the communication device operates
//
public enum  BaudRate {
    CBR_110(110),
    CBR_300(300),
    CBR_600(600),
    CBR_1200(1200),
    CBR_2400(2400),
    CBR_4800(4800),
    CBR_9600(9600),
    CBR_14400(14400),
    CBR_19200(19200),
    CBR_38400(38400),
    CBR_56000(56000),
    CBR_57600(57600),
    CBR_115200(115200),
    CBR_128000(128000),
    CBR_256000(256000);
    final int value;
    BaudRate(int value) {
        this.value = value;
    }
    public static BaudRate convert(int baudRate) {
        switch (baudRate) {
            case 110: return CBR_110;
            case 300: return CBR_300;
            case 600: return CBR_600;
            case 1200: return CBR_1200;
            case 2400: return CBR_2400;
            case 4800: return CBR_4800;
            case 9600: return CBR_9600;
            case 14400: return CBR_14400;
            case 19200: return CBR_19200;
            case 38400: return CBR_38400;
            case 56000: return CBR_56000;
            case 57600: return CBR_57600;
            case 115200: return CBR_115200;
            case 128000: return CBR_128000;
            case 256000: return CBR_256000;
            default: throw new IllegalArgumentException(String.format("baudRate = %d", baudRate));
        }
    }
}
