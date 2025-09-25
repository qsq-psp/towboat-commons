package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;

@CodeHistory(date = "2025/3/13", name = "PrivateIntArray")
@CodeHistory(date = "2025/5/30")
public class PrivateIntList extends AbstractIntList {

    private static final long serialVersionUID = 0xb65f393ed7566b1cL;

    @NotNull
    private int[] array;

    public PrivateIntList(@NotNull int[] array) {
        super();
        this.array = array;
    }

    @Override
    public PrivateIntList clone() {
        try {
            PrivateIntList that = (PrivateIntList) super.clone();
            assert that.array == this.array;
            that.array = this.array.clone();
            return that;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @NotNull
    @Override
    public PrivateIntList duplicate() {
        return new PrivateIntList(array.clone());
    }

    @Override
    public int intLength() {
        return array.length;
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(array, 0);
    }

    @NotNull
    @Override
    public int[] toArray() {
        return array.clone();
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public int getInt(int i) {
        return array[i];
    }

    @Override
    public boolean equals(@NotNull IntList that) {
        if (that instanceof PrivateIntList) {
            return Arrays.equals(this.array, ((PrivateIntList) that).array);
        }
        if (that instanceof PublicIntList) {
            return Arrays.equals(this.array, ((PublicIntList) that).array);
        }
        return super.equals(that);
    }

    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
