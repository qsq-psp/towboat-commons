package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.NodeAllocator1;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/18")
public class U64 extends Base2I64Slot {

    private static final long serialVersionUID = 0x382C04E5DC974B5AL;

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

        private static final long serialVersionUID = 0xF99A94D4B47D0264L;

        U64Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/1")
    public static class U64NodeAllocator1 implements NodeAllocator1<U64Node1> {

        private static final long serialVersionUID = 0xFA916DA2F319FEDAL;

        public static final U64NodeAllocator1 INSTANCE = new U64NodeAllocator1();

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
}
