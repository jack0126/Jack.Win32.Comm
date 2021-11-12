package jack.win32.comm;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

interface KernelBase extends StdCallLibrary {
    KernelBase INSTANCE = Native.loadLibrary("KernelBase", KernelBase.class);
    int GetCommPorts(Pointer /* PULONG */ lpPortNumbers, int uPortNumbersCount, IntByReference puPortNumbersFound);
}
