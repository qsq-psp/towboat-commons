package mujica.ds.of_int.list;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;

@CodeHistory(date = "2025/3/12", name = "IntArrayHandle")
@CodeHistory(date = "2025/5/31")
public class IntListHandle implements Slot<int[]>, Serializable {

    private static final long serialVersionUID = 0x82814632642cdc7cL;

    private int[] array = PublicIntList.EMPTY.array;

    @NotNull
    @Override
    public int[] get() {
        return array;
    }

    @NotNull
    @Override
    public int[] set(@NotNull int[] newArray) {
        final int[] oldArray = array;
        array = newArray;
        return oldArray;
    }

    @NotNull
    public IntList getIntList() {
        return new SecretIntList();
    }

    private class SecretIntList extends AbstractIntList {

        private static final long serialVersionUID = 0xafc09c5b248acc6fL;

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
                return Arrays.equals(IntListHandle.this.array, ((PublicIntList) that).array);
            }
            return super.equals(that);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }
    }
}
