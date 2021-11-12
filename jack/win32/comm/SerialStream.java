package jack.win32.comm;

import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import jack.win32.comm.internal.ComStatStruct;
import jack.win32.comm.internal.CommPropStruct;
import jack.win32.comm.internal.CommTimeoutsStruct;
import jack.win32.comm.internal.DCBStruct;

class SerialStream {
    private Pointer _handle;
    private String portName;
    private Handshake handshake;
    private byte parityReplace;
    private CommPropStruct commProp;
    private ComStatStruct comStat;
    private DCBStruct dcb;
    private CommTimeoutsStruct commTimeouts;
    private boolean rtsEnable;
    SerialStream(String portName, int baudRate, int parity, int dataBits, int stopBits,
                        int readTimeout, int writeTimeout,
                        Handshake handshake, boolean dtrEnable, boolean rtsEnable, boolean discardNull, byte parityReplace)
            throws SerialException
    {
        if (portName == null || portName.length() == 0 || !portName.toUpperCase().startsWith("COM")) {
            throw new SerialException("invalid serial port: " + portName);
        }
        Pointer safeFileHandle = Kernel32.INSTANCE.CreateFileW(new WString("\\\\.\\" + portName),
                -1073741824, 0, null,
                3, 0, null);
        if (safeFileHandle.equals(Kernel32.INVALID_HANDLE_VALUE)) {
            SerialException.throwWinIOException();
        }
        try {
            int fileType = Kernel32.INSTANCE.GetFileType(safeFileHandle);
            if (fileType != FileType.CHAR && fileType != FileType.UNKNOWN) {
                throw new SerialException("invalid serial port: " + portName);
            }
            this._handle = safeFileHandle;
            this.portName = portName;
            this.handshake = handshake;
            this.parityReplace = parityReplace;
            this.commProp = new CommPropStruct();
            IntByReference num = new IntByReference();
            if (!Kernel32.INSTANCE.GetCommProperties(_handle, commProp) || !Kernel32.INSTANCE.GetCommModemStatus(_handle, num)) {
                int lastWin32Error = Kernel32.INSTANCE.GetLastError();
                if (lastWin32Error != 87 && lastWin32Error != 6) {
                    SerialException.throwWinIOException();
                }
                throw new SerialException("invalid serial port extended: " + portName);
            }
            if (commProp.dwMaxBaud != 0 && baudRate > commProp.dwMaxBaud) {
                throw new SerialException("baudRate out of range,: " + baudRate);
            }
            comStat = new ComStatStruct();
            dcb = new DCBStruct();
            commTimeouts = new CommTimeoutsStruct();
            initializeDCB(baudRate, parity, dataBits, stopBits, discardNull);
            setDtrEnable(dtrEnable);
            this.rtsEnable = dcb.getFlag(12) == 1;
            if (handshake != Handshake.RequestToSend && handshake != Handshake.RequestToSendXOnXOff) {
                setRtsEnable(rtsEnable);
            }
            if (readTimeout < 0) {
                commTimeouts.ReadTotalTimeoutConstant = -2;
                commTimeouts.ReadTotalTimeoutMultiplier = -1;
            } else if (readTimeout == 0) {
                commTimeouts.ReadTotalTimeoutConstant = 0;
                commTimeouts.ReadTotalTimeoutMultiplier = 0;
            } else {
                commTimeouts.ReadTotalTimeoutConstant = readTimeout;
                commTimeouts.ReadTotalTimeoutMultiplier = -1;
            }
            commTimeouts.ReadIntervalTimeout = -1;
            commTimeouts.WriteTotalTimeoutMultiplier = 0;
            commTimeouts.WriteTotalTimeoutConstant = Math.max(writeTimeout, 0);
            if (!Kernel32.INSTANCE.SetCommTimeouts(_handle, commTimeouts)) {
                SerialException.throwWinIOException();
            }
        } catch(SerialException e) {
            Kernel32.INSTANCE.CloseHandle(safeFileHandle);
            _handle = null;
            throw e;
        }
    }

