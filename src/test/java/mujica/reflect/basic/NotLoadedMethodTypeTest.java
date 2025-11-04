package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.invoke.MethodType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2025/9/25")
public class NotLoadedMethodTypeTest {

    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    @CodeHistory(date = "2025/9/12", project = "nettyon", name = "TypeDescriptorString")
    @CodeHistory(date = "2025/11/1")
    @Test
    public void caseDescriptorString() {
        Assert.assertEquals("(J)I", MethodType.methodType(int.class, long.class).descriptorString());
        Assert.assertEquals("(Ljava/lang/String;)[C", MethodType.methodType(char[].class, String.class).descriptorString());
        Assert.assertEquals("([Z)Ljava/lang/Void;", MethodType.methodType(Void.class, boolean[].class).descriptorString());
        Assert.assertEquals("(Ljava/util/Date;)V", MethodType.methodType(void.class, Date.class).descriptorString());
        Assert.assertEquals("([[Ljava/io/Serializable;)[Ljava/util/Random;", MethodType.methodType(Random[].class, Serializable[][].class).descriptorString());
        Assert.assertEquals("(Ljava/lang/Runtime$Version;)Ljava/util/Map$Entry;", MethodType.methodType(Map.Entry.class, Runtime.Version.class).descriptorString());
    }

