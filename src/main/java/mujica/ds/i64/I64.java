package mujica.ds.i64;

import mujica.ds.i32.I32;
import mujica.ds.slot.NodeAllocator1;
import mujica.ds.slot.NodeAllocator2;
import mujica.ds.slot.NodeAllocator3;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.text.number.Base16Appender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2023/11/29", project = "OSHI")
@CodeHistory(date = "2023/12/1", project = "Ultramarine", name = "LongQuantity")
@CodeHistory(date = "2023/12/6", project = "Ultramarine", name = "LongBox")
@CodeHistory(date = "2025/3/9", name = "PublicI64Slot")
@CodeHistory(date = "2026/7/1")
@FieldOrder("value") // only "value", not "long"
public class I64 implements I64Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xb0ca00edf2e5e344L;

    long value;

    public I64() {
        super();
    }

    public I64(long value) {
        super();
        this.value = value;
    }

    @Override
    @NotNull
    public I64 clone() {
        return new I64();
    }

    @Override
    public long getI64() {
        return value;
    }

    @Override
    public void setI64(long newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof I64 && this.value == ((I64) object).value;
    }

    static final Base16Appender APPENDER = new Base16Appender(false, true);

    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        APPENDER.append(value, sb);
        return sb.append(')').toString();
    }

    @CodeHistory(date = "2026/7/21")
    public static class I64Node1 extends I64 {

        private static final long serialVersionUID = 0x211F9396194DF282L;

        I64Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/2")
    public static class I64NodeAllocator1 implements NodeAllocator1<I64Node1> {

        public static final I64NodeAllocator1 INSTANCE = new I64NodeAllocator1();

        @NotNull
        @Override
        public I64Node1 newNode() {
            return new I64Node1();
        }

        @Override
        public I64Node1[] newNodeArray(int length) {
            return new I64Node1[length];
        }

        @Override
        public int getTag(@NotNull I64Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I64Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I64Node1 getFirstLink(@NotNull I64Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I64Node1 self, I64Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/7/21")
    public static class I64Node2 extends I64 {

        private static final long serialVersionUID = 0x99FD2C85026CEED6L;

        I64Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/7/23")
    public static class I64NodeAllocator2 implements NodeAllocator2<I64Node2> {

        public static final I64NodeAllocator2 INSTANCE = new I64NodeAllocator2();

        @NotNull
        @Override
        public I64Node2 newNode() {
            return new I64Node2();
        }

        @Override
        public I64Node2[] newNodeArray(int length) {
            return new I64Node2[length];
        }

        @Override
        public int getTag(@NotNull I64Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I64Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I64Node2 getFirstLink(@NotNull I64Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I64Node2 self, I64Node2 node) {
            self.first = node;
        }

        @Override
        public I64Node2 getSecondLink(@NotNull I64Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I64Node2 self, I64Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/21")
    public static class I64Node3 extends I64 {

        private static final long serialVersionUID = 0x409C3CA230790E6EL;

        I64Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/8/5")
    public static class I64NodeAllocator3 implements NodeAllocator3<I64Node3> {

        public static final I64NodeAllocator3 INSTANCE = new I64NodeAllocator3();

        @NotNull
        @Override
        public I64Node3 newNode() {
            return new I64Node3();
        }

        @Override
        public I64Node3[] newNodeArray(int length) {
            return new I64Node3[length];
        }

        @Override
        public int getTag(@NotNull I64Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I64Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I64Node3 getFirstLink(@NotNull I64Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I64Node3 self, I64Node3 node) {
            self.first = node;
        }

        @Override
        public I64Node3 getSecondLink(@NotNull I64Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I64Node3 self, I64Node3 node) {
            self.second = node;
        }

        @Override
        public I64Node3 getThirdLink(@NotNull I64Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull I64Node3 self, I64Node3 node) {
            self.third = node;
        }
    }
}
