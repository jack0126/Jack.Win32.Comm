package jack.win32.comm;
import com.sun.jna.ptr.IntByReference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack,1298809673@qq.com
 * @version 1.0
 * @since 1.8
 */
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

    /**
     * 获取当前计算机的串行端口名的列表。
     * @return
     */
    public static List<String>  getPortNames() {
        int[] lpBuffer = new int[256];
        IntByReference lpCount = new IntByReference();
        KernelBase.INSTANCE.GetCommPorts(lpBuffer, 256, lpCount);
        return Arrays.stream(lpBuffer, 0, lpCount.getValue())
                .boxed()
                .map(e -> "COM" + e)
                .collect(Collectors.toList());
    }

    /**
     * 构建一个 SerialPort 对象
     * @param portName 串行端口名称
     * @param baudRate 波特率
     */
    public SerialPort(String portName, BaudRate baudRate) {
        this(portName, baudRate, Parity.NO, DataBit.Eight, StopBit.One);
    }

    /**
     * 构建一个 SerialPort 对象
     * @param portName 串行端口名称
     * @param baudRate 波特率
     * @param parity 奇偶校验位
     * @param dataBit 数据位数
     * @param stopBit 停止位数
     */
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

    /**
     * 打开一个串行端口的连接。
     * @throws SerialException
     */
    public void open() throws SerialException
    {
        checkPortNotOpen();
        internalSerialStream = new SerialStream(
                portName, baudRate.value, parity.value, dataBit.value, stopBit.value,
                readTimeout, writeTimeout,
                handshake, dtrEnable, rtsEnable, discardNull, parityReplace);
        internalSerialStream.setBufferSizes(readBufferSize, writeBufferSize);
    }

    /**
     * 关闭端口连接，将 IsOpen 属性设置为 false。
     */
    public void close() {
        if (internalSerialStream != null) {
            internalSerialStream.close();
            internalSerialStream = null;
        }
    }

    /**
     * 获取一个值，该值指示 SerialPort 对象的打开或关闭状态。
     * @return
     */
    public boolean isOpen() {
        return internalSerialStream != null && internalSerialStream.isOpen();
    }

    /**
     * 从 SerialPort 输入缓冲区读取一些字节并将那些字节写入字节数组中指定的偏移量处。
     * @param buf 接收缓冲区
     * @param offset 接收缓冲偏移量
     * @param count 读取的字节数
     * @return 实际读取的字节数
     * @throws SerialException
     */
    public int read(byte[]buf, int offset, int count) throws SerialException {
        checkPortOpened();
        return internalSerialStream.read(buf, offset, count);
    }

    /**
     * 将数据写入串行端口输出缓冲区。
     * @param buf 数据
     * @param offset 数据偏移量
     * @param count 发送的字节数
     * @return 实际发送的字节数
     * @throws SerialException
     */
    public int write(byte[] buf, int offset, int count) throws SerialException {
        checkPortOpened();
        return internalSerialStream.write(buf, offset, count);
    }

    /**
     * 刷新串行端口输出缓冲区
     * @throws SerialException
     */
    public void flush() throws SerialException {
        checkPortOpened();
        internalSerialStream.flush();
    }

    /**
     * 丢弃来自串行驱动程序的接收缓冲区的数据。
     * @throws SerialException
     */
    public void discardInBuffer() throws SerialException {
        checkPortOpened();
        internalSerialStream.discardInBuffer();
    }

    /**
     * 丢弃来自串行驱动程序的传输缓冲区的数据。
     * @throws SerialException
     */
    public void discardOutBuffer() throws SerialException {
        checkPortOpened();
        internalSerialStream.discardOutBuffer();
    }

    /**
     * 获取接收缓冲区中数据的字节数。
     * @return
     * @throws SerialException
     */
    public int getBytesToRead() throws SerialException {
        checkPortOpened();
        return internalSerialStream.getBytesToRead();
    }

    /**
     * 获取发送缓冲区中数据的字节数。
     * @return
     * @throws SerialException
     */
    public int getBytesToWrite() throws SerialException {
        checkPortOpened();
        return internalSerialStream.getBytesToWrite();
    }

    /**
     * 获取“可以发送”行的状态。
     * @return
     * @throws SerialException
     */
    public boolean isCtsHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isCtsHolding();
    }

    /**
     * 获取数据设置就绪 (DSR) 信号的状态。
     * @return
     * @throws SerialException
     */
    public boolean isDsrHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isDsrHolding();
    }

    /**
     * 获取端口的载波检测行的状态。
     * @return
     * @throws SerialException
     */
    public boolean isCDHolding() throws SerialException {
        checkPortOpened();
        return internalSerialStream.isCDHolding();
    }

    /**
     * 获取通信端口，包括但不限于所有可用的 COM 端口。
     * @return
     */
    public String getPortName() {
        return portName;
    }

    /***
     * 获取串行波特率。
     * @return
     */
    public BaudRate getBaudRate() {
        return baudRate;
    }

    /**
     * 获取每个字节的标准数据位长度。
     * @return
     */
    public DataBit getDataBit() {
        return dataBit;
    }

    /**
     * 获取奇偶校验检查协议。
     * @return
     */
    public Parity getParity() {
        return parity;
    }

    /**
     * 获取每个字节的标准停止位数。
     * @return
     */
    public StopBit getStopBit() {
        return stopBit;
    }

    /**
     * 获取读取操作超时的毫秒数。
     * @return
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * 设置读取操作超时的毫秒数。
     * @param readTimeout
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * 获取写入操作超时的毫秒数。
     * @return
     */
    public int getWriteTimeout() {
        return writeTimeout;
    }

    /**
     * 设置写入操作超时的毫秒数。
     * @param writeTimeout
     */
    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    /**
     * 获取串行端口数据传输的握手协议。
     * @return
     */
    public Handshake getHandshake() {
        return handshake;
    }

    /**
     * 使用 Handshake 中的值设置串行端口数据传输的握手协议。
     * @param handshake
     */
    public void setHandshake(Handshake handshake) {
        this.handshake = handshake;
    }

    /**
     * 获取一个值，该值指示 null 字节在端口和接收缓冲区之间传输时是否被忽略。
     * @return
     */
    public boolean isDiscardNull() {
        return discardNull;
    }

    /**
     * 设置一个值，该值指示 null 字节在端口和接收缓冲区之间传输时是否被忽略。
     * @param discardNull
     */
    public void setDiscardNull(boolean discardNull) {
        this.discardNull = discardNull;
    }

    /**
     * 获取一个值，该值在串行通信过程中启用数据终端就绪 (DTR) 信号。
     * @return
     */
    public boolean isDtrEnable() {
        return dtrEnable;
    }

    /**
     * 设置一个值，该值在串行通信过程中启用数据终端就绪 (DTR) 信号。
     * @param dtrEnable
     */
    public void setDtrEnable(boolean dtrEnable) {
        this.dtrEnable = dtrEnable;
    }

    /**
     * 获取一个值，该值指示在串行通信中是否启用请求发送 (RTS) 信号。
     * @return
     */
    public boolean isRtsEnable() {
        return rtsEnable;
    }

    /**
     * 设置一个值，该值指示在串行通信中是否启用请求发送 (RTS) 信号。
     * @param rtsEnable
     */
    public void setRtsEnable(boolean rtsEnable) {
        this.rtsEnable = rtsEnable;
    }

    /**
     * 获取一个字节，该字节在发生奇偶校验错误时替换数据流中的无效字节。
     * @return
     */
    public byte getParityReplace() {
        return parityReplace;
    }

    /**
     * 设置一个字节，该字节在发生奇偶校验错误时替换数据流中的无效字节。
     * @param parityReplace
     */
    public void setParityReplace(byte parityReplace) {
        this.parityReplace = parityReplace;
    }

    /**
     * 获取 SerialPort 输入缓冲区的大小。
     * @return
     */
    public int getReadBufferSize() {
        return readBufferSize;
    }

    /**
     * 设置 SerialPort 输入缓冲区的大小。
     * @param readBufferSize
     */
    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    /**
     * 获取串行端口输出缓冲区的大小。
     * @return
     */
    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    /**
     * 设置串行端口输出缓冲区的大小。
     * @param writeBufferSize
     */
    public void setWriteBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }
}
