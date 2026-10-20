package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

@CodeHistory(date = "2026/9/13")
@ReferenceCode(groupId = "cn.hutool", artifactId = "hutool-core", version = "5.8.47", fullyQualifiedName = "cn.hutool.core.util.HashUtil")
public enum StringHash32 implements ToIntFunction<CharSequence> {

    ADD {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result += string.charAt(index);
            }
            return result;
        }
    },
    ROTATE {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = length;
            for (int index = 0; index < length; index++) {
                result = (result << 4) ^ (result >> 28) ^ string.charAt(index);
            }
            return result;
        }
    },
    ONE_BY_ONE {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result += string.charAt(index);
                result += (result << 10);
                result ^= (result >> 6);
            }
            result += (result << 3);
            result ^= (result >> 11);
            result += (result << 15);
            return result;
        }
    },
    BERNSTEIN {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = 33 * result + string.charAt(index);
            }
            return result;
        }
    },
    FNV {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            final int prime = 16777619;
            int result = (int) 2166136261L;
            for (int index = 0; index < length; index++) {
                result = (result ^ string.charAt(index)) * prime;
            }
            result += result << 13;
            result ^= result >> 7;
            result += result << 3;
            result ^= result >> 17;
            result += result << 5;
            return Math.abs(result);
        }
    },
    RS {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            final int beta = 378551;
            int alpha = 63689;
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = result * alpha + string.charAt(index);
                alpha = alpha * beta;
            }
            return result & Integer.MAX_VALUE;
        }
    },
    JS {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 1315423911;
            for (int index = 0; index < length; index++) {
                result ^= (result << 5) + string.charAt(index) + (result >> 2);
            }
            if (result < 0) { // modified absolute value
                if (result == Integer.MIN_VALUE) {
                    return 0;
                } else {
                    return -result;
                }
            } else {
                return result;
            }
        }
    },
    @Name(value = "ELF", language = "en")
    PJW {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = (result << 4) + string.charAt(index);
                int high = result & 0xf0000000;
                if (high != 0) {
                    result = (result ^ (high >> 24)) & (~high);
                }
            }
            return result & Integer.MAX_VALUE;
        }
    },
    BKDR {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = 131 * result + string.charAt(index);
            }
            return result;
        }
    },
    SDBM {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = string.charAt(index) + (result << 6) + (result << 16) - result;
            }
            return result & Integer.MAX_VALUE;
        }
    },
    DJB {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 5381;
            for (int index = 0; index < length; index++) {
                result = (result << 5) + result + string.charAt(index);
            }
            return result & Integer.MAX_VALUE;
        }
    },
    DEK {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = length;
            for (int index = 0; index < length; index++) {
                result = (result << 5) ^ (result >> 27) ^ string.charAt(index);
            }
            return result & Integer.MAX_VALUE;
        }
    },
    AP {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                if ((index & 1) == 0) {
                    result = (result << 7) ^ string.charAt(index) ^ (result >> 3);
                } else {
                    result = ~((result << 11) ^ string.charAt(index) ^ (result >> 5));
                }
            }
            return result;
        }
    },
    JDK {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            final int length = string.length();
            int result = 0;
            for (int index = 0; index < length; index++) {
                result = 31 * result + string.charAt(index);
            }
            return result;
        }
    },
    IDENTITY {
        @Override
        public int applyAsInt(@NotNull CharSequence string) {
            return System.identityHashCode(string);
        }
    };

    @Override
    public abstract int applyAsInt(@NotNull CharSequence string);
}
