package jack.win32.comm;

interface FileType {
    int UNKNOWN  = 0x0000;
    int DISK     = 0x0001;
    int CHAR     = 0x0002;
    int PIPE     = 0x0003;
    int REMOTE   = 0x8000;
}
