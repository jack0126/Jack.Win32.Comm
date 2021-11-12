package jack.win32.comm;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerialPort {

    private String portName;
    private BaudRate baudRate;
    private DataBit dataBit;
    private Parity parity;
    private StopBit stopBit;

    private int readTimeout = -1;
    private int writeTimeout = -1;

    private Handshake handshake = Handshake.None;
    private boolean discardNull;
    private boolean dtrEnable;
    private boolean rtsEnable;
    private byte parityReplace = 63;

    private int readBufferSize = 4096;
    private int writeBufferSize = 2048;

    private SerialStream internalSerialStream;

    public static List<String>  getPortNames() {
        Pointer lpBuffer = new Memory(1024);
        IntByReference lpCount = new IntByReference();
        KernelBase.INSTANCE.GetCommPorts(lpBuffer, 256, lpCount);

        int[] buf = new int[lpCount.getValue()];
        lpBuffer.read(0, buf, 0, buf.length);
        return IntStream.of(buf).boxed().map(e -> "COM" + e).collect(Collectors.toList());
    }

    public SerialPort(String portName, BaudRate baudRate) {
        this(portName, baudRate, Parity.NO, DataBit.Eight, StopBit.One);
    }

    public SerialPort(String portName, BaudRate baudRate, Parity parity, DataBit dataBit, StopBit stopBit) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.parity = parity;
        this.dataBit = dataBit;
        this.stopBit = stopBit;
    }

    private void checkPortOpened() throws SerialException {
        if (internalSerialStream == null) {
            throw new SerialException("port not open");
        }
    }

    private void checkPortNotOpen() throws SerialException {
        if (internalSerialStream != null) {
            throw new SerialException("port already open");
        }
    }

    public void open() throws SerialException
    {
        checkPortNotOpen();
        internalSerialStream = new SerialStream(
                portName, baudRate.value, parity.value, dataBit.value, stopBit.value,
                readTimeout, writeTimeout,
                handshake, dtrEnable, rtsEnable, discardNull, parityReplace);
        internalSerialStream.setBufferSizes(readBufferSize, writeBufferSize);
    }

    public void close() {
        if (internalSerialStream != null) {
            internalSerialStream.close();
            internalSerialStream = null;
        }
    }

    public boolean isOpen() {
        return internalSerialStream != null && internalSerialStream.isOpen();
    }

    public byte read() throws SerialException {
        checkPortOpened();
        return internalSerialStream.read();
    }

    public int read(byte[]buf, int offset, int count) throws SerialException {
        checkPortOpened();
        if (buf == null) {
            return 0;
        }
        int len = Math.min(Math.max(buf.length - offset, 0), Math.max(count, 0));
        for(int i = 0; i < len; i++) {
            buf[offset + i] = internalSerialStream.read();
        }
        return len;
    }

    public int write(byte[] buf, int offset, int count) throws SerialException {
        checkPortOpened();
        if (buf == null) {
            return 0;
        }
        if (offset == 0 && buf.length == count) {
            return internalSerialStream.write(buf);
        }
        int len = Math.min(Math.max(buf.length - offset, 0), Math.max(count, 0));
        if (len == 0) {
            return 0;
        }
        byte[]buffer = new byte[len];
        System.arraycopy(buf, offset, buffer, 0, len);
        return internalSerialStream.write(buffer);
    }

    public void flush() throws SerialException {
        checkPortOpened();
        internalSerialStream.flush();
    }


    public void discardInBuffer() throws SerialException {
        checkPortOpened();
        internalSerialStream.discardInBuffer();
    }

    public void discardOutBuffer() throws SerialException {
        checkPortOpened();
        internalSerialStream.discardOutBuffer();
    }

    public int getBytesToRead() throws SerialException {
        checkPortOpened();
        return internalSerialStream.getBytesToRead();
    }

    public int getBytesToWrite() throws SerialException {
        checkPortOpened();
        return internalSerialStream.getBytesToWrite();
    }

    public boolean isCtsHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isCtsHolding();
    }

    public boolean isDsrHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isDsrHolding();
    }

    public boolean isCDHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isCDHolding();
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public BaudRate getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(BaudRate baudRate) {
        this.baudRate = baudRate;
    }

    public DataBit getDataBit() {
        return dataBit;
    }

    public void setDataBit(DataBit dataBits) {
        this.dataBit = dataBit;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public StopBit getStopBit() {
        return stopBit;
    }

    public void setStopBit(StopBit stopBit) {
        this.stopBit = stopBit;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Handshake getHandshake() {
        return handshake;
    }

    public void setHandshake(Handshake handshake) {
        this.handshake = handshake;
    }

    public boolean isDiscardNull() {
        return discardNull;
    }

    public void setDiscardNull(boolean discardNull) {
        this.discardNull = discardNull;
    }

    public boolean isDtrEnable() {
        return dtrEnable;
    }

    public void setDtrEnable(boolean dtrEnable) {
        this.dtrEnable = dtrEnable;
    }

    public boolean isRtsEnable() {
        return rtsEnable;
    }

    public void setRtsEnable(boolean rtsEnable) {
        this.rtsEnable = rtsEnable;
    }

    public byte getParityReplace() {
        return parityReplace;
    }

    public void setParityReplace(byte parityReplace) {
        this.parityReplace = parityReplace;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    public void setWriteBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }
}
