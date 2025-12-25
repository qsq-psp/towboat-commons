package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import mujica.text.format.JavaFullyQualifiedNameValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.function.Consumer;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2025/9/10")
@ReferencePage(title = "JVMS12 The class File Format", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.3")
public class BytecodeFieldType // according to JVMS, 'bytecode' is one word
        implements NotLoadedFieldType<BytecodeFieldType>, Serializable {

    private static final long serialVersionUID = 0x359f3a80059fcf6aL;

    /**
     * For example:
     * I
     * Z
     * [C
     * [[F
     * Ljava.lang.Object;
     * Ljava.lang.Void;
     * Ljava.util.Map$Entry;
     * [Ljava.util.concurrent.Future;
     */
    @NotNull
    final String string;

    public BytecodeFieldType(@NotNull String string) {
        super();
        this.string = string;
    }

    @NotNull
    public static BytecodeFieldType of(@NotNull Class<?> clazz) {
        final String name = clazz.getName();
        if (name.startsWith("[")) {
            return new BytecodeFieldType(name.replace('.', '/'));
        } else {
            return (new SourceFieldType(name)).toBytecodeFieldType();
        }
    }

    @NotNull
    public static BytecodeFieldType of(@NotNull Field field) {
        return of(field.getType());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int n = string.length();
        for (int i = 0; i < n; i++) {
            switch (string.charAt(i)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    if (i + 1 != n) {
                        consumer.accept(new RuntimeException("excessive content"));
                    }
                    return;
                case 'V':
                    if (i != 0) {
                        consumer.accept(new RuntimeException("void array"));
                    }
                    return;
                case 'L':
                    // if reach here, n > 0, and n - 1 >= 0
                    if (string.charAt(n - 1) == ';') {
                        // if reach here, i < n and string[i] != string[n - 1], so i < n - 1, and i + 1 <= n - 1
                        String message = JavaFullyQualifiedNameValidator.BYTECODE.message(Locale.ENGLISH, string, i + 1, n - 1);
                        if (message != null) {
                            throw new RuntimeException(message);
                        }
                    } else {
                        consumer.accept(new RuntimeException("brackets not paired"));
                    }
                    return;
                case '[':
                    break; // continue loop
                default:
                    consumer.accept(new RuntimeException("reading bad char"));
                    return;
            }
        }
    }

    @Override
    public void checkReturnTypeHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!"V".equals(string)) {
            checkHealth(consumer);
        }
    }

    @Override
    public boolean isArray() {
        return string.startsWith("[");
    }

    @Override
    public boolean isPrimitive() {
        return "BCDFIJSVZ".contains(string); // quick check, if string is healthy; including void
    }

    @Nullable
    @Override
    public BytecodeFieldType componentType() {
        if (string.startsWith("[")) {
            return new BytecodeFieldType(string.substring(1));
        } else {
            return null;
        }
    }

    @Override
    public BytecodeFieldType arrayType() {
        return new BytecodeFieldType("[" + string);
    }

    @Override
    @NotNull
    public String descriptorString() {
        return toBytecodeString();
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
        String result;
        switch (string.charAt(0)) {
            case 'B':
                result = "byte";
                break;
            case 'C':
                result = "char";
                break;
            case 'D':
                result = "double";
                break;
            case 'F':
                result = "float";
                break;
            case 'I':
                result = "int";
                break;
            case 'J':
                result = "long";
                break;
            case 'S':
                result = "short";
                break;
            case 'V':
                result = "void";
                break;
            case 'Z':
                result = "boolean";
                break;
            case 'L':
                if (string.charAt(n - 1) != ';') {
                    throw new RuntimeException("brackets not paired");
                }
                return string.substring(1, n - 1).replace('/', '.');
            case '[':
                return string.replace('/', '.');
            default:
                throw new RuntimeException("reading bad char");
        }
        if (n != 1) {
            throw new RuntimeException("excessive content");
        }
        return result;
    }

    @NotNull
    @Override
    public String toRealSourceString() {
        return toSourceString().replace('$', '.');
    }

    @NotNull
    @Override
    public String toSourceString() {
        final int n = string.length();
        LOOP:
        for (int m = 0; m < n; m++) {
            String result;
            switch (string.charAt(m)) {
                case 'B':
                    result = "byte";
                    break;
                case 'C':
                    result = "char";
                    break;
                case 'D':
                    result = "double";
                    break;
                case 'F':
                    result = "float";
                    break;
                case 'I':
                    result = "int";
                    break;
                case 'J':
                    result = "long";
                    break;
                case 'S':
                    result = "short";
                    break;
                case 'V':
                    if (m != 0) {
                        throw new RuntimeException("void array");
                    }
                    result = "void";
                    break;
                case 'Z':
                    result = "boolean";
                    break;
                case 'L':
                    if (string.charAt(n - 1) != ';') {
                        throw new RuntimeException("brackets not paired");
                    }
                    result = string.substring(m + 1, n - 1).replace('/', '.');
                    break;
                case '[':
                    continue LOOP;
                default:
                    throw new RuntimeException("reading bad char");
            }
            if (m == 0) {
                return result;
            } else {
                return result + "[]".repeat(m);
            }
        }
        throw new RuntimeException("no core content");
    }

    @NotNull
    @Override
    public String toBytecodeString() {
        return string;
    }

    @NotNull
    @Override
    public SourceFieldType toSourceFieldType() {
        return new SourceFieldType(toSourceString());
    }

    @NotNull
    @Override
    public BytecodeFieldType toBytecodeFieldType() {
        return this;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BytecodeFieldType && this.string.equals(((BytecodeFieldType) obj).string);
    }

    @NotNull
    @Override
    public String toString() {
        return "BytecodeFieldType[" + Quote.DEFAULT.apply(string) + "]";
    }
}