    private void initializeDCB(int baudRate, int parity, int dataBits, int stopBits, boolean discardNull)
            throws SerialException
    {
        if (!Kernel32.INSTANCE.GetCommState(_handle, dcb)) {
            SerialException.throwWinIOException();
        }
        dcb.DCBlength = dcb.size();
        dcb.BaudRate = baudRate;
        dcb.ByteSize = (byte)dataBits;
        dcb.StopBits = (byte)stopBits;
        dcb.Parity = (byte)parity;
        dcb.setFlag(1, (parity != 0) ? 1 : 0);
        dcb.setFlag(0, 1);
        dcb.setFlag(2, (handshake == Handshake.RequestToSend || handshake == Handshake.RequestToSendXOnXOff) ? 1 : 0);
        dcb.setFlag(3, 0);
        dcb.setFlag(4, 0);
        dcb.setFlag(6, 0);
        dcb.setFlag(9, (handshake == Handshake.XOnXOff || handshake == Handshake.RequestToSendXOnXOff) ? 1 : 0);
        dcb.setFlag(8, (handshake == Handshake.XOnXOff || handshake == Handshake.RequestToSendXOnXOff) ? 1 : 0);
        if (parity != 0) {
            dcb.setFlag(10, (parityReplace != 0) ? 1 : 0);
            dcb.ErrorChar = parityReplace;
        } else {
            dcb.setFlag(10, 0);
            dcb.ErrorChar = 0;
        }
        dcb.setFlag(11, discardNull ? 1 : 0);
        if (handshake == Handshake.RequestToSend || handshake == Handshake.RequestToSendXOnXOff) {
            dcb.setFlag(12, 2);
        } else if (dcb.getFlag(12) == 2) {
            dcb.setFlag(12, 0);
        }
        dcb.XonChar = 17;
        dcb.XoffChar = 19;
        dcb.XoffLim = (short)(commProp.dwCurrentRxQueue / 4);
        dcb.XonLim = (short)(commProp.dwCurrentRxQueue / 4);
        dcb.EofChar = 26;
        dcb.EvtChar = 26;
        if (!Kernel32.INSTANCE.SetCommState(_handle, dcb)) {
            SerialException.throwWinIOException();
        }
    }

    boolean isDtrEnable() {
        int dcbFlag = dcb.getFlag(4);
        return dcbFlag == 1;
    }

    private void setDtrEnable(boolean value) throws SerialException {
        int dcbFlag = dcb.getFlag(4);
        dcb.setFlag(4, value ? 1 : 0);
        if (!Kernel32.INSTANCE.SetCommState(_handle, dcb)) {
            dcb.setFlag(4, dcbFlag);
            SerialException.throwWinIOException();
        }
        if (!Kernel32.INSTANCE.EscapeCommFunction(_handle, value ? EscapeFunctions.SetDtr.func : EscapeFunctions.ClrDtr.func)) {
            SerialException.throwWinIOException();
        }
    }

    boolean isRtsEnable() {
        int dcbFlag = dcb.getFlag(12);
        if (dcbFlag == 2) {
            throw new UnsupportedOperationException("can't set rts with handshaking");
        }
        return dcbFlag == 1;
    }

    private void setRtsEnable(boolean value) throws SerialException {
        if (handshake == Handshake.RequestToSend || handshake == Handshake.RequestToSendXOnXOff) {
            throw new UnsupportedOperationException("can't set rts with handshaking");
        }
        if (value != rtsEnable){
            int dcbFlag = dcb.getFlag(12);
            rtsEnable = value;

            dcb.setFlag(12, value ? 1 : 0);
            if (!Kernel32.INSTANCE.SetCommState(_handle, dcb)) {
                dcb.setFlag(12, dcbFlag);
                rtsEnable = !rtsEnable;
                SerialException.throwWinIOException();
            }

            if (!Kernel32.INSTANCE.EscapeCommFunction(_handle, value ? EscapeFunctions.SetRts.func : EscapeFunctions.ClrRts.func)) {
                SerialException.throwWinIOException();
            }
        }
    }

    void setBufferSizes(int readBufferSize, int writeBufferSize) throws SerialException {
        if (!Kernel32.INSTANCE.SetupComm(_handle, readBufferSize, writeBufferSize)) {
            SerialException.throwWinIOException();
        }
    }

    int read(byte[] lpByteBuffer, int offset, int size) throws SerialException {
        if (lpByteBuffer == null) {
            return 0;
        }

        int len = Math.min(Math.max(lpByteBuffer.length - offset, 0), Math.max(size, 0));
        byte[] lpByte = new byte[1];
        IntByReference bytesRead = new IntByReference();

        for(int count = 0; count < len; count++) {
            bytesRead.setValue(0);
            if (!Kernel32.INSTANCE.ReadFile(_handle, lpByte, 1, bytesRead, null)) {
                int err = Kernel32.INSTANCE.GetLastError();
                if (err == 6) {
                    _handle = null;
                }

                if (err == 1121) {
                    throw new SerialTimeoutException();
                }

                SerialException.throwWinIOException();
            }

            if (bytesRead.getValue() == 0) {
                throw new SerialTimeoutException();
            }

            lpByteBuffer[offset + count] = lpByte[0];
        }
        return len;
    }

