package mujica.reflect.invoke;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created on 2025/9/11.
 */
@CodeHistory(date = "2025/9/11")
public class BytecodeMethodType implements NotLoadedMethodType<BytecodeFieldType, BytecodeMethodType> {

    @NotNull
    final String string;

    BytecodeMethodType(@NotNull String string) {
        super();
        this.string = string;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        //
    }

    @Override
    public int parameterCount() {
        int count = 0;
        final int length = string.length();
        for (int index = 1; index < length; index++) {
            switch (string.charAt(index)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    count++;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    count++;
                    index = string.indexOf(';', index);
                    if (index == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    break;
                case '[':
                    break;
                case ')':
                    return count;
            }
        }
        throw new RuntimeException("bracket not found");
    }

    @Override
    public BytecodeFieldType parameterType(int index) {
        return null;
    }

    @Override
    public BytecodeFieldType returnType() {
        final int index = string.lastIndexOf(')');
        if (index == -1) {
            throw new RuntimeException("bracket not found");
        }
        return new BytecodeFieldType(string.substring(index + 1));
    }

    @Override
    public BytecodeFieldType[] parameterArray() {
        final BytecodeFieldType[] array = new BytecodeFieldType[parameterCount()];
        int position = 0;
        final int length = string.length();
        int start = 0;
        for (int index = 1; index < length;) {
            switch (string.charAt(index++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    array[position++] = new BytecodeFieldType(string.substring(start, index));
                    start = index;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    index = string.indexOf(';', index);
                    if (index == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    index++;
                    array[position++] = new BytecodeFieldType(string.substring(start, index));
                    start = index;
                    break;
                case '[':
                    break;
                case ')':
                    return array;
            }
        }
        throw new RuntimeException("bracket not found");
    }

    @Override
    public List<BytecodeFieldType> parameterList() {
        final List<BytecodeFieldType> list = new ArrayList<>();
        final int length = string.length();
        int start = 0;
        for (int index = 1; index < length;) {
            switch (string.charAt(index++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    list.add(new BytecodeFieldType(string.substring(start, index)));
                    start = index;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    index = string.indexOf(';', index);
                    if (index == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    index++;
                    list.add(new BytecodeFieldType(string.substring(start, index)));
                    start = index;
                    break;
                case '[':
                    break;
                case ')':
                    return list;
            }
        }
        throw new RuntimeException("bracket not found");
    }

    @Override
    public BytecodeMethodType changeReturnType(BytecodeFieldType newReturnType) {
        final int index = string.lastIndexOf(')');
        if (index == -1) {
            throw new RuntimeException("bracket not found");
        }
        return new BytecodeMethodType(string.substring(0, index + 1) + newReturnType.toBytecodeString());
    }

    @Override
    public BytecodeMethodType changeParameterType(int index, BytecodeFieldType newParameterType) {
        return null;
    }

    @Override
    public BytecodeMethodType dropParameterTypes(int startIndex, int endIndex) {
        return null;
    }

    @Override
    public BytecodeMethodType insertParameterTypes(int position, BytecodeFieldType... insertedParameterTypes) {
        return null;
    }

    @Override
    public String descriptorString() {
        return null;
    }

    @NotNull
    @Override
    public String toRealSourceString(@NotNull String methodName) {
        return null;
    }

    @NotNull
    @Override
    public String toSourceString(@NotNull String methodName) {
        return null;
    }

    @NotNull
    @Override
    public String toBytecodeString() {
        return string;
    }

    @NotNull
    @Override
    public MethodType toMethodType() {
        return null;
    }

    @NotNull
    @Override
    public SourceMethodType toSourceMethodType() {
        return null;
    }

    @NotNull
    @Override
    public BytecodeMethodType toBytecodeMethodType() {
        return this;
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
        return "BytecodeMethodSignature[" + Quote.DEFAULT.apply(string) + "]";
    }
}
