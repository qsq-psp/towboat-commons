package mujica.ds.of_long.list;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created on 2026/6/6.
 */
public class PublicLongList extends AbstractLongList {

    private static final long serialVersionUID = 0x1D369ABF888033F8L;

    @NotNull
    public final long[] array;

    public PublicLongList(@NotNull long[] array) {
        super();
        this.array = array;
    }

    public PublicLongList(int length) {
        this(new long[length]);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public PublicLongList clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public PublicLongList duplicate() {
        return new PublicLongList(array.clone());
    }

    @Override
    public int longLength() {
        return array.length;
    }

    @NotNull
    @Override
    public Spliterator<Long> spliterator() {
        return Spliterators.spliterator(array, 0);
    }

    @NotNull
    @Override
    public long[] toLongArray() {
        return array.clone();
    }

    @Override
    public void getRange(int srcOffset, @NotNull long[] dst, int dstOffset, int length) {
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public long getLong(int i) {
        return array[i];
    }

    @Override
    public boolean equals(@NotNull LongList that) {
        if (that instanceof PublicLongList) {
            return Arrays.equals(this.array, ((PublicLongList) that).array);
        }
        return super.equals(that);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
