package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;

@CodeHistory(date = "2025/3/12", name = "PublicIntArray")
@CodeHistory(date = "2025/5/30")
public class PublicIntList extends AbstractIntList {

    private static final long serialVersionUID = 0x5808c2e84dfb1993L;

    public static final PublicIntList EMPTY = new PublicIntList(0);

    @NotNull
    public final int[] array;

    public PublicIntList(@NotNull int[] array) {
        super();
        this.array = array;
    }

    public PublicIntList(int length) {
        this(new int[length]);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public PublicIntList clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public PublicIntList duplicate() {
        return new PublicIntList(array.clone());
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
        if (that instanceof PublicIntList) {
            return Arrays.equals(this.array, ((PublicIntList) that).array);
        }
        return super.equals(that);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
