package mujica.ds.f64;

import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2018/7/3", project = "existence", name = "MtDouble")
@CodeHistory(date = "2020/2/22", project = "coo", name = "DoubleModel")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/21")
@FieldOrder("value") // only "value", not "double"
public class F64 implements F64Slot, Comparable<F64>, Base2Float, Cloneable, Serializable {

    private static final long serialVersionUID = 0x6a6bc6ef5338a773L;

    double value;

    public F64() {
        super();
    }

    public F64(double value) {
        super();
        this.value = value;
    }

    @Override
    public double getF64() {
        return value;
    }

    @Override
    public void setF64(double newValue) {
        value = newValue;
    }

    @Override
    public int compareTo(@NotNull F64 that) {
        return Double.compare(this.value, that.value);
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
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((F64) obj).value;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    public F64 clone() {
        return new F64(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @CodeHistory(date = "2026/8/6")
    public static class F64Node1 extends F64 {

        private static final long serialVersionUID = 0x0DC9FC278A91663BL;

        F64Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/6")
    public static class F64NodeAllocator1 implements NodeAllocator1<F64Node1> {

        private static final long serialVersionUID = 0xE88BC4876380EF42L;

        public static final F64NodeAllocator1 INSTANCE = new F64NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F64Node1 newNode() {
            return new F64Node1();
        }

        @Override
        public F64Node1[] newNodeArray(int length) {
            return new F64Node1[length];
        }

        @Override
        public int getTag(@NotNull F64Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F64Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F64Node1 getFirstLink(@NotNull F64Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F64Node1 self, F64Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/8/16")
    public static class F64Node2 extends F64 {

        private static final long serialVersionUID = 0x9E0E18410889A660L;

        F64Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/8/17")
    public static class F64NodeAllocator2 implements NodeAllocator2<F64Node2> {

        private static final long serialVersionUID = 0x960BBBC5594035D6L;

        public static final F64NodeAllocator2 INSTANCE = new F64NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F64Node2 newNode() {
            return new F64Node2();
        }

        @Override
        public F64Node2[] newNodeArray(int length) {
            return new F64Node2[length];
        }

        @Override
        public int getTag(@NotNull F64Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F64Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F64Node2 getFirstLink(@NotNull F64Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F64Node2 self, F64Node2 node) {
            self.first = node;
        }

        @Override
        public F64Node2 getSecondLink(@NotNull F64Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull F64Node2 self, F64Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/8/18")
    public static class F64Node3 extends F64 {

        private static final long serialVersionUID = 0x1F9FF63A6AB9E49AL;

        F64Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/8/19")
    public static class F64NodeAllocator3 implements NodeAllocator3<F64Node3> {

        private static final long serialVersionUID = 0xC94924DB88B99B72L;

        public static final F64NodeAllocator3 INSTANCE = new F64NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public F64Node3 newNode() {
            return new F64Node3();
        }

        @Override
        public F64Node3[] newNodeArray(int length) {
            return new F64Node3[length];
        }

        @Override
        public int getTag(@NotNull F64Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull F64Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public F64Node3 getFirstLink(@NotNull F64Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull F64Node3 self, F64Node3 node) {
            self.first = node;
        }

        @Override
        public F64Node3 getSecondLink(@NotNull F64Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull F64Node3 self, F64Node3 node) {
            self.second = node;
        }

        @Override
        public F64Node3 getThirdLink(@NotNull F64Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull F64Node3 self, F64Node3 node) {
            self.third = node;
        }
    }
}
