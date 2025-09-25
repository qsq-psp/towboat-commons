package mujica.reflect.invoke;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created on 2025/9/11.
 */
public class SourceMethodType implements NotLoadedMethodType<SourceFieldType, SourceMethodType> {

    @NotNull
    final SourceFieldType returnType;

    @NotNull
    final SourceFieldType[] parameterTypes;

    SourceMethodType(@NotNull SourceFieldType returnType, @NotNull SourceFieldType[] parameterTypes) {
        super();
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    // @NotNull
    public static SourceMethodType parse(@NotNull String expression) {
        return null;
    }

    @NotNull
    public static SourceMethodType of(@NotNull MethodType methodType) {
        final int parameterCount = methodType.parameterCount();
        final SourceFieldType[] parameterTypes = new SourceFieldType[parameterCount];
        for (int index = 0; index < parameterCount; index++) {
            parameterTypes[index] = SourceFieldType.of(methodType.parameterType(index));
        }
        return new SourceMethodType(SourceFieldType.of(methodType.returnType()), parameterTypes);
    }

    @NotNull
    public static SourceMethodType of(@NotNull MethodHandle methodHandle) {
        return of(methodHandle.type());
    }

    @NotNull
    public static SourceMethodType of(@NotNull Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final int parameterCount = parameterTypes.length;
        final SourceFieldType[] newParameterTypes = new SourceFieldType[parameterCount];
        for (int index = 0; index < parameterCount; index++) {
            newParameterTypes[index] = SourceFieldType.of(parameterTypes[index]);
        }
        return new SourceMethodType(SourceFieldType.of(method.getReturnType()), newParameterTypes);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        returnType.checkReturnTypeHealth(consumer);
        for (SourceFieldType parameterType : parameterTypes) {
            parameterType.checkHealth(consumer);
        }
    }

    @Override
    public int parameterCount() {
        return parameterTypes.length;
    }

    @Override
    public SourceFieldType parameterType(int index) {
        return parameterTypes[index];
    }

    @Override
    public SourceFieldType returnType() {
        return returnType;
    }

    @Override
    public SourceFieldType[] parameterArray() {
        return parameterTypes.clone();
    }

    @Override
    public List<SourceFieldType> parameterList() {
        return List.of(parameterTypes); // immutable
    }

    @Override
    public SourceMethodType changeReturnType(SourceFieldType newReturnType) {
        if (newReturnType.equals(returnType)) { // null check included
            return this;
        } else {
            return new SourceMethodType(newReturnType, parameterTypes);
        }
    }

    @Override
    public SourceMethodType changeParameterType(int index, SourceFieldType newParameterType) {
        if (newParameterType.equals(parameterTypes[index])) { // null and index check included
            return this;
        } else {
            SourceFieldType[] newParameterTypes = parameterTypes.clone();
            newParameterTypes[index] = newParameterType;
            return new SourceMethodType(returnType, newParameterTypes);
        }
    }

    @Override
    public SourceMethodType dropParameterTypes(int startIndex, int endIndex) {
        final int parameterCount = parameterTypes.length;
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= parameterCount)) {
            throw new IndexOutOfBoundsException();
        }
        if (startIndex == endIndex) {
            return this;
        }
        final SourceFieldType[] newParameterTypes = new SourceFieldType[parameterCount - endIndex + startIndex];
        System.arraycopy(parameterTypes, 0, newParameterTypes, 0, startIndex);
        System.arraycopy(parameterTypes, endIndex, newParameterTypes, startIndex, parameterCount - endIndex);
        return new SourceMethodType(returnType, newParameterTypes);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public SourceMethodType insertParameterTypes(int position, SourceFieldType... insertedParameterTypes) {
        final int parameterCount = parameterTypes.length;
        if (!(0 <= position && position <= parameterCount)) {
            throw new IndexOutOfBoundsException();
        }
        final int insertedCount = insertedParameterTypes.length;
        if (insertedCount == 0) {
            return this;
        }
        for (int index = 0; index < insertedCount; index++) {
            Objects.requireNonNull(insertedParameterTypes[index]);
        }
        final SourceFieldType[] newParameterTypes = new SourceFieldType[parameterCount + insertedCount];
        System.arraycopy(parameterTypes, 0, newParameterTypes, 0, position);
        System.arraycopy(insertedParameterTypes, 0, newParameterTypes, position, insertedCount);
        System.arraycopy(parameterTypes, position, newParameterTypes, position + insertedCount, parameterCount - position);
        return new SourceMethodType(returnType, newParameterTypes);
    }

    @Override
    public String descriptorString() {
        return null;
    }

    @NotNull
    @Override
    public String toRealSourceString(@NotNull String methodName) {
        final StringBuilder sb = new StringBuilder();
        sb.append(returnType.toRealSourceString()).append(' ').append(methodName).append('(');
        boolean subsequent = false;
        for (SourceFieldType parameterType : parameterTypes) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(parameterType.toRealSourceString());
            subsequent = true;
        }
        return sb.append(')').toString();
    }

    @NotNull
    @Override
    public String toSourceString(@NotNull String methodName) {
        final StringBuilder sb = new StringBuilder();
        sb.append(returnType.toSourceString()).append(' ').append(methodName).append('(');
        boolean subsequent = false;
        for (SourceFieldType parameterType : parameterTypes) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(parameterType.toSourceString());
            subsequent = true;
        }
        return sb.append(')').toString();
    }

    @NotNull
    @Override
    public String toBytecodeString() {
        if (parameterTypes.length == 0) {
            return "()" + returnType.toBytecodeString();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (SourceFieldType parameterType : parameterTypes) {
            sb.append(parameterType.toBytecodeString());
        }
        return sb.append(')').append(returnType.toBytecodeString()).toString();
    }

    @NotNull
    @Override
    public MethodType toMethodType() throws ClassNotFoundException {
        final Class<?> returnTypeClass = Class.forName(returnType.toClassGetName());
        final int parameterCount = parameterTypes.length;
        final Class<?>[] parameterTypeClasses = new Class<?>[parameterCount];
        for (int index = 0; index < parameterCount; index++) {
            parameterTypeClasses[index] = Class.forName(parameterTypes[index].toClassGetName());
        }
        return MethodType.methodType(returnTypeClass, parameterTypeClasses);
    }

    @NotNull
    @Override
    public SourceMethodType toSourceMethodType() {
        return this;
    }

    @NotNull
    @Override
    public BytecodeMethodType toBytecodeMethodType() {
        return null;
    }
}
