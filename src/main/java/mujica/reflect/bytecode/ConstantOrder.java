package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Created on 2025/10/17.
 */
@CodeHistory(date = "2025/10/17")
public enum ConstantOrder implements Comparator<ConstantInfo> {

    RANDOM_SHUFFLE {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            throw new UnsupportedOperationException();
        }
    },
    HASH_SHUFFLE {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            throw new UnsupportedOperationException();
        }
    },
    TAG_ASC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(a.tag(), b.tag());
        }
    },
    TAG_DESC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(b.tag(), a.tag());
        }
    },
    SECTION_ASC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(a.section(), b.section());
        }
    },
    SECTION_DESC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(b.section(), a.section());
        }
    },
    VERSION_ASC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(a.sinceVersion(), b.sinceVersion());
        }
    },
    VERSION_DESC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(b.sinceVersion(), a.sinceVersion());
        }
    },
    REFERENCE_COUNT_ASC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(a.referenceCount, b.referenceCount);
        }
    },
    REFERENCE_COUNT_DESC {
        @Override
        public int compare(@NotNull ConstantInfo a, @NotNull ConstantInfo b) {
            return Integer.compare(b.referenceCount, a.referenceCount);
        }
    };

    private static final long serialVersionUID = 0x4E7774A2599DA446L;
}
