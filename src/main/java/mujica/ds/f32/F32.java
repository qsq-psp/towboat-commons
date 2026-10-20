package mujica.ds.f32;

import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "PublicFloatSlot")
@CodeHistory(date = "2026/7/4")
public class F32 implements F32Slot, Comparable<F32>, Base2Float, Cloneable, Serializable {

    private static final long serialVersionUID = 0x5a0fec3769996a7bL;

    float value;

    public F32() {
        super();
    }

    public F32(float value) {
        super();
        this.value = value;
    }

    @Override
    public float getF32() {
        return value;
    }

    @Override
    public void setF32(float newValue) {
        value = newValue;
    }

    @Override
    public int compareTo(@NotNull F32 that) {
        return Float.compare(this.value, that.value);
    }

    @NotNull
    @Override
    public Base2Integer exponent() {
        return null;
    }

    @NotNull
    @Override
    public Base2Integer mantissa() {
        return null;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((F32) obj).value;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    public F32 clone() {
        return new F32(value);
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @CodeHistory(date = "2026/7/22")
    public static class F32Node1 extends F32 {

        private static final long serialVersionUID = 0xe211473cc5f17ad6L;

        F32Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/15")
    public static class F32NodeAllocator1 implements NodeAllocator1<F32Node1> {

        private static final long serialVersionUID = 0x46dc3a3bf8882facL;

        public static final F32NodeAllocator1 INSTANCE = new F32NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F32Node1 newNode() {
            return new F32Node1();
        }

        @Override
        public F32Node1[] newNodeArray(int length) {
            return new F32Node1[length];
        }

        @Override
        public int getTag(@NotNull F32Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F32Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F32Node1 getFirstLink(@NotNull F32Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F32Node1 self, F32Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/7/22")
    public static class F32Node2 extends F32 {

        private static final long serialVersionUID = 0x43e5e2b6415b19b5L;

        F32Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/8/20")
    public static class F32NodeAllocator2 implements NodeAllocator2<F32Node2> {

        private static final long serialVersionUID = 0x30d8bbf792c7a7a1L;

        public static final F32NodeAllocator2 INSTANCE = new F32NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F32Node2 newNode() {
            return new F32Node2();
        }

        @Override
        public F32Node2[] newNodeArray(int length) {
            return new F32Node2[length];
        }

        @Override
        public int getTag(@NotNull F32Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F32Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F32Node2 getFirstLink(@NotNull F32Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F32Node2 self, F32Node2 node) {
            self.first = node;
        }

        @Override
        public F32Node2 getSecondLink(@NotNull F32Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull F32Node2 self, F32Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/22")
    public static class F32Node3 extends F32 {

        private static final long serialVersionUID = 0x26d2187a3a10c20aL;

        F32Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/8/21")
    public static class F32NodeAllocator3 implements NodeAllocator3<F32Node3> {

        private static final long serialVersionUID = 0x77937a1cf213a077L;

        public static final F32NodeAllocator3 INSTANCE = new F32NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F32Node3 newNode() {
            return new F32Node3();
        }

        @Override
        public F32Node3[] newNodeArray(int length) {
            return new F32Node3[length];
        }

        @Override
        public int getTag(@NotNull F32Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F32Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F32Node3 getFirstLink(@NotNull F32Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F32Node3 self, F32Node3 node) {
            self.first = node;
        }

        @Override
        public F32Node3 getSecondLink(@NotNull F32Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull F32Node3 self, F32Node3 node) {
            self.second = node;
        }

        @Override
        public F32Node3 getThirdLink(@NotNull F32Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull F32Node3 self, F32Node3 node) {
            self.third = node;
        }
    }
}
