package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    public BytecodeFieldType parameterType(int parameterIndex) {
        final int length = string.length();
        int startIndex = 1;
        int endIndex = startIndex;
        while (endIndex < length) {
            switch (string.charAt(endIndex++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    if (parameterIndex-- == 0) {
                        return new BytecodeFieldType(string.substring(startIndex, endIndex));
                    }
                    startIndex = endIndex;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    endIndex = string.indexOf(';', endIndex);
                    if (endIndex == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    endIndex++;
                    if (parameterIndex-- == 0) {
                        return new BytecodeFieldType(string.substring(startIndex, endIndex));
                    }
                    startIndex = endIndex;
                    break;
                case '[':
                    break;
                case ')':
                    throw new IndexOutOfBoundsException();
            }
        }
        throw new RuntimeException("bracket not found");
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
        int start = 1;
        for (int index = start; index < length;) {
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
        int start = 1;
        for (int index = start; index < length;) {
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
    public BytecodeMethodType changeParameterType(int parameterIndex, BytecodeFieldType newParameterType) {
        final int length = string.length();
        int startIndex = 1;
        int endIndex = startIndex;
        while (endIndex < length) {
            switch (string.charAt(endIndex++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    if (parameterIndex-- == 0) {
                        return new BytecodeMethodType(string.substring(0, startIndex) + newParameterType.toBytecodeString() + string.substring(endIndex));
                    }
                    startIndex = endIndex;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    endIndex = string.indexOf(';', endIndex);
                    if (endIndex == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    endIndex++;
                    if (parameterIndex-- == 0) {
                        return new BytecodeMethodType(string.substring(0, startIndex) + newParameterType.toBytecodeString() + string.substring(endIndex));
                    }
                    startIndex = endIndex;
                    break;
                case '[':
                    break;
                case ')':
                    throw new IndexOutOfBoundsException();
            }
        }
        throw new RuntimeException("bracket not found");
    }

    @Override
    public BytecodeMethodType dropParameterTypes(int startParameterIndex, int endParameterIndex) {
        if (startParameterIndex >= endParameterIndex) {
            if (startParameterIndex == endParameterIndex) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        final int length = string.length();
        int stringIndex = 1;
        int parameterIndex = 0;
        int startParameterStringIndex;
        if (startParameterIndex == 0) {
            startParameterStringIndex = stringIndex;
        } else {
            startParameterStringIndex = -1;
        }
        int endParameterStringIndex = -1;
        while (stringIndex < length) {
            switch (string.charAt(stringIndex++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    parameterIndex++;
                    break;
                case 'V':
                    throw new RuntimeException("void parameter");
                case 'L':
                    stringIndex = string.indexOf(';', stringIndex);
                    if (stringIndex == -1) {
                        throw new RuntimeException("brackets not paired");
                    }
                    stringIndex++;
                    break;
                case '[':
                    break;
                case ')':
                    throw new IndexOutOfBoundsException();
            }
            if (parameterIndex == startParameterIndex && startParameterStringIndex == -1) {
                startParameterStringIndex = stringIndex;
            }
            if (parameterIndex == endParameterIndex) {
                endParameterStringIndex = stringIndex;
                break;
            }
        }
        if (startParameterStringIndex != -1 && endParameterStringIndex != -1) {
            return new BytecodeMethodType(string.substring(0, startParameterStringIndex) + string.substring(endParameterStringIndex));
        } else {
            throw new RuntimeException("bracket not found");
        }
    }

    @Override
    public BytecodeMethodType insertParameterTypes(int slotIndex, BytecodeFieldType... insertedParameterTypes) {
        final int length = string.length();
        int stringIndex = 1;
        if (slotIndex-- != 0) {
            LOOP:
            while (stringIndex < length) {
                switch (string.charAt(stringIndex++)) {
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'F':
                    case 'I':
                    case 'J':
                    case 'S':
                    case 'Z':
                        if (slotIndex-- == 0) {
                            break LOOP;
                        }
                        break;
                    case 'V':
                        throw new RuntimeException("void parameter");
                    case 'L':
                        stringIndex = string.indexOf(';', stringIndex);
                        if (stringIndex == -1) {
                            throw new RuntimeException("brackets not paired");
                        }
                        stringIndex++;
                        if (slotIndex-- == 0) {
                            break LOOP;
                        }
                        break;
                    case '[':
                        break;
                    case ')':
                        throw new IndexOutOfBoundsException();
                }
            }
        }
        if (stringIndex < length) {
            StringBuilder sb = new StringBuilder();
            sb.append(string, 0, stringIndex);
            for (BytecodeFieldType insertedParameterType : insertedParameterTypes) {
                sb.append(insertedParameterType.toBytecodeString());
            }
            sb.append(string, stringIndex, length);
            return new BytecodeMethodType(sb.toString());
        } else {
            throw new RuntimeException("bracket not found");
        }
    }

    @Override
    public String descriptorString() {
        return toBytecodeString();
    }

    @NotNull
    @Override
    public String toRealSourceString(@NotNull String methodName) {
        return toSourceMethodType().toRealSourceString(methodName);
    }

    @NotNull
    @Override
    public String toSourceString(@NotNull String methodName) {
        return toSourceMethodType().toSourceString(methodName);
    }

    @NotNull
    @Override
    public String toBytecodeString() {
        return string;
    }

    @NotNull
    @Override
    public MethodType toMethodType() throws ClassNotFoundException {
        return toSourceMethodType().toMethodType();
    }

    @NotNull
    @Override
    public SourceMethodType toSourceMethodType() {
        final SourceFieldType[] parameterTypes = new SourceFieldType[parameterCount()];
        int position = 0;
        final int length = string.length();
        int start = 1;
        int index = start;
        LOOP:
        while (index < length) {
            switch (string.charAt(index++)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    parameterTypes[position++] = (new BytecodeFieldType(string.substring(start, index))).toSourceFieldType();
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
                    parameterTypes[position++] = (new BytecodeFieldType(string.substring(start, index))).toSourceFieldType();
                    start = index;
                    break;
                case '[':
                    break;
                case ')':
                    break LOOP;
            }
        }
        if (index < length) {
            return new SourceMethodType(
                    (new BytecodeFieldType(string.substring(index))).toSourceFieldType(),
                    parameterTypes
            );
        } else {
            throw new RuntimeException("bracket not found");
        }
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
        return obj instanceof BytecodeMethodType && this.string.equals(((BytecodeMethodType) obj).string);
    }

    @NotNull
    @Override
    public String toString() {
        return "BytecodeMethodSignature[" + Quote.DEFAULT.apply(string) + "]";
    }
}
