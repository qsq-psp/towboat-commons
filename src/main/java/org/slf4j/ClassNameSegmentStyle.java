package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/26.
 */
@CodeHistory(date = "2025/8/26")
public enum ClassNameSegmentStyle {

    FULL {

        @Override
        public int calculateLength(@NotNull String string, int beginIndex, int endIndex) {
            return 0;
        }

        @Override
        public void transform(@NotNull String string, int beginIndex, int endIndex, @NotNull StringBuilder out) {

        }
    },
    ABBR {

        @Override
        public int calculateLength(@NotNull String string, int beginIndex, int endIndex) {
            return 0;
        }

        @Override
        public void transform(@NotNull String string, int beginIndex, int endIndex, @NotNull StringBuilder out) {

        }
    },
    LETTER {

        @Override
        public int calculateLength(@NotNull String string, int beginIndex, int endIndex) {
            return 0;
        }

        @Override
        public void transform(@NotNull String string, int beginIndex, int endIndex, @NotNull StringBuilder out) {

        }
    },
    NUMBER {

        @Override
        public int calculateLength(@NotNull String string, int beginIndex, int endIndex) {
            int numberEndIndex;
            for (numberEndIndex = endIndex; numberEndIndex > beginIndex; numberEndIndex--) {
                int ch = string.charAt(numberEndIndex - 1);
                if ('0' <= ch && ch <= '9') {
                    break;
                }
            }
            int numberBeginIndex;
            for (numberBeginIndex = numberEndIndex; numberBeginIndex > beginIndex; numberBeginIndex--) {
                int ch = string.charAt(numberBeginIndex - 1);
                if (!('0' <= ch && ch <= '9')) {
                    break;
                }
            }
            return numberEndIndex - numberBeginIndex;
        }

        @Override
        public void transform(@NotNull String string, int beginIndex, int endIndex, @NotNull StringBuilder out) {
            int numberEndIndex;
            for (numberEndIndex = endIndex; numberEndIndex > beginIndex; numberEndIndex--) {
                int ch = string.charAt(numberEndIndex - 1);
                if ('0' <= ch && ch <= '9') {
                    break;
                }
            }
            int numberBeginIndex;
            for (numberBeginIndex = numberEndIndex; numberBeginIndex > beginIndex; numberBeginIndex--) {
                int ch = string.charAt(numberBeginIndex - 1);
                if (!('0' <= ch && ch <= '9')) {
                    break;
                }
            }
            if (numberBeginIndex < numberEndIndex) {
                out.append(string, numberBeginIndex, numberEndIndex);
            }
        }
    },
    NONE {

        @Override
        public int calculateLength(@NotNull String string, int beginIndex, int endIndex) {
            return 0;
        }

        @Override
        public void transform(@NotNull String string, int beginIndex, int endIndex, @NotNull StringBuilder out) {
            // pass
        }
    };

    private static final long serialVersionUID = 0x966F14878442D911L;

    public abstract int calculateLength(@NotNull String string, @Index(of = "string") int beginIndex, @Index(of = "string", inclusive = false) int endIndex);

    public abstract void transform(@NotNull String string, @Index(of = "string") int beginIndex, @Index(of = "string", inclusive = false) int endIndex, @NotNull StringBuilder out);
}
