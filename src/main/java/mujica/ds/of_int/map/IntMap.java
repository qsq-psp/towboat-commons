package mujica.ds.of_int.map;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Mapping from int to int, initially, all 2**32 values map to zero, designed as a counter
 */
@CodeHistory(date = "2023/2/27", project = "Ultramarine")
@CodeHistory(date = "2025/3/25")
public abstract class IntMap implements DataStructure {

    public IntMap() {
        super();
    }

    @NotNull
    @Override
    public abstract IntMap duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public abstract int getInt(int key);

    public int putInt(int key, int newValue) {
        throw new UnsupportedOperationException();
    }

    public int addInt(int key, int delta) {
        return putInt(key, getInt(key) + delta);
    }

    public int addIntExact(int key, int delta) {
        return putInt(key, Math.addExact(key, delta));
    }

    public void increment(int key) {
        addInt(key, 1);
    }

    public void incrementExact(int key) {
        addIntExact(key, 1);
    }

    public void decrement(int key) {
        addInt(key, -1);
    }

    public void decrementExact(int key) {
        addIntExact(key, -1);
    }

    public void addAll(@NotNull IterableIntMap that) {
        for (IntMapEntry entry : that) {
            addInt(entry.getIntKey(), entry.getIntValue());
        }
    }

    public void addAll(@NotNull int[] array) {
        for (int num : array) {
            increment(num);
        }
    }

    public void addAll(@NotNull char[] array) {
        for (int num : array) {
            increment(num);
        }
    }

    public void addChars(@NotNull String string) {
        string.chars().forEach(this::increment);
    }

    public void addCodePoints(@NotNull String string) {
        string.codePoints().forEach(this::increment);
    }

    static final int TYPE_SHIFT = 30;

    static final int TYPE_MASK = 0x3 << TYPE_SHIFT;

    @SuppressWarnings("PointlessBitwiseExpression")
    static final int TYPE_TRIE = 0x0 << TYPE_SHIFT;

    static final int TYPE_ACCEPT = 0x1 << TYPE_SHIFT;

    /**
     * TRIE = dictionary tree
     * https://oi-wiki.org/string/trie/
     */
    public void buildTrie(String[] patterns) {
        int nextState = 1;
        for (String pattern : patterns) {
            int length = pattern.length();
            int state = 0;
            int index = 0;
            while (index < length) {
                int key = TYPE_TRIE | (state << Character.SIZE) | pattern.charAt(index++);
                int value = getInt(key);
                if (value != 0) {
                    state = value;
                } else {
                    state = nextState++;
                    putInt(key, state);
                }
            }
            putInt(TYPE_ACCEPT | state, 1);
        }
        if ((nextState - 1) >> TYPE_SHIFT != 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @return index in input string array, or -1 when not found
     */
    public int runTrie(@NotNull String string) {
        final int length = string.length();
        int state = 0;
        for (int index = 0; index < length; index++) {
            state = getInt(TYPE_TRIE | (state << Character.SIZE) | string.charAt(index));
            if (state == 0) {
                return -1;
            } else if (getInt(TYPE_ACCEPT | state) != 0) {
                return index;
            }
        }
        return -1;
    }

    static final int TYPE_CHAR = 0x2 << TYPE_SHIFT;

    static final int TYPE_FAIL = 0x3 << TYPE_SHIFT;

    /**
     * AC = Aho-Corasick automaton
     * https://oi-wiki.org/string/ac-automaton/
     */
    public void buildAC(@NotNull String[] patterns) {
        int charCount = 0;
        int nextState = 1;
        for (String pattern : patterns) {
            int length = pattern.length();
            int state = 0;
            int index = 0;
            while (index < length) {
                int ch = pattern.charAt(index++);
                int key = TYPE_CHAR | ch;
                if (getInt(key) != 0) {
                    putInt(key, 1);
                    putInt(TYPE_CHAR | (++charCount << Character.SIZE), ch);
                }
                key = TYPE_TRIE | (state << Character.SIZE) | ch;
                int value = getInt(key);
                if (value != 0) {
                    state = value;
                } else {
                    state = nextState++;
                    putInt(key, state);
                }
            }
            putInt(TYPE_ACCEPT | state, 1);
        }
        if (nextState >> (TYPE_SHIFT - Character.SIZE) != 0) { // states are always more than chars, so only check states
            throw new IndexOutOfBoundsException();
        }
        final char[] chars = new char[charCount];
        final char[] states = new char[nextState - 1];
        nextState = 0;
        for (int charIndex = 0; charIndex < charCount; charIndex++) {
            int ch = getInt(TYPE_CHAR | ((charIndex + 1) << Character.SIZE));
            chars[charIndex] = (char) ch;
            int state = getInt(TYPE_TRIE | ch);
            if (state != 0) {
                states[nextState++] = (char) state;
            }
        }
        for (int stateIndex = 0; stateIndex < nextState; stateIndex++) {
            int state = states[stateIndex];
            int fail = getInt(TYPE_FAIL | state);
            if (fail != 0 && getInt(TYPE_ACCEPT | fail) != 0) {
                putInt(TYPE_ACCEPT | state, 1);
            }
            for (int charIndex = 0; charIndex < charCount; charIndex++) {
                int ch = chars[charIndex];
                int key1 = TYPE_TRIE | (state << Character.SIZE) | ch;
                int state1 = getInt(key1);
                int state2 = getInt(TYPE_TRIE | (fail << Character.SIZE) | ch);
                if (state1 != 0) {
                    putInt(TYPE_FAIL | state1, state2);
                    states[nextState++] = (char) state1;
                } else {
                    putInt(key1, state2);
                }
            }
        }
        assert nextState == states.length;
    }

    @NotNull
    public byte[] runAC(@NotNull String string) {
        final int length = string.length();
        final byte[] result = new byte[length];
        int state = 0;
        for (int index = 0; index < length; index++) {
            state = getInt(TYPE_TRIE | (state << Character.SIZE) | string.charAt(index));
            result[index] = (byte) getInt(TYPE_ACCEPT | state);
        }
        return result;
    }

    /**
     * SAM = Suffix automaton
     * https://oi-wiki.org/string/sam/
     */
    public void buildSAM(String pattern) {
        //
    }
}