    private void caseParameterArray(@NotNull Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            BytecodeMethodType bytecodeMethodType = SourceMethodType.of(method).toBytecodeMethodType();
            BytecodeFieldType[] array = bytecodeMethodType.parameterArray();
            int length = bytecodeMethodType.parameterCount();
            Assert.assertEquals(length, array.length);
            for (int index = 0; index < length; index++) {
                Assert.assertEquals(bytecodeMethodType.parameterType(index), array[index]);
            }
        }
    }

    @Test
    public void caseParameterArray() {
        caseParameterArray(Object.class);
        caseParameterArray(Runtime.class);
        caseParameterArray(System.class);
        caseParameterArray(Thread.class);
        caseParameterArray(Arrays.class);
    }

    private void caseParameterList(@NotNull Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            BytecodeMethodType bytecodeMethodType = SourceMethodType.of(method).toBytecodeMethodType();
            List<BytecodeFieldType> list = bytecodeMethodType.parameterList();
            int length = bytecodeMethodType.parameterCount();
            Assert.assertEquals(length, list.size());
            for (int index = 0; index < length; index++) {
                Assert.assertEquals(bytecodeMethodType.parameterType(index), list.get(index));
            }
        }
    }

    @Test
    public void caseParameterList() {
        caseParameterList(String.class);
        caseParameterList(DataInput.class);
        caseParameterList(Float.class);
        caseParameterList(ByteBuffer.class);
        caseParameterList(Date.class);
    }

    @Test
    public void casePrimitiveEmpty() {
        final SourceMethodType sourceMethodType = SourceMethodType.of(MethodType.methodType(short.class));
        final BytecodeMethodType bytecodeMethodType = new BytecodeMethodType("()S");
        sourceMethodType.checkHealth(errorCollector::addError);
        bytecodeMethodType.checkHealth(errorCollector::addError);
        Assert.assertEquals(0, sourceMethodType.parameterCount());
        Assert.assertEquals(0, bytecodeMethodType.parameterCount());
        Assert.assertEquals("short readShort()", sourceMethodType.toSourceString("readShort"));
        Assert.assertEquals("short readShort()", bytecodeMethodType.toSourceString("readShort"));
        Assert.assertEquals("()S", sourceMethodType.toBytecodeString());
        Assert.assertEquals("()S", bytecodeMethodType.toBytecodeString());
        Assert.assertEquals(sourceMethodType, bytecodeMethodType.toSourceMethodType());
        Assert.assertEquals(bytecodeMethodType, sourceMethodType.toBytecodeMethodType());
    }

    @Test
    public void casePrimitivePrimitive() {
        final SourceMethodType sourceMethodType = SourceMethodType.of(MethodType.methodType(double.class, int.class));
        final BytecodeMethodType bytecodeMethodType = new BytecodeMethodType("(I)D");
        sourceMethodType.checkHealth(errorCollector::addError);
        bytecodeMethodType.checkHealth(errorCollector::addError);
        Assert.assertEquals(1, sourceMethodType.parameterCount());
        Assert.assertEquals(1, bytecodeMethodType.parameterCount());
        Assert.assertEquals("double getComponent(int)", sourceMethodType.toRealSourceString("getComponent"));
        Assert.assertEquals("double getComponent(int)", bytecodeMethodType.toRealSourceString("getComponent"));
        Assert.assertEquals("(I)D", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(I)D", bytecodeMethodType.toBytecodeString());
        Assert.assertEquals(sourceMethodType, bytecodeMethodType.toSourceMethodType());
        Assert.assertEquals(bytecodeMethodType, sourceMethodType.toBytecodeMethodType());
    }

    @Test
    public void casePrimitiveObject() {
        final SourceMethodType sourceMethodType = SourceMethodType.of(MethodType.methodType(byte.class, URL.class));
        final BytecodeMethodType bytecodeMethodType = new BytecodeMethodType("(Ljava/net/URL;)B");
        sourceMethodType.checkHealth(errorCollector::addError);
        bytecodeMethodType.checkHealth(errorCollector::addError);
        Assert.assertEquals(1, sourceMethodType.parameterCount());
        Assert.assertEquals(1, bytecodeMethodType.parameterCount());
        Assert.assertEquals("byte simpleTag(java.net.URL)", sourceMethodType.toRealSourceString("simpleTag"));
        Assert.assertEquals("byte simpleTag(java.net.URL)", bytecodeMethodType.toRealSourceString("simpleTag"));
        Assert.assertEquals("(Ljava/net/URL;)B", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(Ljava/net/URL;)B", bytecodeMethodType.toBytecodeString());
        Assert.assertEquals(sourceMethodType, bytecodeMethodType.toSourceMethodType());
        Assert.assertEquals(bytecodeMethodType, sourceMethodType.toBytecodeMethodType());
    }

    @Test
    public void caseSourceChangeReturnType() {
        SourceMethodType sourceMethodType = SourceMethodType.of(MethodType.methodType(InputStream.class));
        Assert.assertEquals("()Ljava/io/InputStream;", sourceMethodType.toBytecodeString());
        Assert.assertEquals("()Z", sourceMethodType.changeReturnType(SourceFieldType.of(boolean.class)).toBytecodeString());
        Assert.assertEquals("()Ljava/io/OutputStream;", sourceMethodType.changeReturnType(SourceFieldType.of(OutputStream.class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(int.class, int.class, int[].class, int.class));
        Assert.assertEquals("(I[II)I", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(I[II)[J", sourceMethodType.changeReturnType(SourceFieldType.of(long[].class)).toBytecodeString());
        Assert.assertEquals("(I[II)Ljava/util/concurrent/ThreadPoolExecutor;", sourceMethodType.changeReturnType(SourceFieldType.of(ThreadPoolExecutor.class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(double[][].class, double.class, double.class));
        Assert.assertEquals("(DD)[[D", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(DD)Ljava/lang/Double;", sourceMethodType.changeReturnType(SourceFieldType.of(Double.class)).toBytecodeString());
        Assert.assertEquals("(DD)Ljava/lang/annotation/Retention;", sourceMethodType.changeReturnType(SourceFieldType.of(Retention.class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(Runnable.class, RetentionPolicy.class));
        Assert.assertEquals("(Ljava/lang/annotation/RetentionPolicy;)Ljava/lang/Runnable;", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(Ljava/lang/annotation/RetentionPolicy;)Ljava/lang/annotation/RetentionPolicy;", sourceMethodType.changeReturnType(SourceFieldType.of(RetentionPolicy.class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(IOException.class, Throwable.class));
        Assert.assertEquals("(Ljava/lang/Throwable;)Ljava/io/IOException;", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(Ljava/lang/Throwable;)Ljava/io/IOException;", sourceMethodType.changeReturnType(SourceFieldType.of(IOException.class)).toBytecodeString());
    }

    @Test
    public void caseSourceExchangeReturnType() {
        SourceMethodType sourceMethodTypeA = SourceMethodType.of(MethodType.methodType(AnnotatedElement.class));
        SourceMethodType sourceMethodTypeB = SourceMethodType.of(MethodType.methodType(Method.class));
        Assert.assertEquals("()Ljava/lang/reflect/Method;", sourceMethodTypeA.changeReturnType(sourceMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("()Ljava/lang/reflect/AnnotatedElement;", sourceMethodTypeB.changeReturnType(sourceMethodTypeA.returnType()).toBytecodeString());
        sourceMethodTypeA = SourceMethodType.of(MethodType.methodType(AnnotatedElement[].class, Object.class));
        sourceMethodTypeB = SourceMethodType.of(MethodType.methodType(Method.class, Object.class, Object.class));
        Assert.assertEquals("(Ljava/lang/Object;)Ljava/lang/reflect/Method;", sourceMethodTypeA.changeReturnType(sourceMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("(Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/reflect/AnnotatedElement;", sourceMethodTypeB.changeReturnType(sourceMethodTypeA.returnType()).toBytecodeString());
        sourceMethodTypeA = SourceMethodType.of(MethodType.methodType(Field.class));
        sourceMethodTypeB = SourceMethodType.of(MethodType.methodType(void.class));
        Assert.assertEquals("()V", sourceMethodTypeA.changeReturnType(sourceMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("()Ljava/lang/reflect/Field;", sourceMethodTypeB.changeReturnType(sourceMethodTypeA.returnType()).toBytecodeString());
        sourceMethodTypeA = SourceMethodType.of(MethodType.methodType(Field.class, int.class, int.class));
        sourceMethodTypeB = SourceMethodType.of(MethodType.methodType(void.class, File.class));
        Assert.assertEquals("(II)V", sourceMethodTypeA.changeReturnType(sourceMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("(Ljava/io/File;)Ljava/lang/reflect/Field;", sourceMethodTypeB.changeReturnType(sourceMethodTypeA.returnType()).toBytecodeString());
    }

    @Test
    public void caseBytecodeChangeReturnType() {
        BytecodeMethodType bytecodeMethodType = new BytecodeMethodType("(IF)V");
        Assert.assertEquals("(IF)C", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(char.class)).toBytecodeString());
        Assert.assertEquals("(IF)Ljava/lang/Character;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(Character.class)).toBytecodeString());
        bytecodeMethodType = new BytecodeMethodType("(BI)J");
        Assert.assertEquals("(BI)V", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(void.class)).toBytecodeString());
        Assert.assertEquals("(BI)Ljava/lang/Throwable;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(Throwable.class)).toBytecodeString());
        bytecodeMethodType = new BytecodeMethodType("(Ljava/lang/Exception;Ljava/lang/Exception;)S");
        Assert.assertEquals("(Ljava/lang/Exception;Ljava/lang/Exception;)I", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(int.class)).toBytecodeString());
        Assert.assertEquals("(Ljava/lang/Exception;Ljava/lang/Exception;)Ljava/io/IOException;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(IOException.class)).toBytecodeString());
        bytecodeMethodType = new BytecodeMethodType("()Ljava/lang/Object;");
        Assert.assertEquals("()Ljava/util/function/Consumer;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(Consumer.class)).toBytecodeString());
        Assert.assertEquals("()Ljava/lang/FunctionalInterface;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(FunctionalInterface.class)).toBytecodeString());
        bytecodeMethodType = new BytecodeMethodType("()[[Ljava/lang/Object;");
        Assert.assertEquals("()Ljava/util/function/Consumer;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(Consumer.class)).toBytecodeString());
        Assert.assertEquals("()Ljava/lang/FunctionalInterface;", bytecodeMethodType.changeReturnType(BytecodeFieldType.of(FunctionalInterface.class)).toBytecodeString());
    }

    @Test
    public void caseBytecodeExchangeReturnType() {
        BytecodeMethodType bytecodeMethodTypeA = new BytecodeMethodType("(IF)V");
        BytecodeMethodType bytecodeMethodTypeB = new BytecodeMethodType("(ID)C");
        Assert.assertEquals("(IF)C", bytecodeMethodTypeA.changeReturnType(bytecodeMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("(ID)V", bytecodeMethodTypeB.changeReturnType(bytecodeMethodTypeA.returnType()).toBytecodeString());
        bytecodeMethodTypeA = new BytecodeMethodType("(JF)Ljava/lang/Character;");
        bytecodeMethodTypeB = new BytecodeMethodType("(CCD)Ljava/io/File;");
        Assert.assertEquals("(JF)Ljava/io/File;", bytecodeMethodTypeA.changeReturnType(bytecodeMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("(CCD)Ljava/lang/Character;", bytecodeMethodTypeB.changeReturnType(bytecodeMethodTypeA.returnType()).toBytecodeString());
        bytecodeMethodTypeA = new BytecodeMethodType("([Ljava/lang/String;)[I");
        bytecodeMethodTypeB = new BytecodeMethodType("()[[I");
        Assert.assertEquals("([Ljava/lang/String;)[[I", bytecodeMethodTypeA.changeReturnType(bytecodeMethodTypeB.returnType()).toBytecodeString());
        Assert.assertEquals("()[I", bytecodeMethodTypeB.changeReturnType(bytecodeMethodTypeA.returnType()).toBytecodeString());
    }

    @Test
    public void caseSourceChangeParameterType() {
        SourceMethodType sourceMethodType = SourceMethodType.of(MethodType.methodType(void.class, InputStream.class));
        Assert.assertEquals("(Ljava/io/InputStream;)V", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(Z)V", sourceMethodType.changeParameterType(0, SourceFieldType.of(boolean.class)).toBytecodeString());
        Assert.assertEquals("([B)V", sourceMethodType.changeParameterType(0, SourceFieldType.of(byte[].class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(float.class, int[].class, int[][].class, int.class, int.class));
        Assert.assertEquals("([I[[III)F", sourceMethodType.toBytecodeString());
        Assert.assertEquals("(Ljava/net/URL;[[III)F", sourceMethodType.changeParameterType(0, SourceFieldType.of(URL.class)).toBytecodeString());
        Assert.assertEquals("([ILjava/net/URL;II)F", sourceMethodType.changeParameterType(1, SourceFieldType.of(URL.class)).toBytecodeString());
        Assert.assertEquals("([I[[ILjava/net/URL;I)F", sourceMethodType.changeParameterType(2, SourceFieldType.of(URL.class)).toBytecodeString());
        Assert.assertEquals("([I[[IILjava/net/URL;)F", sourceMethodType.changeParameterType(3, SourceFieldType.of(URL.class)).toBytecodeString());
        sourceMethodType = SourceMethodType.of(MethodType.methodType(int[].class, Predicate.class, Consumer.class, Function.class, Supplier.class));
        Assert.assertEquals("(Ljava/util/function/Predicate;Ljava/util/function/Consumer;Ljava/util/function/Function;Ljava/util/function/Supplier;)[I", sourceMethodType.toBytecodeString());
        Assert.assertEquals(
                "(JLjava/util/function/Consumer;Ljava/util/function/Function;Ljava/util/function/Supplier;)[I",
                sourceMethodType.changeParameterType(0, SourceFieldType.of(long.class)).toBytecodeString()
        );
        Assert.assertEquals(
                "(Ljava/util/function/Predicate;BLjava/util/function/Function;Ljava/util/function/Supplier;)[I",
                sourceMethodType.changeParameterType(1, SourceFieldType.of(byte.class)).toBytecodeString()
        );
        Assert.assertEquals(
                "(Ljava/util/function/Predicate;Ljava/util/function/Consumer;Ljava/lang/Exception;Ljava/util/function/Supplier;)[I",
                sourceMethodType.changeParameterType(2, SourceFieldType.of(Exception.class)).toBytecodeString()
        );
        Assert.assertEquals(
                "(Ljava/util/function/Predicate;Ljava/util/function/Consumer;Ljava/util/function/Function;[Ljava/lang/Thread;)[I",
                sourceMethodType.changeParameterType(3, SourceFieldType.of(Thread[].class)).toBytecodeString()
        );
    }

    @Test
    public void caseBytecodeChangeParameterType() {
        BytecodeMethodType bytecodeMethodType = new BytecodeMethodType("(II[F)V");
        Assert.assertEquals("(JI[F)V", bytecodeMethodType.changeParameterType(0, BytecodeFieldType.of(long.class)).toBytecodeString());
        Assert.assertEquals("(I[I[F)V", bytecodeMethodType.changeParameterType(1, BytecodeFieldType.of(int[].class)).toBytecodeString());
        Assert.assertEquals("(IIF)V", bytecodeMethodType.changeParameterType(2, BytecodeFieldType.of(float.class)).toBytecodeString());
    }
}
