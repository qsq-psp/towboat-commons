package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/9")
public class SlicedCharSequence extends FilteredCharSequence {

    private static final long serialVersionUID = 0x4C8D91836250D3D4L;

    @Index(of = "original")
    protected final int startIndex;

    @Index(of = "original", inclusive = false)
    protected final int endIndex;

    public SlicedCharSequence(@NotNull CharSequence original, int startIndex, int endIndex) {
        super(original);
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= original.length())) {
            throw new IndexOutOfBoundsException();
        }
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int length() {
        return endIndex - startIndex;
    }

    @Override
    public char charAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        return original.charAt(index);
    }

    @Override
    public CharSequence subSequence(int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            startIndex += this.startIndex;
            endIndex += this.startIndex;
            if (!(this.startIndex <= startIndex && endIndex <= this.endIndex)) {
                throw new IndexOutOfBoundsException();
            }
            return new SlicedCharSequence(original, startIndex, endIndex);
        } else if (startIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        } else {
            return EmptyCharSequence.INSTANCE;
        }
    }
}
