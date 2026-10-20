package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/8")
public class S64 extends Base2I64Slot implements Comparable<S64> {

    private static final long serialVersionUID = 0xcec8b436ede6d694L;

    public S64() {
        super();
    }

    public S64(long value) {
        super(value);
    }

    @NotNull
    @Override
    public S64 clone() {
        return new S64(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return value < 0L;
            }

            @Override
            public void setBit(boolean newValue) {
                if (value < 0L) {
                    if (!newValue) {
                        if (value == Long.MIN_VALUE) {
                            throw new ArithmeticException();
                        }
                        value = -value;
                    }
                } else {
                    if (newValue) {
                        value = -value;
                    }
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

    @Override
    public int compareTo(@NotNull S64 that) {
        return Long.compare(this.value, that.value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((S64) obj).value;
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + value + ")";
    }

    @CodeHistory(date = "2026/8/16")
    public static class S64Node1 extends S64 {

        private static final long serialVersionUID = 0x85688c9132725aa8L;

        S64Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/9/9")
    public static class S64NodeAllocator1 implements NodeAllocator1<S64Node1> {

        private static final long serialVersionUID = 0xb66960c913812edfL;

        @NotNull
        @Override
        public S64Node1 newNode() {
            return new S64Node1();
        }

        @Override
        public S64Node1[] newNodeArray(int length) {
            return new S64Node1[length];
        }

        @Override
        public int getTag(@NotNull S64Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S64Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S64Node1 getFirstLink(@NotNull S64Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S64Node1 self, S64Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/9/7")
    public static class S64Node2 extends S64 {

        private static final long serialVersionUID = 0x03774927d91bdc7aL;

        S64Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/9/10")
    public static class S64NodeAllocator2 implements NodeAllocator2<S64Node2> {

        @NotNull
        @Override
        public S64Node2 newNode() {
            return new S64Node2();
        }

        @Override
        public S64Node2[] newNodeArray(int length) {
            return new S64Node2[length];
        }

        @Override
        public int getTag(@NotNull S64Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S64Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S64Node2 getFirstLink(@NotNull S64Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S64Node2 self, S64Node2 node) {
            self.first = node;
        }

        @Override
        public S64Node2 getSecondLink(@NotNull S64Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull S64Node2 self, S64Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/9/12")
    public static class S64Node3 extends S64 {

        private static final long serialVersionUID = 0x92F54BE85299B7C7L;

        S64Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/9/28")
    public static class S64NodeAllocator3 implements NodeAllocator3<S64Node3> {


        @NotNull
        @Override
        public S64Node3 newNode() {
            return new S64Node3();
        }

        @Override
        public S64Node3[] newNodeArray(int length) {
            return new S64Node3[length];
        }

        @Override
        public int getTag(@NotNull S64Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S64Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S64Node3 getFirstLink(@NotNull S64Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S64Node3 self, S64Node3 node) {
            self.first = node;
        }

        @Override
        public S64Node3 getSecondLink(@NotNull S64Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull S64Node3 self, S64Node3 node) {
            self.second = node;
        }

        @Override
        public S64Node3 getThirdLink(@NotNull S64Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull S64Node3 self, S64Node3 node) {
            self.third = node;
        }
    }
}
