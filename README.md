# Jack.Win32.Comm
JNA实现的 Java串口通信工具类



# jdk.version 1.8

# Demo

```java
import jack.win32.comm.*;

public class Demo
{

  public static void main(String[]args) throws SerialException
  {
  
    //查看本机串口
    
    SerialPort.getPortNames().forEach(System.out::println);
    
    SerialPort serialPort = new SerialPort("COM1", BaudRate.CBR_9600, Parity.NO, DataBit.Eight, StopBit.One);
    serialPort.setReadTimeout(1000);
    serialPort.setWriteTimeout(1000);
    serialPort.open();
    
    byte[] req = {0x01,0x02,0x03,0x04,0x05,0x06};
    serialPort.write(req, 0, req.length);
    serialPort.flush();
    
    byte[]resp = new byte[8];
    int count = serialPort.read(resp, 0, resp.length);
    //...
    serialPort.close();
  }
  
}
```
