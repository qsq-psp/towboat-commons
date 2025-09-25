package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.InterpretAsByte;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/17")
public class RuntimeVisibleAnnotationsAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x2841B67A2A148719L;

    @CodeHistory(date = "2025/9/17")
    private static abstract class ElementValue implements Dependent {

        private static final long serialVersionUID = 0x3B7FF66278629B3CL;

        @NotNull
        static ElementValue createByTag(int tag) {
            switch (tag) {
                case 'B': // CONSTANT_INTEGER byte
                case 'C': // CONSTANT_INTEGER char
                case 'D': // CONSTANT_DOUBLE
                case 'F': // CONSTANT_FLOAT
                case 'I': // CONSTANT_INTEGER int
                case 'J': // CONSTANT_LONG
                case 'S': // CONSTANT_INTEGER short
                case 'Z': // CONSTANT_INTEGER boolean
                case 's': // CONSTANT_UTF8 string
                case 'c': // CONSTANT_CLASS class
                    return new SimpleElementValue(tag);
                case 'e':
                    return new EnumElementValue(tag);
                case '@':
                    return new AnnotationElementValue(tag);
                case '[':
                    return new ArrayElementValue(tag);
                default:
                    throw new BytecodeException("RuntimeVisibleAnnotations ElementValue tag = " + tag);
            }
        }

        @NotNull
        static ElementValue readNew(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            final ElementValue elementValue = createByTag(in.readUnsignedByte());
            elementValue.read(context, in);
            return elementValue;
        }

        @NotNull
        static ElementValue readNew(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            final ElementValue elementValue = createByTag(0xff & buffer.get());
            elementValue.read(context, buffer);
            return elementValue;
        }

        @InterpretAsByte(unsigned = true)
        final int tag;

        ElementValue(int tag) {
            super();
            this.tag = tag;
        }

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

        int byteSize() {
            return 1;
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeByte(tag);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.put((byte) tag);
        }
    }

    @CodeHistory(date = "2025/9/21")
    private static class SimpleElementValue extends ElementValue {

        private static final long serialVersionUID = 0xB16DD2DFB858DC40L;

        int constantPoolIndex; // CONSTANT_UTF8, CONSTANT_INTEGER, CONSTANT_FLOAT, CONSTANT_LONG, CONSTANT_DOUBLE, CONSTANT_CLASS

        SimpleElementValue(int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 3;
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            constantPoolIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            constantPoolIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            super.write(context, out);
            out.writeShort(constantPoolIndex);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            super.write(context, buffer);
            buffer.putShort((short) constantPoolIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "simple " + context.constantPool.simpleValueToString(context, constantPoolIndex);
        }
    }

    @CodeHistory(date = "2025/9/21")
    private static class EnumElementValue extends ElementValue {

        private static final long serialVersionUID = 0xC5B49118935F6241L;

        int typeNameIndex; // CONSTANT_UTF8

        int constantNameIndex; // CONSTANT_UTF8

        EnumElementValue(int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 5;
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            typeNameIndex = in.readUnsignedShort();
            constantNameIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            typeNameIndex = 0xffff & buffer.getShort();
            constantNameIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            super.write(context, out);
            out.writeShort(typeNameIndex);
            out.writeShort(constantNameIndex);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            super.write(context, buffer);
            buffer.putShort((short) typeNameIndex);
            buffer.putShort((short) constantNameIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "enum " + context.constantPool.getUtf8(typeNameIndex) + ' ' + context.constantPool.getUtf8(constantNameIndex);
        }
    }

    @CodeHistory(date = "2025/9/19")
    private static class AnnotationElementValue extends ElementValue {

        private static final long serialVersionUID = 0x33189945B5EA768CL;

        @NotNull
        final Annotation annotation = new Annotation();

        AnnotationElementValue(int tag) {
            super(tag);
        }

        @Override
        public int groupCount() {
            return 1;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return Annotation.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            if (group == Annotation.class) {
                return 1;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            if (group == Annotation.class) {
                return annotation;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        int byteSize() {
            return 1 + annotation.byteSize();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            annotation.read(context, in);
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            annotation.read(context, buffer);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            super.write(context, out);
            annotation.write(context, out);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            super.write(context, buffer);
            annotation.write(context, buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "annotation";
        }
    }

    @CodeHistory(date = "2025/9/22")
    private static class ArrayElementValue extends ElementValue {

        ElementValue[] array;

        ArrayElementValue(int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            int size = 3;
            for (ElementValue elementValue : array) {
                size += elementValue.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            final int length = in.readUnsignedShort();
            array = new ElementValue[length];
            for (int index = 0; index < length; index++) {
                array[index] = readNew(context, in);
            }
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            final int length = 0xffff & buffer.getShort();
            array = new ElementValue[length];
            for (int index = 0; index < length; index++) {
                array[index] = readNew(context, buffer);
            }
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            super.write(context, out);
            out.writeShort(array.length);
            for (ElementValue elementValue : array) {
                elementValue.write(context, out);
            }
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            super.write(context, buffer);
            buffer.putShort((short) array.length);
            for (ElementValue elementValue : array) {
                elementValue.write(context, buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "array " + array.length;
        }
    }

    @CodeHistory(date = "2025/9/19")
    private static class ElementEntry implements Dependent {

        private static final long serialVersionUID = 0x1E564E6845872B5EL;

        int nameIndex;

        ElementValue elementValue;

        ElementEntry() {
            super();
        }

        @Override
        public int groupCount() {
            return 1;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return ElementValue.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            if (group == ElementValue.class) {
                return 1;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            if (group == ElementValue.class && nodeIndex == 0) {
                return elementValue;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        int byteSize() {
            return 2 + elementValue.byteSize();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            nameIndex = in.readUnsignedShort();
            elementValue = ElementValue.readNew(context, in);
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            nameIndex = 0xffff & buffer.getShort();
            elementValue = ElementValue.readNew(context, buffer);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeShort(nameIndex);
            elementValue.write(context, out);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.putShort((short) nameIndex);
            elementValue.write(context, buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "name " + context.constantPool.getUtf8(nameIndex);
        }
    }

    @CodeHistory(date = "2025/9/17")
    private static class Annotation implements Dependent {

        private static final long serialVersionUID = 0xAC7A9BE7CCFA7B8FL;

        int typeIndex; // CONSTANT_UTF8

        ElementEntry[] entries;

        Annotation() {
            super();
        }

        @Override
        public int groupCount() {
            return 1;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return ElementEntry.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            if (group == ElementEntry.class) {
                return entries.length;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            if (group == ElementEntry.class) {
                return entries[nodeIndex];
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        int byteSize() {
            int size = 4;
            for (ElementEntry elementEntry : entries) {
                size += elementEntry.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            typeIndex = in.readUnsignedShort();
            final int length = in.readUnsignedShort();
            entries = new ElementEntry[length];
            for (int index = 0; index < length; index++) {
                ElementEntry elementEntry = new ElementEntry();
                elementEntry.read(context, in);
                entries[index] = elementEntry;
            }
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            typeIndex = 0xffff & buffer.getShort();
            final int length = 0xffff & buffer.getShort();
            entries = new ElementEntry[length];
            for (int index = 0; index < length; index++) {
                ElementEntry elementEntry = new ElementEntry();
                elementEntry.read(context, buffer);
                entries[index] = elementEntry;
            }
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeShort(typeIndex);
            out.writeShort(entries.length);
            for (ElementEntry elementEntry : entries) {
                elementEntry.write(context, out);
            }
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.putShort((short) typeIndex);
            buffer.putShort((short) entries.length);
            for (ElementEntry elementEntry : entries) {
                elementEntry.write(context, buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return '@' + context.constantPool.getUtf8(typeIndex);
        }
    }

    Annotation[] annotations;

    RuntimeVisibleAnnotationsAttributeInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return Annotation.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == Annotation.class) {
            return annotations.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == Annotation.class) {
            return annotations[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final String NAME = "RuntimeVisibleAnnotations";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (Annotation annotation : annotations) {
            size += annotation.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int length = in.readUnsignedShort();
        annotations = new Annotation[length];
        for (int index = 0; index < length; index++) {
            Annotation annotation = new Annotation();
            annotation.read(context, in);
            annotations[index] = annotation;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort();
        annotations = new Annotation[length];
        for (int index = 0; index < length; index++) {
            Annotation annotation = new Annotation();
            annotation.read(context, buffer);
            annotations[index] = annotation;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(annotations.length);
        for (Annotation annotation : annotations) {
            annotation.write(context, out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) annotations.length);
        for (Annotation annotation : annotations) {
            annotation.write(context, buffer);
        }
    }
}
