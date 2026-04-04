package mujica.ds.of_char.sequence;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/3/7.
 */
public class UnitCharSequence extends TowboatCharSequence {

    private static final long serialVersionUID = 0x8ABBFFDCB78EB24CL;

    public char ch;

    public UnitCharSequence(char ch) {
        super();
        this.ch = ch;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public char charAt(int index) {
        if (index == 0) {
            return ch;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    @NotNull
    public CharSequence subSequence(int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return EmptyCharSequence.INSTANCE;
        }
        if (startIndex == 0 && endIndex == 1) {
            return this;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    @NotNull
    public String toString() {
        return String.valueOf(ch);
    }
}
