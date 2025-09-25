package mujica.text.word;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.IntUnaryOperator;

/**
 * Created by Hiro in bone on 2019/5/8.
 * Recreated in Ultramarine on 2020/12/26.
 * Recreated on 2025/3/7.
 */
public abstract class SplitBy implements Iterable<String>, Iterator<String> {

    protected final String string;

    protected int position;

    public SplitBy(String string) {
        super();
        this.string = string;
    }

    @Override
    @NotNull
    public Iterator<String> iterator() {
        position = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return position <= string.length(); // for splits based on separators
    }

    public void addAll(@NotNull Collection<String> collection) {
        for (String string : this) {
            collection.add(string);
        }
    }

    public int maxCount() {
        return string.length() + 2; // n elements have (n + 1) slots
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    public int count() {
        int count = 0;
        Iterator<String> iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    @NotNull
    public String[] toArray() {
        final ArrayList<String> list = new ArrayList<>();
        for (String string : this) {
            list.add(string);
        }
        final int size = list.size();
        final String[] array = new String[size];
        for (int index = 0; index < size; index++) {
            array[index] = list.get(index);
        }
        return array;
    }

    /**
     * Created in Ultramarine, named Characters.
     * Recreated on 2025/3/7.
     */
    public static class BetweenChars extends SplitBy {

        public BetweenChars(String string) {
            super(string);
        }

        @Override
        public String next() {
            return String.valueOf(string.charAt(position++));
        }
    }

    /**
     * Created in Ultramarine, named CodePoints.
     * Recreated on 2025/3/7.
     */
    public static class BetweenCodePoints extends SplitBy {

        public BetweenCodePoints(String string) {
            super(string);
        }

        @Override
        public String next() {
            final int index = position;
            if (string.codePointAt(index) < 0x10000) {
                position++;
            } else {
                position += 2;
            }
            return string.substring(index, position);
        }
    }

    public static class CRLF extends SplitBy {

        public CRLF(String string) {
            super(string);
        }

        @Override
        public String next() {
            int div1 = string.indexOf('\n', position);
            String segment;
            if (div1 < 0) {
                segment = string.substring(position);
                position = string.length() + 1;
            } else {
                int div0 = div1;
                while (div0 > position && string.charAt(div0 - 1) == '\r') {
                    div0--;
                }
                segment = string.substring(position, div0);
                position = div1 + 1;
            }
            return segment;
        }
    }

    public static class ContinuousWhitespace extends SplitBy {

        public ContinuousWhitespace(String string) {
            super(string);
        }

        @Override
        public String next() {
            int length = string.length();
            int div = position;
            while (div < length && string.charAt(div) > ' ') {
                div++;
            }
            String segment;
            if (div == length) {
                segment = string.substring(position);
                position = length + 1;
            } else {
                segment = string.substring(position, div);
                div++;
                while (div < length && string.charAt(div) <= ' ') {
                    div++;
                }
                position = div;
            }
            return segment;
        }
    }

    public static class SeparatorChar extends SplitBy {

        protected final char separatorChar;

        public SeparatorChar(String string, char separatorChar) {
            super(string);
            this.separatorChar = separatorChar;
        }

        @Override
        public String next() {
            int div = string.indexOf(separatorChar, position);
            String segment;
            if (div < 0) {
                segment = string.substring(position);
                position = string.length() + 1;
            } else {
                segment = string.substring(position, div);
                position = div + 1;
            }
            return segment;
        }
    }

    /**
     * if there are more than one separator chars together, they are considered as one separator.
     */
    public static class ContinuousSeparatorChar extends SeparatorChar {

        public ContinuousSeparatorChar(String string, char separatorChar) {
            super(string, separatorChar);
        }

        @Override
        public String next() {
            int length = string.length();
            int div = string.indexOf(separatorChar, position);
            String segment;
            if (div < 0) {
                segment = string.substring(position);
                position = length + 1;
            } else {
                segment = string.substring(position, div);
                div++;
                while (div < length && string.charAt(div) == separatorChar) {
                    div++;
                }
                position = div;
            }
            return segment;
        }
    }

    public static class SeparatorCharSequence extends SplitBy {

        protected final CharSequence separatorCharSequence;

        protected int separatorPosition;

        public SeparatorCharSequence(String string, CharSequence separatorCharSequence) {
            super(string);
            this.separatorCharSequence = separatorCharSequence;
        }

        @Override
        public String next() {
            while (separatorPosition < separatorCharSequence.length()) {
                int div = string.indexOf(separatorCharSequence.charAt(separatorPosition), position);
                separatorPosition++;
                if (div != -1) {
                    String segment = string.substring(position, div);
                    position = div + 1;
                    return segment;
                }
            }
            return string.substring(position);
        }

        @Override
        public int maxCount() {
            return separatorCharSequence.length() + 1;
        }
    }

    /**
     * zero is a special character type, it is not copied into output
     */
    public static class CharacterCategory extends SplitBy {

        @NotNull
        protected final IntUnaryOperator categoryFunction;

        protected int category;

        public CharacterCategory(String string, @NotNull IntUnaryOperator categoryFunction) {
            super(string);
            this.categoryFunction = categoryFunction;
        }

        @Override
        @NotNull
        public Iterator<String> iterator() {
            final int length = string.length();
            int newPosition = 0;
            int newCategory = 0;
            while (newPosition < length) {
                newCategory = categoryFunction.applyAsInt(string.charAt(newPosition));
                if (newCategory != 0) {
                    break;
                } else {
                    newPosition++;
                }
            }
            position = newPosition;
            category = newCategory;
            return this;
        }

        @Override
        public boolean hasNext() {
            return category != 0;
        }

        @Override
        public String next() {
            final int length = string.length();
            String next = null;
            int newPosition = position;
            int newCategory = 0;
            while (++newPosition < length) {
                newCategory = categoryFunction.applyAsInt(string.charAt(newPosition));
                if (category != newCategory) {
                    next = string.substring(position, newPosition);
                    break;
                }
            }
            while (newCategory == 0) {
                if (++newPosition >= length) {
                    break;
                }
                newCategory = categoryFunction.applyAsInt(string.charAt(newPosition));
            }
            if (next == null) {
                next = string.substring(position);
                newCategory = 0;
            }
            position = newPosition;
            category = newCategory;
            return next;
        }

        @Override
        public int maxCount() {
            return string.length();
        }
    }

    public static class BeforeUpper extends SplitBy {

        public BeforeUpper(String string) {
            super(string);
        }

        @Override
        public boolean hasNext() {
            return position < string.length(); // for splits based on initial characters
        }

        @Override
        public String next() {
            final int length = string.length();
            int index = position;
            for (index++; index < length; index++) {
                int ch = string.charAt(index);
                if (Character.isUpperCase(ch)) {
                    break;
                }
            }
            final String next = string.substring(position, index);
            position = index;
            return next;
        }

        @Override
        public int maxCount() {
            return string.length();
        }
    }

    public static class Camel extends SplitBy {

        public Camel(String string) {
            super(string);
        }

        @Override
        public boolean hasNext() {
            return position < string.length(); // for splits based on initial characters
        }

        private static final int STATE_UPPER = 0;
        private static final int STATE_ABBR = 1;
        private static final int STATE_LOWER = 2;
        private static final int STATE_OTHER = 3;

        @Override
        public String next() {
            final int length = string.length();
            int index = position;
            int ch = string.charAt(index);
            int state;
            if (Character.isUpperCase(ch)) {
                state = STATE_UPPER;
            } else if (Character.isLowerCase(ch)) {
                state = STATE_LOWER;
            } else {
                state = STATE_OTHER;
            }
            LOOP:
            for (index++; index < length; index++) {
                ch = string.charAt(index);
                switch (state) {
                    case STATE_UPPER:
                        if (Character.isUpperCase(ch)) {
                            state = STATE_ABBR;
                        } else if (Character.isLowerCase(ch)) {
                            state = STATE_LOWER;
                        } else {
                            break LOOP;
                        }
                        break;
                    case STATE_ABBR:
                        if (Character.isLowerCase(ch)) {
                            index--;
                            break LOOP;
                        } else if (!Character.isUpperCase(ch)) {
                            break LOOP;
                        }
                        break;
                    case STATE_LOWER:
                        if (!Character.isLowerCase(ch)) {
                            break LOOP;
                        }
                        break;
                    case STATE_OTHER:
                        if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
                            break LOOP;
                        }
                        break;
                }
            }
            final String next = string.substring(position, index);
            position = index;
            return next;
        }

        @Override
        public int maxCount() {
            return string.length();
        }
    }

    @NotNull
    public static SplitBy autoWords(@NotNull String string) {
        if (string.indexOf('_') != -1) {
            return new SeparatorChar(string, '_'); // snake case
        }
        if (string.indexOf('-') != -1) {
            return new SeparatorChar(string, '-'); // kebab case
        }
        return new Camel(string); // camel case
    }
}
