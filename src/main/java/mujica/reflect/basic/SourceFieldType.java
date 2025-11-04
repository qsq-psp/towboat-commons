package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import mujica.text.format.JavaFullyQualifiedNameValidator;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.function.Consumer;

@CodeHistory(date = "2025/9/10")
public class SourceFieldType implements NotLoadedFieldType<SourceFieldType>, Serializable {

    private static final long serialVersionUID = 0xA026EFF285E73602L;

    @ReferencePage(title = "JVMS12 The class File Format", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.3")
    private static char primitiveTag(@NotNull String name) {
        switch (name) {
            case "byte":
                return 'B';
            case "char":
                return 'C';
            case "double":
                return 'D';
            case "float":
                return 'F';
            case "int":
                return 'I';
            case "long":
                return 'J';
            case "short":
                return 'S';
            case "void":
                return 'V';
            case "boolean":
                return 'Z';
            default:
                return 0; // not a primitive
        }
    }

    /**
     * For example:
     * int
     * boolean
     * char[]
     * float[][]
     * java.lang.Object (prefix 'java.lang.' is necessary)
     * java.lang.Void (but void is not allowed)
     * java.util.ArrayList (no generic parameter)
     * java.util.Map$Entry (use '$', not '.')
     * java.util.concurrent.Future[]
     */
    @NotNull
    final String string;

    public SourceFieldType(@NotNull String string) {
        super();
        this.string = string;
    }

    @NotNull
    public static SourceFieldType of(@NotNull Class<?> clazz) {
        final String name = clazz.getName();
        if (name.startsWith("[")) {
            return (new BytecodeFieldType(name.replace('.', '/'))).toSourceFieldType();
        } else {
            return new SourceFieldType(name);
        }
    }

    @NotNull
    public static SourceFieldType of(@NotNull Field field) {
        return of(field.getType());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int n = string.length();
        for (int m = n; m > 0; m -= 2) {
            if (string.charAt(m - 1) == ']') {
                if (m >= 2 && string.charAt(m - 2) == '[') {
                    continue;
                }
                consumer.accept(new RuntimeException("brackets not paired"));
            }
            String core = string.substring(0, m);
            char tag = primitiveTag(core);
            if (tag == 0) {
                String message = JavaFullyQualifiedNameValidator.SOURCE.message(Locale.ENGLISH, string.substring(0, m));
                if (message != null) {
                    consumer.accept(new RuntimeException(message));
                }
            }
            break;
        }
    }

    @Override
    public void checkReturnTypeHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!"void".equals(string)) {
            checkHealth(consumer);
        }
    }

    @Override
    public boolean isArray() {
        return string.endsWith("[]");
    }

    @Override
    public boolean isPrimitive() {
        return primitiveTag(string) != 0;
    }

    @Override
    public SourceFieldType componentType() {
        if (string.endsWith("[]")) {
            return new SourceFieldType(string.substring(0, string.length() - 2));
        } else {
            return null;
        }
    }

    @Override
    public SourceFieldType arrayType() {
        return new SourceFieldType(string + "[]");
    }

    @Override
    @NotNull
    public String descriptorString() { // almost same with toBytecodeString(), but no throw
        final int n = string.length();
        for (int m = n; m > 0; m -= 2) {
            if (string.charAt(m - 1) == ']') {
                if (m >= 2 && string.charAt(m - 2) == '[') {
                    continue;
                }
                return "?";
            }
            String result = string.substring(0, m).replace('.', '/');
            char tag = primitiveTag(result);
            if (tag != 0) {
                result = String.valueOf(tag);
            } else {
                result = 'L' + result + ';';
            }
            if (m < n) {
                result = "[".repeat((n - m) / 2) + result;
            }
            return result;
        }
        return "?";
    }

    /**
     * For example:
     * long
     * [F
     * [[B
     * void
     * java.lang.Void
     * java.util.ArrayList
     * [Ljava.math.BigInteger;
     * java.lang.Runtime$Version
     * java.lang.Thread$State
     * java.lang.invoke.MethodHandles$Lookup
     */
    @NotNull
    @Override
    public String toClassGetName() {
        final int n = string.length();
        for (int m = n; m > 0; m -= 2) {
            if (string.charAt(m - 1) == ']') {
                if (m >= 2 && string.charAt(m - 2) == '[') {
                    continue;
                }
                throw new RuntimeException("brackets not paired");
            }
            if (m == n) {
                return string;
            }
            String result = string.substring(0, m); // no slash replace
            char tag = primitiveTag(result);
            if (tag != 0) {
                result = String.valueOf(tag);
            } else {
                result = 'L' + result + ';';
            }
            if (m < n) {
                result = "[".repeat((n - m) / 2) + result;
            }
            return result;
        }
        throw new RuntimeException("no core content");
    }

    /**
     * A little different from real class representation in source code.
     * This class use '$' to separate outer class and inner class.
     */
    @NotNull
    @Override
    public String toRealSourceString() {
        return string.replace('$', '.');
    }

    @NotNull
    @Override
    public String toSourceString() {
        return string;
    }

    @NotNull
    @Override
    public String toBytecodeString() {
        final int n = string.length();
        for (int m = n; m > 0; m -= 2) {
            if (string.charAt(m - 1) == ']') {
                if (m >= 2 && string.charAt(m - 2) == '[') {
                    continue;
                }
                throw new RuntimeException("brackets not paired");
            }
            String result = string.substring(0, m).replace('.', '/');
            char tag = primitiveTag(result);
            if (tag != 0) {
                result = String.valueOf(tag);
            } else {
                result = 'L' + result + ';';
            }
            if (m < n) {
                result = "[".repeat((n - m) / 2) + result;
            }
            return result;
        }
        throw new RuntimeException("no core content");
    }

    @NotNull
    @Override
    public SourceFieldType toSourceFieldType() {
        return this;
    }

    @NotNull
    @Override
    public BytecodeFieldType toBytecodeFieldType() {
        return new BytecodeFieldType(toBytecodeString());
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SourceFieldType && this.string.equals(((SourceFieldType) obj).string);
    }

    @NotNull
    @Override
    public String toString() {
        return "SourceFieldType[" + Quote.DEFAULT.apply(string) + "]";
    }
}