    int write(byte[]bytes, int offset, int size) throws SerialException {
        if (bytes == null) {
            return 0;
        }

        byte[] lpByteBuffer;
        if (offset == 0 && bytes.length == size) {
            lpByteBuffer = bytes;
        } else {
            int len = Math.min(Math.max(bytes.length - offset, 0), Math.max(size, 0));
            lpByteBuffer = new byte[len];
            System.arraycopy(bytes, offset, lpByteBuffer, 0, len);
        }

        if (lpByteBuffer.length == 0) {
            return 0;
        }

        IntByReference bytesRead = new IntByReference();
        if (!Kernel32.INSTANCE.WriteFile(_handle, lpByteBuffer, lpByteBuffer.length, bytesRead, null)) {
            int err = Kernel32.INSTANCE.GetLastError();
            if (err == 6) {
                _handle = null;
            }

            if (err == 1121) {
                throw new SerialTimeoutException();
            }

            SerialException.throwWinIOException();
        }
        return bytesRead.getValue();
    }

    void flush() throws SerialException {
        if (!Kernel32.INSTANCE.FlushFileBuffers(_handle)){
            int err = Kernel32.INSTANCE.GetLastError();
            if (err == 6) {
                _handle = null;
            }
            SerialException.throwWinIOException();
        }
    }

    void discardInBuffer() throws SerialException {
        if (!Kernel32.INSTANCE.PurgeComm(_handle, PurgeFunctionFlags.PURGE_RXABORT | PurgeFunctionFlags.PURGE_RXCLEAR)) {
            SerialException.throwWinIOException();
        }
    }

    void discardOutBuffer() throws SerialException {
        if (!Kernel32.INSTANCE.PurgeComm(_handle, PurgeFunctionFlags.PURGE_TXABORT | PurgeFunctionFlags.PURGE_TXCLEAR)) {
            SerialException.throwWinIOException();
        }
    }

    void close() {
        if (_handle == null) {
            return;
        }

        try {
            boolean flag = false;
            Kernel32.INSTANCE.SetCommMask(_handle, 0);
            if (!Kernel32.INSTANCE.EscapeCommFunction(_handle, EscapeFunctions.ClrDtr.func)) {
                int err = Kernel32.INSTANCE.GetLastError();
                if (err == 5 || err == 22 || err == 1617) {
                    flag = true;
                }
            }
            if (!flag) {
                flush();
                discardInBuffer();
                discardOutBuffer();
            }
        } catch (SerialException ignore) {
        } finally {
            Kernel32.INSTANCE.CloseHandle(_handle);
            _handle = null;
        }
    }

    int getBytesToRead() throws SerialException {
        if (!Kernel32.INSTANCE.ClearCommError(_handle, new IntByReference(), comStat)) {
            SerialException.throwWinIOException();
        }
        return comStat.cbInQue;
    }

    int getBytesToWrite() throws SerialException {
        if (!Kernel32.INSTANCE.ClearCommError(_handle, new IntByReference(), comStat)) {
            SerialException.throwWinIOException();
        }
        return comStat.cbOutQue;
    }

    boolean isCtsHolding() throws SerialException {
        IntByReference num = new IntByReference();
        if (!Kernel32.INSTANCE.GetCommModemStatus(_handle, num)) {
            SerialException.throwWinIOException();
        }
        return (ModemStatusFlags.MS_CTS_ON & num.getValue()) != 0;
    }

    boolean isDsrHolding() throws SerialException {
        IntByReference num = new IntByReference();
        if (!Kernel32.INSTANCE.GetCommModemStatus(_handle, num)) {
            SerialException.throwWinIOException();
        }
        return (ModemStatusFlags.MS_DSR_ON & num.getValue()) != 0;
    }

    boolean isCDHolding() throws SerialException {
        IntByReference num = new IntByReference();
        if (!Kernel32.INSTANCE.GetCommModemStatus(_handle, num)) {
            SerialException.throwWinIOException();
        }
        return (ModemStatusFlags.MS_RLSD_ON & num.getValue()) != 0;
    }

    int getReadTimeout() {
        int readTotalTimeoutConstant = commTimeouts.ReadTotalTimeoutConstant;
        if (readTotalTimeoutConstant == -2) {
            return -1;
        }
        return readTotalTimeoutConstant;
    }

    int getWriteTimeout() {
        int writeTotalTimeoutConstant = commTimeouts.WriteTotalTimeoutConstant;
        if (writeTotalTimeoutConstant != 0) {
            return writeTotalTimeoutConstant;
        }
        return -1;
    }

    private final IntByReference lpIsOpenIntCache = new IntByReference();
    boolean isOpen() {
        return _handle != null && Kernel32.INSTANCE.ClearCommError(_handle, lpIsOpenIntCache, comStat);
    }

}