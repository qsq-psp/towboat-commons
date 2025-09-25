package mujica.reflect.bytecode;

import mujica.ds.generic.list.TruncateList;
import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.InterpretAsByte;
import mujica.reflect.modifier.Name;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created on 2025/9/13.
 */
@CodeHistory(date = "2025/9/13")
public class ConstantPool implements ClassFileNode.Independent {

    private static final long serialVersionUID = 0x3E9BA0CFA68B44ADL;

    public ConstantPool() {
        super();
    }

    /**
     * CONSTANT_INTEGER, CONSTANT_FLOAT, CONSTANT_LONG, CONSTANT_DOUBLE, CONSTANT_STRING
     */
    private interface ConstantValue {

        @NotNull
        String constantValueToString(@NotNull ClassFile context);
    }

    /**
     * CONSTANT_UTF8, CONSTANT_INTEGER, CONSTANT_FLOAT, CONSTANT_LONG, CONSTANT_DOUBLE, CONSTANT_CLASS
     */
    private interface SimpleValue {

        @NotNull
        String simpleValueToString(@NotNull ClassFile context);
    }

    /**
     * Occupy two slots
     */
    private interface Large {}

    /**
     * The base class is a slot
     */
    @CodeHistory(date = "2025/9/13")
    private static class Info implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x80A41766D5EA67C0L;

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            // pass
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            // pass
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            // pass
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            // pass
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " slot";
        }
    }

    private final TruncateList<Info> list = new TruncateList<>();

    private final HashMap<Info, Integer> map = new HashMap<>();

    /**
     * Slot at zero or after long and double included
     */
    public int occupiedSize() {
        return list.size();
    }

    /**
     * Slot at zero or after long and double excluded
     */
    public int usableSize() {
        return map.size();
    }

    private int put(@NotNull Info info) {
        Integer index = map.get(info);
        if (index != null) {
            return index;
        }
        index = list.size();
        list.add(info);
        map.put(info, index);
        return index;
    }

    @InterpretAsByte(unsigned = true)
    public static final int CONSTANT_UTF8 = 1;

    @CodeHistory(date = "2025/9/6")
    private static class Utf8Info extends Info implements SimpleValue {

        private static final long serialVersionUID = 0xCE9EA2B9C2469887L;

        String string;

        Utf8Info() {
            super();
        }

        Utf8Info(@NotNull String string) {
            super();
            this.string = string;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            string = in.readUTF();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            //
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_UTF8);
            out.writeUTF(string);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_UTF8);
            //
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " utf8 " + Quote.JSON.apply(string);
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "utf8 " + Quote.JSON.apply(string);
        }
    }

    @NotNull
    public String getUtf8(int index) {
        return ((Utf8Info) list.get(index)).string;
    }

    public int putUtf8(@NotNull String string) {
        return put(new Utf8Info(string));
    }

    @InterpretAsByte(unsigned = true)
    public static final int CONSTANT_INTEGER = 3;

    @CodeHistory(date = "2025/9/6")
    private static class IntegerInfo extends Info implements ConstantValue, SimpleValue {

        private static final long serialVersionUID = 0x68F480567150BA16L;

        int value;

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            value = in.readInt();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            value = buffer.getInt();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_INTEGER);
            out.writeInt(value);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_INTEGER);
            buffer.putInt(value);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " int " + value;
        }

        @NotNull
        @Override
        public String constantValueToString(@NotNull ClassFile context) {
            return Integer.toString(value);
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "int " + value;
        }
    }

    @InterpretAsByte(unsigned = true)
    public static final int CONSTANT_FLOAT = 4;

    @CodeHistory(date = "2025/9/6")
    private static class FloatInfo extends Info implements ConstantValue, SimpleValue {

        private static final long serialVersionUID = 0xf8c5bd1075c263d0L;

        float value;

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            value = in.readFloat();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            value = buffer.getFloat();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_FLOAT);
            out.writeFloat(value);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_FLOAT);
            buffer.putFloat(value);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " float " + value;
        }

        @NotNull
        @Override
        public String constantValueToString(@NotNull ClassFile context) {
            if (Float.isNaN(value)) {
                return "Float.NaN";
            } else if (value == Float.POSITIVE_INFINITY) {
                return "Float.POSITIVE_INFINITY";
            } else if (value == Float.NEGATIVE_INFINITY) {
                return "Float.NEGATIVE_INFINITY";
            } else {
                return value + "F";
            }
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "float " + value;
        }
    }

    public static final int CONSTANT_LONG = 5;

    @CodeHistory(date = "2025/9/21")
    private static class LongInfo extends Info implements ConstantValue, SimpleValue, Large {

        long value;

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            value = in.readLong();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            value = buffer.getLong();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_LONG);
            out.writeLong(value);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_LONG);
            buffer.putLong(value);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " long " + value;
        }

        @NotNull
        @Override
        public String constantValueToString(@NotNull ClassFile context) {
            return value + "L";
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "long " + value;
        }
    }

    public static final int CONSTANT_DOUBLE = 6;

    @CodeHistory(date = "2025/9/20")
    private static class DoubleInfo extends Info implements ConstantValue, SimpleValue, Large {

        private static final long serialVersionUID = 0xf8c5bd1075c263d0L;

        double value;

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            value = in.readDouble();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            value = buffer.getDouble();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_DOUBLE);
            out.writeDouble(value);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_DOUBLE);
            buffer.putDouble(value);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " double " + value;
        }

        @NotNull
        @Override
        public String constantValueToString(@NotNull ClassFile context) {
            if (Double.isNaN(value)) {
                return "Double.NaN";
            } else if (value == Double.POSITIVE_INFINITY) {
                return "Double.POSITIVE_INFINITY";
            } else if (value == Double.NEGATIVE_INFINITY) {
                return "Double.NEGATIVE_INFINITY";
            } else {
                return value + "D";
            }
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "double " + value;
        }
    }

    public static final int CONSTANT_CLASS = 7;

    @CodeHistory(date = "2025/9/21")
    private static class ClassInfo extends Info implements SimpleValue {

        private static final long serialVersionUID = 0x8F53B690656E7D58L;

        int nameIndex; // CONSTANT_UTF8

        ClassInfo() {
            super();
        }

        ClassInfo(int nameIndex) {
            super();
            this.nameIndex = nameIndex;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            nameIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            nameIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_CLASS);
            out.writeShort(nameIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_CLASS);
            buffer.putShort((short) nameIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " class #" + nameIndex + " " + context.constantPool.getUtf8(nameIndex);
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "class " + context.constantPool.getUtf8(nameIndex);
        }
    }

    @NotNull
    public String getClassName(int index) {
        return getUtf8(((ClassInfo) list.get(index)).nameIndex);
    }

    public int putClassName(@NotNull String className) {
        return put(new ClassInfo(putUtf8(className)));
    }

    @NotNull
    public String getSourceClassName(int index) {
        return getClassName(index).replace('/', '.');
    }

    public static final int CONSTANT_STRING = 8;

    @CodeHistory(date = "2025/9/22")
    private static class StringInfo extends Info implements ConstantValue, SimpleValue {

        private static final long serialVersionUID = 0x6AD502A11703BE44L;

        int dataIndex; // CONSTANT_UTF8

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            dataIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            dataIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_STRING);
            out.writeShort(dataIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_STRING);
            buffer.putShort((short) dataIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " string #" + dataIndex + " " + Quote.JSON.apply(context.constantPool.getUtf8(dataIndex));
        }

        @NotNull
        @Override
        public String constantValueToString(@NotNull ClassFile context) {
            return Quote.JSON.apply(context.constantPool.getUtf8(dataIndex));
        }

        @NotNull
        @Override
        public String simpleValueToString(@NotNull ClassFile context) {
            return "string " + Quote.JSON.apply(context.constantPool.getUtf8(dataIndex));
        }
    }

    @NotNull
    public String constantValueToString(@NotNull ClassFile context, int index) {
        final Info info = list.get(index);
        if (info instanceof ConstantValue) {
            return ((ConstantValue) info).constantValueToString(context);
        } else {
            throw new RuntimeException();
        }
    }

    @NotNull
    public String simpleValueToString(@NotNull ClassFile context, int index) {
        final Info info = list.get(index);
        if (info instanceof SimpleValue) {
            return ((SimpleValue) info).simpleValueToString(context);
        } else {
            throw new RuntimeException();
        }
    }

    @Name(value = "field reference", language = "en")
    public static final int CONSTANT_FIELDREF = 9;

    @Name(value = "method reference", language = "en")
    public static final int CONSTANT_METHODREF = 10;

    @Name(value = "interface method reference", language = "en")
    public static final int CONSTANT_INTERFACEMETHODREF = 11;

    @CodeHistory(date = "2025/9/23")
    private static class MemberReferenceInfo extends Info {

        final int tag;

        int classIndex;

        int nameAndTypeIndex;

        MemberReferenceInfo(int tag) {
            super();
            this.tag = tag;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            classIndex = in.readUnsignedShort();
            nameAndTypeIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            classIndex = 0xffff & buffer.getShort();
            nameAndTypeIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(tag);
            out.writeShort(classIndex);
            out.writeShort(nameAndTypeIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) tag);
            buffer.putShort((short) classIndex);
            buffer.putShort((short) nameAndTypeIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            String tagString;
            switch (tag) {
                case CONSTANT_FIELDREF:
                    tagString = " field-reference ";
                    break;
                case CONSTANT_METHODREF:
                    tagString = " method-reference ";
                    break;
                case CONSTANT_INTERFACEMETHODREF:
                    tagString = " interface-method-reference ";
                    break;
                default:
                    tagString = " ? ";
                    break;
            }
            return "#" + position + tagString + context.constantPool.list.get(classIndex).toString(context, classIndex)
                    + " " + context.constantPool.list.get(nameAndTypeIndex).toString(context, nameAndTypeIndex);
        }
    }

    @Name(value = "name and type", language = "en")
    public static final int CONSTANT_NAMEANDTYPE = 12;

    @CodeHistory(date = "2025/9/21")
    private static class NameAndTypeInfo extends Info {

        int nameIndex; // CONSTANT_UTF8

        int typeIndex; // CONSTANT_UTF8

        NameAndTypeInfo() {
            super();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            nameIndex = in.readUnsignedShort();
            typeIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            nameIndex = 0xffff & buffer.getShort();
            typeIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(CONSTANT_NAMEANDTYPE);
            out.writeShort(nameIndex);
            out.writeShort(typeIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) CONSTANT_NAMEANDTYPE);
            buffer.putShort((short) nameIndex);
            buffer.putShort((short) typeIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " name-and-type #" + nameIndex + " " + context.constantPool.getUtf8(nameIndex)
                    + " #" + typeIndex + " " + context.constantPool.getUtf8(typeIndex);
        }
    }

    @Name(value = "method handle", language = "en")
    public static final int CONSTANT_METHODHANDLE = 15;

    @Name(value = "method type", language = "en")
    public static final int CONSTANT_METHODTYPE = 16;

    public static final int CONSTANT_DYNAMIC = 17;

    @Name(value = "invoke dynamic", language = "en")
    public static final int CONSTANT_INVOKEDYNAMIC = 18;

    public static final int CONSTANT_MODULE = 19;

    public static final int CONSTANT_PACKAGE = 20;

    @CodeHistory(date = "2025/9/23")
    private static class NotParsedInfo extends Info {

        final int tag;

        final byte[] data;

        NotParsedInfo(int tag, int length) {
            super();
            this.tag = tag;
            this.data = new byte[length];
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            in.readFully(data);
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            buffer.get(data);
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(tag);
            out.write(data);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) tag);
            buffer.put(data);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "#" + position + " not-parsed " + tag;
        }
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return Info.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == Info.class) {
            return list.size();
        }  else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == Info.class) {
            return list.get(nodeIndex);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    private static Info createByTag(int tag) {
        switch (tag) {
            case CONSTANT_UTF8:
                return new Utf8Info();
            case CONSTANT_INTEGER:
                return new IntegerInfo();
            case CONSTANT_FLOAT:
                return new FloatInfo();
            case CONSTANT_LONG:
                return new LongInfo();
            case CONSTANT_DOUBLE:
                return new DoubleInfo();
            case CONSTANT_CLASS:
                return new ClassInfo();
            case CONSTANT_STRING:
                return new StringInfo();
            case CONSTANT_FIELDREF:
            case CONSTANT_METHODREF:
            case CONSTANT_INTERFACEMETHODREF:
                return new MemberReferenceInfo(tag);
            case CONSTANT_NAMEANDTYPE:
                return new NameAndTypeInfo();
            case CONSTANT_METHODHANDLE:
                return new NotParsedInfo(tag, 3);
            case CONSTANT_METHODTYPE:
            case CONSTANT_MODULE:
            case CONSTANT_PACKAGE:
                return new NotParsedInfo(tag, 2);
            case CONSTANT_DYNAMIC:
            case CONSTANT_INVOKEDYNAMIC:
                return new NotParsedInfo(tag, 4);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        final int occupiedSize = in.readUnsignedShort();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        list.clear();
        list.add(new Info());
        map.clear();
        for (int index = 1; index < occupiedSize; index++) {
            Info info = createByTag(in.readUnsignedByte());
            info.read(in);
            list.add(info);
            map.put(info, index);
            if (info instanceof Large) {
                list.add(new Info());
                index++;
            }
        }
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        final int occupiedSize = 0xffff & buffer.getShort();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        list.clear();
        list.add(null);
        map.clear();
        for (int index = 1; index < occupiedSize; index++) {
            Info info = createByTag(0xff & buffer.get());
            info.read(buffer);
            list.add(info);
            map.put(info, index);
            if (info instanceof Large) {
                list.add(new Info());
                index++;
            }
        }
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        final int occupiedSize = list.size();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        out.writeShort(occupiedSize);
        for (int index = 1; index < occupiedSize; index++) {
            list.get(index).write(out);
        }
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        final int occupiedSize = list.size();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        buffer.putShort((short) occupiedSize);
        for (int index = 1; index < occupiedSize; index++) {
            list.get(index).write(buffer);
        }
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return "ConstantPool " + occupiedSize() + " / " + usableSize();
    }
}
