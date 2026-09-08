package mujica.ds.i8;

import mujica.ds.slot.NodeAllocator1;
import mujica.ds.slot.NodeAllocator2;
import mujica.ds.slot.NodeAllocator3;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.Base16Appender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

@CodeHistory(date = "2026/6/25")
public class I8 implements I8Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xEC429A7C188F0D47L;

    byte value;

    public I8() {
        super();
    }

    public I8(byte value) {
        super();
        this.value = value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public I8 clone() {
        return new I8(value);
    }

    @Override
    public byte getI8() {
        return value;
    }

    @Override
    public void setI8(byte newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof I8 && this.value == ((I8) object).value;
    }

    static final Base16Appender APPENDER = new Base16Appender(false, true);

    @Override
    @NotNull
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        APPENDER.append(value, sb);
        return sb.append(')').toString();
    }

    @CodeHistory(date = "2026/7/31")
    public static class I8Node1 extends I8 {

        private static final long serialVersionUID = 0x411F8CE3DC4F09D7L;

        I8Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/7/31")
    public static class I8NodeAllocator1 implements NodeAllocator1<I8Node1> {

        private static final long serialVersionUID = 0xEF4D9063DC5FD941L;

        public static final I8NodeAllocator1 INSTANCE = new I8NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I8Node1 newNode() {
            return new I8Node1();
        }

        @Override
        public I8Node1[] newNodeArray(int length) {
            return new I8Node1[length];
        }

        @Override
        public int getTag(@NotNull I8Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I8Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I8Node1 getFirstLink(@NotNull I8Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I8Node1 self, I8Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/7/28")
    public static class I8Node2 extends I8 {

        private static final long serialVersionUID = 0x3151C65DC5731C74L;

        I8Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/7/28")
    public static class I8NodeAllocator2 implements NodeAllocator2<I8Node2> {

        private static final long serialVersionUID = 0x6293C0196157E893L;

        public static final I8NodeAllocator2 INSTANCE = new I8NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I8Node2 newNode() {
            return new I8Node2();
        }

        @Override
        public void releaseNode(@NotNull I8Node2 node) {
            node.first = null;
            node.second = null;
        }

        @Override
        public I8Node2[] newNodeArray(int length) {
            return new I8Node2[length];
        }

        @Override
        public void releaseNodeArray(@NotNull I8Node2[] array) {
            Arrays.fill(array, null);
        }

        @Override
        public int getTag(@NotNull I8Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I8Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I8Node2 getFirstLink(@NotNull I8Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I8Node2 self, I8Node2 node) {
            self.first = node;
        }

        @Override
        public I8Node2 getSecondLink(@NotNull I8Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I8Node2 self, I8Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/30")
    public static class I8Node3 extends I8 {

        private static final long serialVersionUID = 0xF9EA8A549C9F9BACL;

        I8Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/7/30")
    public static class I8NodeAllocator3 implements NodeAllocator3<I8Node3> {

        private static final long serialVersionUID = 0xFDEC22A81B9F4515L;

        public static final I8NodeAllocator3 INSTANCE = new I8NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I8Node3 newNode() {
            return new I8Node3();
        }

        @Override
        public I8Node3[] newNodeArray(int length) {
            return new I8Node3[length];
        }

        @Override
        public int getTag(@NotNull I8Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I8Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I8Node3 getFirstLink(@NotNull I8Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I8Node3 self, I8Node3 node) {
            self.first = node;
        }

        @Override
        public I8Node3 getSecondLink(@NotNull I8Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I8Node3 self, I8Node3 node) {
            self.second = node;
        }

        @Override
        public I8Node3 getThirdLink(@NotNull I8Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull I8Node3 self, I8Node3 node) {
            self.third = node;
        }
    }
}
