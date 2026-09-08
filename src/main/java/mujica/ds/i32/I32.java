package mujica.ds.i32;

import mujica.ds.slot.NodeAllocator1;
import mujica.ds.slot.NodeAllocator2;
import mujica.ds.slot.NodeAllocator3;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.Base16Appender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/6/15")
public class I32 implements I32Slot, Cloneable, Serializable {

    private static final long serialVersionUID = 0x7A8D4E595E0C9E2EL;

    int value;

    public I32() {
        super();
    }

    public I32(int value) {
        super();
        this.value = value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public I32 clone() {
        return new I32(value);
    }

    @Override
    public int getI32() {
        return value;
    }

    @Override
    public void setI32(int newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof I32 && this.value == ((I32) object).value;
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

    @CodeHistory(date = "2026/7/19")
    public static class I32Node1 extends I32 {

        private static final long serialVersionUID = 0xCAE10142A563E541L;

        I32Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/1")
    public static class I32NodeAllocator1 implements NodeAllocator1<I32Node1> {

        public static final I32NodeAllocator1 INSTANCE = new I32NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I32Node1 newNode() {
            return new I32Node1();
        }

        @Override
        public I32Node1[] newNodeArray(int length) {
            return new I32Node1[length];
        }

        @Override
        public int getTag(@NotNull I32Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I32Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I32Node1 getFirstLink(@NotNull I32Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I32Node1 self, I32Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/7/18")
    public static class I32Node2 extends I32 {

        private static final long serialVersionUID = 0x70E331E9B515D4CCL;

        I32Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/7/18")
    public static class I32NodeAllocator2 implements NodeAllocator2<I32Node2> {

        public static final I32NodeAllocator2 INSTANCE = new I32NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I32Node2 newNode() {
            return new I32Node2();
        }

        @Override
        public I32Node2[] newNodeArray(int length) {
            return new I32Node2[length];
        }

        @Override
        public int getTag(@NotNull I32Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I32Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I32Node2 getFirstLink(@NotNull I32Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I32Node2 self, I32Node2 node) {
            self.first = node;
        }

        @Override
        public I32Node2 getSecondLink(@NotNull I32Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I32Node2 self, I32Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/19")
    public static class I32Node3 extends I32 {

        private static final long serialVersionUID = 0xD430272C312D9681L;

        I32Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/7/19")
    public static class I32NodeAllocator3 implements NodeAllocator3<I32Node3> {

        public static final I32NodeAllocator3 INSTANCE = new I32NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public I32Node3 newNode() {
            return new I32Node3();
        }

        @Override
        public I32Node3[] newNodeArray(int length) {
            return new I32Node3[length];
        }

        @Override
        public int getTag(@NotNull I32Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull I32Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public I32Node3 getFirstLink(@NotNull I32Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I32Node3 self, I32Node3 node) {
            self.first = node;
        }

        @Override
        public I32Node3 getSecondLink(@NotNull I32Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I32Node3 self, I32Node3 node) {
            self.second = node;
        }

        @Override
        public I32Node3 getThirdLink(@NotNull I32Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull I32Node3 self, I32Node3 node) {
            self.third = node;
        }
    }
}
