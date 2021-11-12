package jack.win32.comm;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE = Native.loadLibrary("kernel32", Kernel32.class);
    Pointer INVALID_HANDLE_VALUE = Pointer.createConstant(-1L);
    Pointer CreateFileW(WString lpFileName, int dwDesiredAccess, int dwShareMode,
                            Pointer lpSecurityAttributes,
                            int dwCreationDisposition, int dwFlagsAndAttributes,
                            Pointer hTemplateFile);
    boolean WriteFile(Pointer hFile, byte[] lpBuffer, int nNumberOfBytesToWrite,
                      IntByReference lpNumberOfBytesWritten, OverlappedStruct lpOverlapped);
    boolean ReadFile(Pointer hFile, byte[] lpBuffer, int nNumberOfBytesToRead,
                     IntByReference lpNumberOfBytesRead, OverlappedStruct lpOverlapped);
    boolean FlushFileBuffers(Pointer hFile);
    boolean CloseHandle(Pointer hObject);
    int GetFileType(Pointer hFile);
    int GetLastError();
    boolean GetCommProperties(Pointer hFile, CommPropStruct lpCommProp);
    boolean GetCommModemStatus(Pointer hFile, IntByReference lpModemStat);
    boolean GetCommState(Pointer hFile, DCBStruct lpDCB);
    boolean SetCommState(Pointer hFile, DCBStruct lpDCB);
    boolean GetCommTimeouts(Pointer hFile, CommTimeoutsStruct lpCommTimeouts);
    boolean SetCommTimeouts(Pointer hFile, CommTimeoutsStruct lpCommTimeouts);
    boolean SetupComm(Pointer hFile, int dwInQueue, int dwOutQueue);
    boolean PurgeComm(Pointer hFile, int dwFlags);
    boolean ClearCommError(Pointer hFile, IntByReference lpErrors, ComStatStruct lpStat);
    boolean SetCommBreak(Pointer hFile);
    boolean ClearCommBreak(Pointer hFile);
    boolean EscapeCommFunction(Pointer hFile, int dwFunc);
    boolean TransmitCommChar(Pointer hFile, byte cChar);
    boolean SetCommMask(Pointer hFile, int dwEvtMask);
    boolean WaitCommEvent(Pointer hFile, IntByReference lpEvtMask, OverlappedStruct lpOverlapped);
    boolean GetOverlappedResult(Pointer hFile, OverlappedStruct lpOverlapped,
                                IntByReference lpNumberOfBytesTransferred, boolean bWait);

}
