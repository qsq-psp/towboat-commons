package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.NodeAllocator1;
import mujica.ds.slot.NodeAllocator2;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/18")
public class U64 extends Base2I64Slot {

    private static final long serialVersionUID = 0x382c04e5dc974b5aL;

    public U64() {
        super();
    }

    public U64(@DataType("u64") long value) {
        super(value);
    }

    @NotNull
    @Override
    public U64 clone() {
        return new U64(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return false;
            }

            @Override
            public void setBit(boolean newValue) {
                if (newValue && value != 0) {
                    throw new ArithmeticException();
                }
            }
        };
    }

    @NotNull
    @Override
    public ReadOnlyBase2Iterator readMagnitude(boolean msbFirst) {
        return null;
    }

    @NotNull
    @Override
    public Base2Iterator magnitude() {
        return null;
    }

    @CodeHistory(date = "2026/8/1")
    public static class U64Node1 extends U64 {

        private static final long serialVersionUID = 0xf99a94d4b47d0264L;

        U64Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/1")
    public static class U64NodeAllocator1 implements NodeAllocator1<U64Node1> {

        private static final long serialVersionUID = 0xfa916da2f319fedaL;

        public static final U64NodeAllocator1 INSTANCE = new U64NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public U64Node1 newNode() {
            return new U64Node1();
        }

        @Override
        public U64Node1[] newNodeArray(int length) {
            return new U64Node1[length];
        }

        @Override
        public int getTag(@NotNull U64Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull U64Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public U64Node1 getFirstLink(@NotNull U64Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull U64Node1 self, U64Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/8/19")
    public static class U64Node2 extends U64 {

        private static final long serialVersionUID = 0xb52eb96bd0284d0bL;

        U64Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/8/24")
    public static class U64NodeAllocator2 implements NodeAllocator2<U64Node2> {

        @NotNull
        @Override
        public U64Node2 newNode() {
            return new U64Node2();
        }

        @Override
        public U64Node2[] newNodeArray(int length) {
            return new U64Node2[length];
        }

        @Override
        public int getTag(@NotNull U64Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull U64Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public U64Node2 getFirstLink(@NotNull U64Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull U64Node2 self, U64Node2 node) {
            self.first = node;
        }

        @Override
        public U64Node2 getSecondLink(@NotNull U64Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull U64Node2 self, U64Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/9/22")
    public static class U64Node3 extends U64 {

        private static final long serialVersionUID = 0xACD4F2ABBF55E2A9L;

        U64Node3 first, second, third;

        int tag;
    }
}
