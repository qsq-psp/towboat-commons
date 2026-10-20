package mujica.ds.i32;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@CodeHistory(date = "2026/7/1")
public class U32 extends Base2I32Slot {

    private static final long serialVersionUID = 0xf919c433cbd61776L;

    public U32() {
        super();
    }

    public U32(@DataType("u32") int value) {
        super(value);
    }

    @NotNull
    @Override
    public U32 clone() {
        return new U32(value);
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
        if (msbFirst) {
            return new ReadOnlyBase2Iterator() {

                int index = Integer.SIZE;

                @Override
                public boolean next() {
                    return index-- > 0;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE) {
                        throw new NoSuchElementException();
                    }
                    return (value & (1 << index)) != 0;
                }
            };
        } else {
            return new ReadOnlyBase2Iterator() {

                int index = -1;

                @Override
                public boolean next() {
                    return index++ < Integer.SIZE;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE) {
                        throw new NoSuchElementException();
                    }
                    return (value & (1 << index)) != 0;
                }
            };
        }
    }

    @NotNull
    @Override
    public Base2Iterator magnitude() {
        return new Base2Iterator() {

            int index = -1;

            @Override
            public boolean next() {
                return index++ < Integer.SIZE;
            }

            @Override
            public boolean getBit() {
                if (index < 0  || index >= Integer.SIZE) {
                    throw new NoSuchElementException();
                }
                return (value & (1 << index)) != 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (index < 0  || index >= Integer.SIZE) {
                    throw new NoSuchElementException();
                }
                if (newValue) {
                    value |= 1 << index;
                } else {
                    value &= ~(1 << index);
                }
            }
        };
    }

    @Override
    @NotNull
    public String toString() {
        return "(" + (0xffffffffL & value) + ")";
    }

    @CodeHistory(date = "2026/8/1")
    public static class U32Node1 extends U32 {

        private static final long serialVersionUID = 0x5e8391974660c7f4L;

        U32Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/1")
    public static class U32NodeAllocator1 implements NodeAllocator1<U32Node1> {

        private static final long serialVersionUID = 0x0cb05c70bca281ebL;

        public static final U32NodeAllocator1 INSTANCE = new U32NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public U32Node1 newNode() {
            return new U32Node1();
        }

        @Override
        public U32Node1[] newNodeArray(int length) {
            return new U32Node1[length];
        }

        @Override
        public int getTag(@NotNull U32Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull U32Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public U32Node1 getFirstLink(@NotNull U32Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull U32Node1 self, U32Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/9/9")
    public static class U32Node2 extends U32 {

        private static final long serialVersionUID = 0x7c7791ce6c2d9bedL;

        U32Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/9/15")
    public static class U32NodeAllocator2 implements NodeAllocator2<U32Node2> {

        private static final long serialVersionUID = 0x0EC49C99D63EA3C8L;

        public static final U32NodeAllocator2 INSTANCE = new U32NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public U32Node2 newNode() {
            return new U32Node2();
        }

        @Override
        public U32Node2[] newNodeArray(int length) {
            return new U32Node2[length];
        }

        @Override
        public int getTag(@NotNull U32Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull U32Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public U32Node2 getFirstLink(@NotNull U32Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull U32Node2 self, U32Node2 node) {
            self.first = node;
        }

        @Override
        public U32Node2 getSecondLink(@NotNull U32Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull U32Node2 self, U32Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/9/16")
    public static class U32Node3 extends U32 {

        private static final long serialVersionUID = 0xCA27EC2E44A52EFAL;

        U32Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/9/17")
    public static class U32NodeAllocator3 implements NodeAllocator3<U32Node3> {

        private static final long serialVersionUID = 0x4BF14862BC2173E9L;

        public static final U32NodeAllocator3 INSTANCE = new U32NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public U32Node3 newNode() {
            return new U32Node3();
        }

        @Override
        public U32Node3[] newNodeArray(int length) {
            return new U32Node3[length];
        }

        @Override
        public int getTag(@NotNull U32Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull U32Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public U32Node3 getFirstLink(@NotNull U32Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull U32Node3 self, U32Node3 node) {
            self.first = node;
        }

        @Override
        public U32Node3 getSecondLink(@NotNull U32Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull U32Node3 self, U32Node3 node) {
            self.second = node;
        }

        @Override
        public U32Node3 getThirdLink(@NotNull U32Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull U32Node3 self, U32Node3 node) {
            self.third = node;
        }
    }
}
