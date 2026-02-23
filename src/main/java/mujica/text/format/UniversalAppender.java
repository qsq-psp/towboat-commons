package mujica.text.format;

import io.netty.buffer.ByteBuf;
import mujica.ds.generic.set.CollectionConstant;
import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.IntegralToStringFunction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@CodeHistory(date = "2026/1/27")
public class UniversalAppender {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversalAppender.class);

    @CodeHistory(date = "2026/1/28")
    private static class SpecialMethodType {

        @NotNull
        final Class<?> srcType, dstType;

        final int hash;

        SpecialMethodType(@NotNull Class<?> srcType, @NotNull Class<?> dstType) {
            super();
            this.srcType = srcType;
            this.dstType = dstType;
            this.hash = srcType.hashCode() * 31 + dstType.hashCode();
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SpecialMethodType)) {
                return false;
            }
            final SpecialMethodType that = (SpecialMethodType) obj;
            return this.srcType == that.srcType && this.dstType == that.dstType;
        }

        public String toString() {
            return "(" + srcType.getName() + "," + dstType.getName() + ")";
        }
    }

    @AccessStructure(online = false, local = true)
    @NotNull
    final Map<SpecialMethodType, MethodHandle> methods = new HashMap<>();

    private void invoke(@NotNull Object src, Class<?> srcType, @NotNull Object dst, @NotNull Class<?> dstType) {
        do {
            SpecialMethodType methodKey = new SpecialMethodType(srcType, dstType);
            MethodHandle methodHandle = methods.get(methodKey);
            if (methodHandle != null) {
                try {
                    methodHandle.invoke(src, dst);
                } catch (Throwable e) {
                    LOGGER.warn("invoke {} {}", src, methodKey, e);
                }
                return;
            }
            srcType = srcType.getSuperclass();
        } while (srcType != null);
        LOGGER.warn("invoke {} {}", src, dstType);
    }

    private void invoke(@NotNull Object src, @NotNull Object dst, @NotNull Class<?> dstType) {
        invoke(src, src.getClass(), dst, dstType);
    }

    public void write(@NotNull Object object, @NotNull ByteBuffer out) {
        invoke(object, out, ByteBuf.class);
    }

    public void write(@NotNull Object object, @NotNull ByteBuf out) {
        invoke(object, out, ByteBuf.class);
    }

    public void append(@NotNull Object object, @NotNull StringBuilder out) {
        invoke(object, out, StringBuilder.class);
    }

    public void append(@NotNull Object object, @NotNull StringBuffer out) {
        invoke(object, out, StringBuffer.class);
    }

    public void print(@NotNull Object object, @NotNull PrintStream out) {
        invoke(object, out, PrintStream.class);
    }

    public void addTokens(@NotNull Object object, @NotNull List<Object> out) {
        invoke(object, out, List.class);
    }

    private static void writeObject(@NotNull Object object, @NotNull ByteBuffer out) {
        out.put(object.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void writeObject(@NotNull Object object, @NotNull ByteBuf out) {
        out.writeCharSequence(object.toString(), StandardCharsets.UTF_8);
    }

    private static void appendObject(@NotNull Object object, @NotNull StringBuilder out) {
        out.append(object); // toString() called inside
    }

    private static void appendObject(@NotNull Object object, @NotNull StringBuffer out) {
        out.append(object); // toString() called inside
    }

    private static void printObject(@NotNull Object object, @NotNull PrintStream out) {
        out.print(object); // toString() called inside
    }

    private static void addObjectTokens(@NotNull Object object, @NotNull List<Object> out) {
        out.add(object.toString());
    }

    private void configObject(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "writeObject", MethodType.methodType(void.class, Object.class, ByteBuffer.class))
        );
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "writeObject", MethodType.methodType(void.class, Object.class, ByteBuf.class))
        );
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "appendObject", MethodType.methodType(void.class, Object.class, StringBuilder.class))
        );
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "appendObject", MethodType.methodType(void.class, Object.class, StringBuffer.class))
        );
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "printObject", MethodType.methodType(void.class, Object.class, PrintStream.class))
        );
        methods.put(
                new SpecialMethodType(Object.class, ByteBuffer.class),
                lookup.findStatic(UniversalAppender.class, "addObjectTokens", MethodType.methodType(void.class, Object.class, List.class))
        );
    }

    @AccessStructure(online = true, local = true)
    @NotNull
    final Set<Object> stack = new HashSet<>(); // do not need identity hash set, because hash code method of array type is never override, it is always identity

    private void enter(@NotNull Object container) {
        if (!stack.add(container)) {
            throw new RuntimeException("recursive");
        }
    }

    private void exit(@NotNull Object container) {
        if (!stack.remove(container)) {
            throw new RuntimeException("vacant");
        }
    }

    public void writeArray(@NotNull Object[] array, @NotNull ByteBuffer out) {
        enter(array);
        try {
            out.put((byte) '[');
            int length = array.length;
            if (length != 0) {
                int index = 0;
                while (true) {
                    Object item = array[index];
                    if (item != null) {
                        write(item, out);
                    } else {
                        out.put((byte) '-');
                    }
                    index++;
                    if (index < length) {
                        out.put((byte) ',');
                    } else {
                        break;
                    }
                }
            }
            out.put((byte) ']');
        } finally {
            exit(array);
        }
    }

    public void writeArray(@NotNull Object[] array, @NotNull ByteBuf out) {
        enter(array);
        try {
            out.writeByte('[');
            int length = array.length;
            if (length != 0) {
                int index = 0;
                while (true) {
                    Object item = array[index];
                    if (item != null) {
                        write(item, out);
                    } else {
                        out.writeByte('-');
                    }
                    index++;
                    if (index < length) {
                        out.writeByte(',');
                    } else {
                        break;
                    }
                }
            }
            out.writeByte(']');
        } finally {
            exit(array);
        }
    }

    public void appendArray(@NotNull Object[] array, @NotNull StringBuilder out) {
        final int length = array.length;
        if (length == 0) {
            out.append("[]");
            return;
        }
        enter(array);
        try {
            out.append('[');
            int index = 0;
            while (true) {
                Object item = array[index];
                if (item != null) {
                    append(item, out);
                } else {
                    out.append("null");
                }
                index++;
                if (index < length) {
                    out.append(", ");
                } else {
                    break;
                }
            }
            out.append(']');
        } finally {
            exit(array);
        }
    }

    private void appendArray(@NotNull Object[] array, @NotNull StringBuffer out) {
        final int length = array.length;
        if (length == 0) {
            out.append("[]");
            return;
        }
        enter(array);
        try {
            out.append('[');
            int index = 0;
            while (true) {
                Object item = array[index];
                if (item != null) {
                    append(item, out);
                } else {
                    out.append("null");
                }
                index++;
                if (index < length) {
                    out.append(", ");
                } else {
                    break;
                }
            }
            out.append(']');
        } finally {
            exit(array);
        }
    }

    private void printArray(@NotNull Object[] array, @NotNull PrintStream out) {
        final int length = array.length;
        if (length == 0) {
            out.print("[]");
            return;
        }
        enter(array);
        try {
            out.print("[");
            int index = 0;
            while (true) {
                Object item = array[index];
                if (item != null) {
                    print(item, out);
                } else {
                    out.append("null");
                }
                print(array[index], out);
                index++;
                if (index < length) {
                    out.append(", ");
                } else {
                    break;
                }
            }
            out.print("]");
        } finally {
            exit(array);
        }
    }

    private void addArrayTokens(@NotNull Object[] array, @NotNull List<Object> out) {
        final int length = array.length;
        if (length == 0) {
            out.add("[]");
            return;
        }
        enter(array);
        try {
            out.add("[");
            int index = 0;
            while (true) {
                Object item = array[index];
                if (item != null) {
                    addTokens(item, out);
                } else {
                    out.add(CollectionConstant.EMPTY);
                }
                index++;
                if (index < length) {
                    out.add(", ");
                } else {
                    break;
                }
            }
            out.add("]");
        } finally {
            exit(array);
        }
    }

    private void configArray(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        methods.put(
                new SpecialMethodType(Object[].class, ByteBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "writeArray", MethodType.methodType(void.class, Object[].class, ByteBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(Object[].class, ByteBuf.class),
                lookup.findVirtual(UniversalAppender.class, "writeArray", MethodType.methodType(void.class, Object[].class, ByteBuf.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(Object[].class, StringBuilder.class),
                lookup.findVirtual(UniversalAppender.class, "appendArray", MethodType.methodType(void.class, Object[].class, StringBuilder.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(Object[].class, StringBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "appendArray", MethodType.methodType(void.class, Object[].class, StringBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(Object[].class, PrintStream.class),
                lookup.findVirtual(UniversalAppender.class, "printArray", MethodType.methodType(void.class, Object[].class, PrintStream.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(Object[].class, List.class),
                lookup.findVirtual(UniversalAppender.class, "addArrayTokens", MethodType.methodType(void.class, Object[].class, List.class)).bindTo(this)
        );
    }

    private void writeByteArray(@NotNull byte[] array, @NotNull ByteBuffer out) {
        final int length = array.length;
        out.put((byte) '[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put((byte) ',');
            }
            invoke(array[index], byte.class, out, ByteBuffer.class);
        }
        out.put((byte) ']');
    }

    private void writeByteArray(@NotNull byte[] array, @NotNull ByteBuf out) {
        final int length = array.length;
        out.writeByte('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeByte(',');
            }
            invoke(array[index], byte.class, out, ByteBuf.class);
        }
        out.writeByte(']');
    }

    private void appendByteArray(@NotNull byte[] array, @NotNull StringBuilder out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], byte.class, out, StringBuilder.class);
        }
        out.append(']');
    }

    private void appendByteArray(@NotNull byte[] array, @NotNull StringBuffer out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], byte.class, out, StringBuffer.class);
        }
        out.append(']');
    }

    private void printByteArray(@NotNull byte[] array, @NotNull PrintStream out) {
        final int length = array.length;
        out.print("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], byte.class, out, PrintStream.class);
        }
        out.print("]");
    }

    private void addByteArrayTokens(@NotNull byte[] array, @NotNull List<Object> out) {
        final int length = array.length;
        out.add("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.add(", ");
            }
            invoke(array[index], byte.class, out, List.class);
        }
        out.add("]");
    }

    private void configByteArray(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        methods.put(
                new SpecialMethodType(byte[].class, ByteBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "writeByteArray", MethodType.methodType(void.class, byte[].class, ByteBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(byte[].class, ByteBuf.class),
                lookup.findVirtual(UniversalAppender.class, "writeByteArray", MethodType.methodType(void.class, byte[].class, ByteBuf.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(byte[].class, StringBuilder.class),
                lookup.findVirtual(UniversalAppender.class, "appendByteArray", MethodType.methodType(void.class, byte[].class, StringBuilder.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(byte[].class, StringBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "appendByteArray", MethodType.methodType(void.class, byte[].class, StringBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(byte[].class, PrintStream.class),
                lookup.findVirtual(UniversalAppender.class, "printByteArray", MethodType.methodType(void.class, byte[].class, PrintStream.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(byte[].class, List.class),
                lookup.findVirtual(UniversalAppender.class, "addByteArrayTokens", MethodType.methodType(void.class, byte[].class, List.class)).bindTo(this)
        );
    }

    private void writeIntArray(@NotNull int[] array, @NotNull ByteBuffer out) {
        final int length = array.length;
        out.put((byte) '[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put((byte) ',');
            }
            invoke(array[index], int.class, out, ByteBuffer.class);
        }
        out.put((byte) ']');
    }

    private void writeIntArray(@NotNull int[] array, @NotNull ByteBuf out) {
        final int length = array.length;
        out.writeByte('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeByte(',');
            }
            invoke(array[index], int.class, out, ByteBuf.class);
        }
        out.writeByte(']');
    }

    private void appendIntArray(@NotNull int[] array, @NotNull StringBuilder out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], int.class, out, StringBuilder.class);
        }
        out.append(']');
    }

    private void appendIntArray(@NotNull int[] array, @NotNull StringBuffer out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], int.class, out, StringBuffer.class);
        }
        out.append(']');
    }

    private void printIntArray(@NotNull int[] array, @NotNull PrintStream out) {
        final int length = array.length;
        out.print("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], int.class, out, PrintStream.class);
        }
        out.print("]");
    }

    private void addIntArrayTokens(@NotNull int[] array, @NotNull List<Object> out) {
        final int length = array.length;
        out.add("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.add(", ");
            }
            invoke(array[index], int.class, out, List.class);
        }
        out.add("]");
    }

    private void configIntArray(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        methods.put(
                new SpecialMethodType(int[].class, ByteBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "writeIntArray", MethodType.methodType(void.class, int[].class, ByteBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(int[].class, ByteBuf.class),
                lookup.findVirtual(UniversalAppender.class, "writeIntArray", MethodType.methodType(void.class, int[].class, ByteBuf.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(int[].class, StringBuilder.class),
                lookup.findVirtual(UniversalAppender.class, "appendIntArray", MethodType.methodType(void.class, int[].class, StringBuilder.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(int[].class, StringBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "appendIntArray", MethodType.methodType(void.class, int[].class, StringBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(int[].class, PrintStream.class),
                lookup.findVirtual(UniversalAppender.class, "printIntArray", MethodType.methodType(void.class, int[].class, PrintStream.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(int[].class, List.class),
                lookup.findVirtual(UniversalAppender.class, "addIntArrayTokens", MethodType.methodType(void.class, int[].class, List.class)).bindTo(this)
        );
    }

    private void writeLongArray(@NotNull long[] array, @NotNull ByteBuffer out) {
        final int length = array.length;
        out.put((byte) '[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put((byte) ',');
            }
            invoke(array[index], long.class, out, ByteBuffer.class);
        }
        out.put((byte) ']');
    }

    private void writeLongArray(@NotNull long[] array, @NotNull ByteBuf out) {
        final int length = array.length;
        out.writeByte('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeByte(',');
            }
            invoke(array[index], long.class, out, ByteBuf.class);
        }
        out.writeByte(']');
    }

    private void appendLongArray(@NotNull long[] array, @NotNull StringBuilder out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], long.class, out, StringBuilder.class);
        }
        out.append(']');
    }

    private void appendLongArray(@NotNull long[] array, @NotNull StringBuffer out) {
        final int length = array.length;
        out.append('[');
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], long.class, out, StringBuffer.class);
        }
        out.append(']');
    }

    private void printLongArray(@NotNull long[] array, @NotNull PrintStream out) {
        final int length = array.length;
        out.print("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(", ");
            }
            invoke(array[index], long.class, out, PrintStream.class);
        }
        out.print("]");
    }

    private void addLongArrayTokens(@NotNull long[] array, @NotNull List<Object> out) {
        final int length = array.length;
        out.add("[");
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.add(", ");
            }
            invoke(array[index], long.class, out, List.class);
        }
        out.add("]");
    }

    private void configLongArray(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        methods.put(
                new SpecialMethodType(long[].class, ByteBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "writeLongArray", MethodType.methodType(void.class, long[].class, ByteBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(long[].class, ByteBuf.class),
                lookup.findVirtual(UniversalAppender.class, "writeLongArray", MethodType.methodType(void.class, long[].class, ByteBuf.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(long[].class, StringBuilder.class),
                lookup.findVirtual(UniversalAppender.class, "appendLongArray", MethodType.methodType(void.class, long[].class, StringBuilder.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(long[].class, StringBuffer.class),
                lookup.findVirtual(UniversalAppender.class, "appendLongArray", MethodType.methodType(void.class, long[].class, StringBuffer.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(long[].class, PrintStream.class),
                lookup.findVirtual(UniversalAppender.class, "printLongArray", MethodType.methodType(void.class, long[].class, PrintStream.class)).bindTo(this)
        );
        methods.put(
                new SpecialMethodType(long[].class, List.class),
                lookup.findVirtual(UniversalAppender.class, "addLongArrayTokens", MethodType.methodType(void.class, long[].class, List.class)).bindTo(this)
        );
    }

    private static void appendInt(@NotNull Integer value, @NotNull StringBuilder out) {
        out.append((int) value);
    }

    private static void appendInt(@NotNull Integer value, @NotNull StringBuffer out) {
        out.append((int) value);
    }

    private void configInt(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        MethodHandle methodHandle = lookup.findStatic(UniversalAppender.class, "appendInt", MethodType.methodType(void.class, Integer.class, StringBuilder.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findStatic(UniversalAppender.class, "appendInt", MethodType.methodType(void.class, Integer.class, StringBuffer.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuffer.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuffer.class), methodHandle);
    }

    private static void appendHexInt(@NotNull Integer value, @NotNull StringBuilder out) {
        out.append(Integer.toHexString(value));
    }

    private static void appendHexInt(@NotNull Integer value, @NotNull StringBuffer out) {
        out.append(Integer.toHexString(value));
    }

    private void configHexInt(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        MethodHandle methodHandle = lookup.findStatic(UniversalAppender.class, "appendHexInt", MethodType.methodType(void.class, Integer.class, StringBuilder.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findStatic(UniversalAppender.class, "appendHexInt", MethodType.methodType(void.class, Integer.class, StringBuffer.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuffer.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuffer.class), methodHandle);
    }

    private static void appendPoundHexInt(@NotNull Integer value, @NotNull StringBuilder out) {
        out.append('#').append(Integer.toHexString(value));
    }

    private static void appendPoundHexInt(@NotNull Integer value, @NotNull StringBuffer out) {
        out.append('#').append(Integer.toHexString(value));
    }

    private void configPoundHexInt(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        MethodHandle methodHandle = lookup.findStatic(UniversalAppender.class, "appendPoundHexInt", MethodType.methodType(void.class, Integer.class, StringBuilder.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findStatic(UniversalAppender.class, "appendPoundHexInt", MethodType.methodType(void.class, Integer.class, StringBuffer.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuffer.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuffer.class), methodHandle);
    }

    private static void append0xHexInt(@NotNull Integer value, @NotNull StringBuilder out) {
        out.append("0x").append(Integer.toHexString(value));
    }

    private static void append0xHexInt(@NotNull Integer value, @NotNull StringBuffer out) {
        out.append("0x").append(Integer.toHexString(value));
    }

    private void config0xHexInt(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        MethodHandle methodHandle = lookup.findStatic(UniversalAppender.class, "append0xHexInt", MethodType.methodType(void.class, Integer.class, StringBuilder.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findStatic(UniversalAppender.class, "append0xHexInt", MethodType.methodType(void.class, Integer.class, StringBuffer.class));
        methods.put(new SpecialMethodType(Integer.class, StringBuffer.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuffer.class), methodHandle);
    }

    private void config(@NotNull IntegralToStringFunction function, @NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        MethodHandle methodHandle = lookup.findVirtual(IntegralToStringFunction.class, "append", MethodType.methodType(void.class, int.class, StringBuilder.class)).bindTo(function);
        methods.put(new SpecialMethodType(Integer.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(int.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findVirtual(IntegralToStringFunction.class, "append", MethodType.methodType(void.class, long.class, StringBuilder.class)).bindTo(function);
        methods.put(new SpecialMethodType(Long.class, StringBuilder.class), methodHandle);
        methods.put(new SpecialMethodType(long.class, StringBuilder.class), methodHandle);
        methodHandle = lookup.findVirtual(IntegralToStringFunction.class, "append", MethodType.methodType(void.class, BigInteger.class, StringBuilder.class)).bindTo(function);
        methods.put(new SpecialMethodType(BigInteger.class, StringBuilder.class), methodHandle);
    }

    private void configIntAll(@NotNull MethodHandles.Lookup lookup) throws ReflectiveOperationException {
        configArray(lookup);
        configIntArray(lookup);
        configInt(lookup);
    }

    private void configIntAll() {
        try {
            configIntAll(MethodHandles.lookup());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static UniversalAppender createIntAll() {
        final UniversalAppender appender = new UniversalAppender();
        appender.configIntAll();
        return appender;
    }

    private UniversalAppender() {
        super();
    }

    @Override
    @NotNull
    public String toString() {
        return methods.toString();
    }
}
