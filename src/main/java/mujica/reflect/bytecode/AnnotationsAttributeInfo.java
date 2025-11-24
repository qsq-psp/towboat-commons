package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.InterpretAsByte;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/11/1")
abstract class AnnotationsAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x311F63EC02F4B228L;

    @CodeHistory(date = "2025/9/17")
    static abstract class ElementValue implements ClassFileNode.Independent {

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
                    throw new ClassFormatError("tag = " + tag);
            }
        }

        @NotNull
        static ElementValue readNew(@NotNull LimitedDataInput in) throws IOException {
            final ElementValue elementValue = createByTag(in.readUnsignedByte());
            elementValue.read(in);
            return elementValue;
        }

        @NotNull
        static ElementValue readNew(@NotNull ByteBuffer buffer) {
            final ElementValue elementValue = createByTag(0xff & buffer.get());
            elementValue.read(buffer);
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
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        int byteSize() {
            return 1;
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(tag);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) tag);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            // pass
        }
    }

    @CodeHistory(date = "2025/9/21")
    static class SimpleElementValue extends ElementValue {

        private static final long serialVersionUID = 0xB16DD2DFB858DC40L;

        @ConstantType(tags = {
                ConstantPool.CONSTANT_UTF8,
                ConstantPool.CONSTANT_INTEGER,
                ConstantPool.CONSTANT_FLOAT,
                ConstantPool.CONSTANT_LONG,
                ConstantPool.CONSTANT_DOUBLE,
                ConstantPool.CONSTANT_CLASS
        }) // simple value
                int constantPoolIndex;

        SimpleElementValue(int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 3;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            constantPoolIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            constantPoolIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeShort(constantPoolIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.putShort((short) constantPoolIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "simple " + context.constantPool.getToString(context, constantPoolIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            constantPoolIndex = remap.applyAsInt(constantPoolIndex);
        }
    }

    @CodeHistory(date = "2025/9/21")
    static class EnumElementValue extends ElementValue {

        private static final long serialVersionUID = 0xC5B49118935F6241L;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int typeNameIndex;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int constantNameIndex;

        EnumElementValue(int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 5;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            typeNameIndex = in.readUnsignedShort();
            constantNameIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            typeNameIndex = 0xffff & buffer.getShort();
            constantNameIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeShort(typeNameIndex);
            out.writeShort(constantNameIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.putShort((short) typeNameIndex);
            buffer.putShort((short) constantNameIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "enum " + context.constantPool.getUtf8(typeNameIndex) + ' ' + context.constantPool.getUtf8(constantNameIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            typeNameIndex = remap.applyAsInt(typeNameIndex);
            constantNameIndex = remap.applyAsInt(constantNameIndex);
        }
    }

    @CodeHistory(date = "2025/9/19")
    static class AnnotationElementValue extends ElementValue {

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
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return Annotation.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == Annotation.class) {
                return 1;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
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
        public void read(@NotNull LimitedDataInput in) throws IOException {
            annotation.read(in);
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            annotation.read(buffer);
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            annotation.write(out);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            annotation.write(buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "annotation";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            annotation.remapConstant(remap);
        }
    }

    @CodeHistory(date = "2025/9/22")
    static class ArrayElementValue extends ElementValue {

        private ElementValue[] array;

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
        public void read(@NotNull LimitedDataInput in) throws IOException {
            final int length = in.readUnsignedShort();
            array = new ElementValue[length];
            for (int index = 0; index < length; index++) {
                array[index] = readNew(in);
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            final int length = 0xffff & buffer.getShort();
            array = new ElementValue[length];
            for (int index = 0; index < length; index++) {
                array[index] = readNew(buffer);
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeShort(array.length);
            for (ElementValue elementValue : array) {
                elementValue.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.putShort((short) array.length);
            for (ElementValue elementValue : array) {
                elementValue.write(buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "array " + array.length;
        }
    }

    @CodeHistory(date = "2025/9/19")
    static class ElementEntry implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x1E564E6845872B5EL;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
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
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return ElementValue.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == ElementValue.class) {
                return 1;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
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
        public void read(@NotNull LimitedDataInput in) throws IOException {
            nameIndex = in.readUnsignedShort();
            elementValue = ElementValue.readNew(in);
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            nameIndex = 0xffff & buffer.getShort();
            elementValue = ElementValue.readNew(buffer);
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(nameIndex);
            elementValue.write(out);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) nameIndex);
            elementValue.write(buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "name " + context.constantPool.getUtf8(nameIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            nameIndex = remap.applyAsInt(nameIndex);
            elementValue.remapConstant(remap);
        }
    }

    @CodeHistory(date = "2025/9/17")
    static class Annotation implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xAC7A9BE7CCFA7B8FL;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        private int typeIndex;

        private ElementEntry[] entries;

        Annotation() {
            super();
        }

        @Override
        public int groupCount() {
            return 1;
        }

        @NotNull
        @Override
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return ElementEntry.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == ElementEntry.class) {
                return entries.length;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
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
        public void read(@NotNull LimitedDataInput in) throws IOException {
            typeIndex = in.readUnsignedShort();
            final int length = in.readUnsignedShort();
            entries = new ElementEntry[length];
            for (int index = 0; index < length; index++) {
                ElementEntry elementEntry = new ElementEntry();
                elementEntry.read(in);
                entries[index] = elementEntry;
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            typeIndex = 0xffff & buffer.getShort();
            final int length = 0xffff & buffer.getShort();
            entries = new ElementEntry[length];
            for (int index = 0; index < length; index++) {
                ElementEntry elementEntry = new ElementEntry();
                elementEntry.read(buffer);
                entries[index] = elementEntry;
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(typeIndex);
            out.writeShort(entries.length);
            for (ElementEntry elementEntry : entries) {
                elementEntry.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) typeIndex);
            buffer.putShort((short) entries.length);
            for (ElementEntry elementEntry : entries) {
                elementEntry.write(buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return '@' + context.constantPool.getUtf8(typeIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            typeIndex = remap.applyAsInt(typeIndex);
            for (ElementEntry elementEntry : entries) {
                elementEntry.remapConstant(remap);
            }
        }
    }

    @CodeHistory(date = "2025/11/3")
    static class ParameterAnnotation implements ClassFileNode.Independent {

        Annotation[] annotations;

        @Override
        public int groupCount() {
            return 1;
        }

        @NotNull
        @Override
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            if (groupIndex == 0) {
                return Annotation.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == Annotation.class) {
                return annotations.length;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            if (group == Annotation.class) {
                return annotations[nodeIndex];
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        int byteSize() {
            int size = 2;
            for (Annotation annotation : annotations) {
                size += annotation.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            final int length = in.readUnsignedShort();
            annotations = new Annotation[length];
            for (int index = 0; index < length; index++) {
                Annotation annotation = new Annotation();
                annotation.read(in);
                annotations[index] = annotation;
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            final int length = 0xffff & buffer.getShort();
            annotations = new Annotation[length];
            for (int index = 0; index < length; index++) {
                Annotation annotation = new Annotation();
                annotation.read(buffer);
                annotations[index] = annotation;
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(annotations.length);
            for (Annotation annotation : annotations) {
                annotation.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) annotations.length);
            for (Annotation annotation : annotations) {
                annotation.write(buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            for (Annotation annotation : annotations) {
                annotation.remapConstant(remap);
            }
        }
    }

    @CodeHistory(date = "2025/11/3")
    static class TypeAnnotation implements ClassFileNode.Independent {

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {

        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {

        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {

        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {

        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {

        }
    }

    protected final boolean visible;

    protected AnnotationsAttributeInfo(boolean visible) {
        super();
        this.visible = visible;
    }

    @Override
    public boolean isNecessary() {
        return visible;
    }
}
