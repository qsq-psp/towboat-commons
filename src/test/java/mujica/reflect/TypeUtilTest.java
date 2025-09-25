package mujica.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;

import static org.junit.Assert.*;

@CodeHistory(date = "2022/7/22", project = "infrastructure")
@CodeHistory(date = "2022/7/23", project = "Ultramarine")
@CodeHistory(date = "2025/3/6")
public class TypeUtilTest {

    public static class Collection0<A, B, C extends Runnable> extends ArrayList<B> {

        public A a;

        public C c;
    }

    public static class Collection1<D extends Number> extends Collection0<System, Runtime, Thread> {

        public D d;
    }

    public static class Collection2 extends Collection1<Float> {}

    public Collection0<Math, RuntimeException, Thread> collection0;

    @Test
    public void caseCollection() throws ReflectiveOperationException {
        assertEquals(Runtime.class, TypeUtil.searchForClass(Collection1.class, Iterable.class, "T"));
        assertEquals(Runtime.class, TypeUtil.searchForClass(Collection2.class, Iterable.class, "T"));
        assertEquals(RuntimeException.class, TypeUtil.searchForClass(TypeUtilTest.class.getField("collection0").getGenericType(), Iterable.class, "T"));
    }

    public static class Map0<A, B, C extends IOException, D> extends LinkedHashMap<B, A> {

        public C c;

        public D d;
    }

    public static class Map1<K, T> extends Map0<Class<?>, K, EOFException, T> {}

    public static class Map2<T, W> extends Map1<String, T> implements Function<W, T> {

        @Override
        public T apply(W w) {
            return null;
        }
    }

    public Map2<Boolean, ClassLoader> map2;

    @Test
    public void caseMap() throws ReflectiveOperationException {
        assertEquals(String.class, TypeUtil.searchForClass(Map2.class, Map.class, "K"));
        assertEquals(Class.class, TypeUtil.searchForClass(Map2.class, Map.class, "V"));
        assertEquals(EOFException.class, TypeUtil.searchForClass(Map1.class, Map0.class, "C"));
        assertEquals(Boolean.class, TypeUtil.searchForClass(TypeUtilTest.class.getField("map2").getGenericType(), Map0.class, "D"));
    }

    public static class Chain0<A extends Number, B> implements Serializable {

        public A a;

        public B b;
    }

    public static class Chain1<A extends Number, C> extends Chain0<A, Class<?>> {

        public C c;
    }

    public static class Chain2<C extends Comparable<String>> extends Chain1<Integer, C> {}

    public static class Chain3 extends Chain2<String> {}

    public Chain1<Long, Void> chain1;

    @Test
    public void caseChain() throws ReflectiveOperationException {
        assertEquals(Integer.class, TypeUtil.searchForClass(Chain2.class, Chain0.class, "A"));
        assertEquals(Integer.class, TypeUtil.searchForClass(Chain3.class, Chain0.class, "A"));
        assertEquals(Class.class, TypeUtil.searchForClass(Chain1.class, Chain0.class, "B"));
        assertEquals(Class.class, TypeUtil.searchForClass(Chain2.class, Chain0.class, "B"));
        assertEquals(Class.class, TypeUtil.searchForClass(Chain3.class, Chain0.class, "B"));
        assertEquals(Long.class, TypeUtil.searchForClass(TypeUtilTest.class.getField("chain1").getGenericType(), Chain0.class, "A"));
    }

    public interface Tree0<A, B> extends Supplier<A>, Consumer<B> {}

    public interface Tree1<C, D> extends Tree0<C, D> {}

    public interface Tree2<D, E> extends Tree1<ThreadGroup, D>, Iterable<E> {}

    public interface Tree3 extends Tree2<StrictMath, Random> {}

    public static class Tree4 implements Tree3 {

        @Override
        @NotNull
        public Iterator<Random> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public void accept(StrictMath strictMath) {
            // pass
        }

        @Override
        public ThreadGroup get() {
            return null;
        }
    }

    public Tree4 tree4;

    @Test
    public void caseTree() throws ReflectiveOperationException {
        assertEquals(ThreadGroup.class, TypeUtil.searchForClass(Tree2.class, Tree0.class, "A"));
        assertEquals(StrictMath.class, TypeUtil.searchForClass(Tree3.class, Tree0.class, "B"));
        assertEquals(Random.class, TypeUtil.searchForClass(Tree3.class, Tree2.class, "E"));
        assertEquals(ThreadGroup.class, TypeUtil.searchForClass(Tree4.class, Tree1.class, "C"));
        assertEquals(StrictMath.class, TypeUtil.searchForClass(Tree4.class, Tree1.class, "D"));
        assertEquals(Random.class, TypeUtil.searchForClass(Tree4.class, Tree2.class, "E"));
        assertEquals(StrictMath.class, TypeUtil.searchForClass(TypeUtilTest.class.getField("tree4").getGenericType(), Tree0.class, "B"));
    }

    public interface Diamond0<A, B extends Path, C extends OutputStream> extends Function<Supplier<C>, Consumer<B>>, Predicate<A> {}

    public interface Diamond1<A, B extends Path, C extends OutputStream> extends Diamond0<A, B, C>, Cloneable, Serializable {}

    public interface Diamond2<A, B extends Path, C extends OutputStream> extends RandomAccess, AutoCloseable, Diamond0<A, B, C> {}

    public interface Diamond3<A, B extends Path, C extends OutputStream> extends Diamond1<A, B, C>, Diamond2<A, B, C> {}

    public interface Diamond4 extends Diamond3<InetAddress, Path, FilterOutputStream> {}

    public Diamond3<URL, Path, BufferedOutputStream> diamond3;

    @Test
    public void caseDiamond() throws ReflectiveOperationException {
        assertEquals(InetAddress.class, TypeUtil.searchForClass(Diamond4.class, Diamond0.class, "A"));
        assertEquals(Path.class, TypeUtil.searchForClass(Diamond4.class, Diamond2.class, "B"));
        assertEquals(FilterOutputStream.class, TypeUtil.searchForClass(Diamond4.class, Diamond3.class, "C"));
        final Type type = TypeUtilTest.class.getField("diamond3").getGenericType();
        assertEquals(URL.class, TypeUtil.searchForClass(type, Diamond2.class, "A"));
        assertEquals(Path.class, TypeUtil.searchForClass(type, Diamond1.class, "B"));
        assertEquals(BufferedOutputStream.class, TypeUtil.searchForClass(type, Diamond0.class, "C"));
    }
}
