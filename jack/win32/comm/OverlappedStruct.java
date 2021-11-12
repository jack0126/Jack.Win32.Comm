package jack.win32.comm;

import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

class OverlappedStruct extends Structure {
    public int Internal;
    public int InternalHigh;
    public DummyUnionName.ByValue DUMMYUNIONNAME;
    public int /* HANDLE */  hEvent;
    @Override
    protected List<String> getFieldOrder() {
        return FIELDS;
    }
    private static final List<String> FIELDS = Arrays.asList("Internal", "InternalHigh", "DUMMYUNIONNAME", "hEvent");
    public static class DummyUnionName extends Union {
        public DummyStructName.ByValue DUMMYSTRUCTNAME;
        public int /* PVOID */ Pointer;
        @Override
        protected List<String> getFieldOrder() {
            return FIELDS;
        }
        private static final List<String> FIELDS = Arrays.asList("DUMMYSTRUCTNAME", "Pointer");
        public static class ByValue extends DummyUnionName implements Union.ByValue {}
        public static class DummyStructName extends Structure {
            public int Offset;
            public int OffsetHigh;
            public static class ByValue extends DummyStructName implements Structure.ByValue {}
            @Override
            protected List<String> getFieldOrder() {
                return FIELDS;
            }
            private static final List<String> FIELDS = Arrays.asList("Offset", "OffsetHigh");;
        }
    }
}