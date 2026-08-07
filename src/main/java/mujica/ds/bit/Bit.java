package mujica.ds.bit;

import mujica.ds.slot.NodeAllocator2;
import mujica.ds.slot.NodeAllocator3;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

@CodeHistory(date = "2018/11/12", project = "TubeM", name = "MtBoolean")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/5/27", name = "PublicBitSlot")
@CodeHistory(date = "2026/7/23")
@FieldOrder("value") // only "value", not "bit"
public class Bit implements BitSlot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xbf54c7e30e0d41adL;

    public boolean value; // public access for convenience

    public Bit() {
        super();
    }

    public Bit(boolean value) {
        super();
        this.value = value;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    protected Bit clone() {
        return new Bit(value);
    }

    @Override
    public boolean getBit() {
        return value;
    }

    @Override
    public void setBit(boolean newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((Bit) obj).value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @CodeHistory(date = "2026/7/21")
    public static class BitNode1 extends Bit {

        private static final long serialVersionUID = 0x9A038731A41F8689L;

        BitNode1 first;

        int tag;
    }

    @CodeHistory(date = "2026/7/21")
    public static class BitNode2 extends Bit {

        private static final long serialVersionUID = 0x33C5297F13DF0E2EL;

        BitNode2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/7/28")
    public static class BitNodeAllocator2 implements NodeAllocator2<BitNode2> {

        public static final BitNodeAllocator2 INSTANCE = new BitNodeAllocator2();

        @NotNull
        @Override
        public BitNode2 newNode() {
            return new BitNode2();
        }

        @Override
        public void releaseNode(@NotNull BitNode2 node) {
            node.first = null;
            node.second = null;
        }

        @Override
        public BitNode2[] newNodeArray(int length) {
            return new BitNode2[length];
        }

        @Override
        public void releaseNodeArray(@NotNull BitNode2[] array) {
            Arrays.fill(array, null);
        }

        @Override
        public int getTag(@NotNull BitNode2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull BitNode2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public BitNode2 getFirstLink(@NotNull BitNode2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull BitNode2 self, BitNode2 node) {
            self.first = node;
        }

        @Override
        public BitNode2 getSecondLink(@NotNull BitNode2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull BitNode2 self, BitNode2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/21")
    public static class BitNode3 extends Bit {

        private static final long serialVersionUID = 0xEFA4EC12189ADCA1L;

        BitNode3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/7/23")
    public static class BitNodeAllocator3 implements NodeAllocator3<BitNode3> {
        
        @NotNull
        @Override
        public BitNode3 newNode() {
            return new BitNode3();
        }

        @Override
        public BitNode3[] newNodeArray(int length) {
            return new BitNode3[length];
        }

        @Override
        public int getTag(@NotNull BitNode3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull BitNode3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public BitNode3 getFirstLink(@NotNull BitNode3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull BitNode3 self, BitNode3 node) {
            self.first = node;
        }

        @Override
        public BitNode3 getSecondLink(@NotNull BitNode3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull BitNode3 self, BitNode3 node) {
            self.second = node;
        }

        @Override
        public BitNode3 getThirdLink(@NotNull BitNode3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull BitNode3 self, BitNode3 node) {
            self.third = node;
        }
    }
}
