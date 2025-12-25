package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/12/6")
@ReferencePage(title = "JVMS12 The RuntimeVisibleTypeAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.20")
@ReferencePage(title = "JVMS12 The RuntimeInvisibleTypeAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.21")
class TypeAnnotationsAttributeInfo extends AnnotationsAttributeInfo {

    /**
     * empty_target, for type in field declaration (0x13);
     * return type of method, or type of newly constructed object (0x14);
     * receiver type of method or constructor (0x15).
     */
    @CodeHistory(date = "2025/12/6")
    static class EmptyTarget extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x10E3141060CE9E8BL;

        @NotNull
        static EmptyTarget createByTag(@DataType("u8") int tag) {
            switch (tag) {
                case 0x00: // type_parameter_target
                case 0x01: // type_parameter_target
                case 0x16: // formal_parameter_target
                    return new TypeOrFormalParameterTarget(tag);
                case 0x10: // supertype_target
                case 0x17: // throws_target
                case 0x42: // catch_target
                    return new ThrowsOrCatchTarget(tag);
                case 0x11: // type_parameter_bound_target
                case 0x12: // type_parameter_bound_target
                    return new TypeParameterBoundTarget(tag);
                case 0x13: // empty_target
                case 0x14: // empty_target
                case 0x15: // empty_target
                    return new EmptyTarget(tag);
                case 0x40: // localvar_target
                case 0x41: // localvar_target
                    return new LocalVariableTarget(tag);
                case 0x43: // offset_target
                case 0x44: // offset_target
                case 0x45: // offset_target
                case 0x46: // offset_target
                    return new OffsetTarget(tag);
                case 0x47: // type_argument_target
                case 0x48: // type_argument_target
                case 0x49: // type_argument_target
                case 0x4a: // type_argument_target
                case 0x4b: // type_argument_target
                    return new TypeArgumentTarget(tag);
                default:
                    throw new ClassFormatError("tag = " + tag);
            }
        }

        @DataType("u8")
        final int tag;

        EmptyTarget(@DataType("u8") int tag) {
            super();
            this.tag = tag;
        }

        int byteSize() {
            return 0;
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
    }

    /**
     * type_parameter_target, for type parameter declaration of generic class or interface (0x00),
     * type parameter declaration of generic method or constructor (0x01).
     * formal_parameter_target, for type in formal parameter declaration of method, constructor,
     * or lambda expression (0x16).
     */
    @CodeHistory(date = "2025/12/6")
    static class TypeOrFormalParameterTarget extends EmptyTarget {

        private static final long serialVersionUID = 0xEC2E5BC104D97905L;

        @DataType("u8")
        int typeParameterIndex;

        TypeOrFormalParameterTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 1;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            typeParameterIndex = in.readUnsignedByte();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            typeParameterIndex = 0xff & buffer.get();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(typeParameterIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) typeParameterIndex);
        }
    }

    /**
     * type_parameter_bound_target, for type in bound of type parameter declaration of generic class or interface
     * (0x11), type in bound of type parameter declaration of generic method or constructor (0x12).
     */
    @CodeHistory(date = "2025/12/7")
    static class TypeParameterBoundTarget extends EmptyTarget {

        private static final long serialVersionUID = 0xE4BB03FBC84FFAE4L;

        @DataType("u8")
        int typeParameterIndex;

        @DataType("u8")
        int boundIndex;

        TypeParameterBoundTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 2;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            typeParameterIndex = in.readUnsignedByte();
            boundIndex = in.readUnsignedByte();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            typeParameterIndex = 0xff & buffer.get();
            boundIndex = 0xff & buffer.get();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(typeParameterIndex);
            out.writeByte(boundIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) typeParameterIndex);
            buffer.put((byte) boundIndex);
        }
    }

    /**
     * throws_target, for type in throws clause of method or constructor (0x17).
     * catch_target, for type in exception parameter declaration (0x42).
     * supertype_target, for type in extends or implements clause of class declaration (including the direct superclass
     * or direct superinterface of an anonymous class declaration),
     * or in extends clause of interface declaration (0x10).
     */
    @CodeHistory(date = "2025/12/7")
    static class ThrowsOrCatchTarget extends EmptyTarget {

        private static final long serialVersionUID = 0xF75B1A63A4231925L;

        @DataType("u16")
        int throwsTypeIndex;

        ThrowsOrCatchTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 2;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            throwsTypeIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            throwsTypeIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(throwsTypeIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) throwsTypeIndex);
        }
    }

    /**
     * the given local variable has a value at indices into the code array in the interval [startPC, startPC + length)
     */
    @CodeHistory(date = "2025/12/8")
    static class LocalVariableItem extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x7A696ACB7E81CC2EL;

        @DataType("u16")
        int startPC;

        @DataType("u16")
        int length;

        @DataType("u16")
        int index;

        LocalVariableItem() {
            super();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            startPC = in.readUnsignedShort();
            length = in.readUnsignedShort();
            index = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            length = 0xffff & buffer.getShort();
            index = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(length);
            out.writeShort(index);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) length);
            buffer.putShort((short) index);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "startPC = " + startPC + ", length = " + length + ", index = " + index;
        }
    }

    /**
     * localvar_target, for type in local variable declaration (0x40); type in resource variable declaration (0x41).
     */
    @CodeHistory(date = "2025/12/8")
    static class LocalVariableTarget extends EmptyTarget {

        private static final long serialVersionUID = 0xF8677B5501402D9CL;

        LocalVariableItem[] table;

        LocalVariableTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 2 + 6 * table.length;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            final int tableLength = in.readUnsignedShort();
            table = new LocalVariableItem[tableLength];
            for (int index = 0; index < tableLength; index++) {
                LocalVariableItem item = new LocalVariableItem();
                item.read(in);
                table[index] = item;
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            final int tableLength = 0xffff & buffer.getShort();
            table = new LocalVariableItem[tableLength];
            for (int index = 0; index < tableLength; index++) {
                LocalVariableItem item = new LocalVariableItem();
                item.read(buffer);
                table[index] = item;
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(table.length);
            for (LocalVariableItem item : table) {
                item.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) table.length);
            for (LocalVariableItem item : table) {
                item.write(buffer);
            }
        }
    }

    /**
     * offset_target, for type in instanceof expression (0x43); type in new expression (0x44);
     * type in method reference expression using ::new (0x45);
     * type in method reference expression using ::Identifier (0x46).
     */
    @CodeHistory(date = "2025/12/9")
    static class OffsetTarget extends EmptyTarget {

        private static final long serialVersionUID = 0x5B6A9759687B65EDL;

        @DataType("u16")
        int offset;

        OffsetTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 2;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            offset = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            offset = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(offset);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) offset);
        }
    }

    /**
     * type_argument_target, for type argument for generic constructor in new expression or explicit constructor
     * invocation statement (0x48); type argument for generic method in method invocation expression (0x49);
     * type argument for generic constructor in method reference expression using ::new (0x4a);
     * type argument for generic method in method reference expression using ::Identifier (0x4b)
     */
    @CodeHistory(date = "2025/12/9")
    static class TypeArgumentTarget extends OffsetTarget {

        private static final long serialVersionUID = 0x3D86E905782DC69CL;

        @DataType("u8")
        int typeArgumentIndex;

        TypeArgumentTarget(@DataType("u8") int tag) {
            super(tag);
        }

        @Override
        int byteSize() {
            return 1;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            super.read(in);
            typeArgumentIndex = in.readUnsignedByte();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            super.read(buffer);
            typeArgumentIndex = 0xff & buffer.get();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeByte(typeArgumentIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.put((byte) typeArgumentIndex);
        }
    }

    @CodeHistory(date = "2025/12/10")
    static class TypePathItem extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        /**
         * 0: Annotation is deeper in an array type
         * 1: Annotation is deeper in a nested type
         * 2: Annotation is on the bound of a wildcard type argument of a parameterized type
         * 3: Annotation is on a type argument of a parameterized type
         */
        @DataType("u8")
        int kind;

        @DataType("u8")
        int argumentIndex;

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            kind = in.readUnsignedByte();
            argumentIndex = in.readUnsignedByte();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            kind = 0xff & buffer.get();
            argumentIndex = 0xff & buffer.get();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(kind);
            out.writeByte(argumentIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) kind);
            buffer.put((byte) argumentIndex);
        }
    }

    @CodeHistory(date = "2025/11/3")
    static class TypeAnnotation implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x0C746CCFE0F6B2D9L;

        EmptyTarget targetInfo;

        TypePathItem[] targetPath;

        @ConstantType(tags = Utf8ConstantInfo.TAG)
        @DataType("u16-{0}")
        int typeIndex;

        ElementEntry[] entries;

        @Override
        public int groupCount() {
            return 3;
        }

        @NotNull
        @Override
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            switch (groupIndex) {
                case 0:
                    return EmptyTarget.class;
                case 1:
                    return TypePathItem.class;
                case 2:
                    return ElementEntry.class;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == EmptyTarget.class) {
                return 1;
            } else if (group == TypePathItem.class) {
                return targetPath.length;
            } else if (group == ElementEntry.class) {
                return entries.length;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            if (group == EmptyTarget.class) {
                if (nodeIndex == 0) {
                    return targetInfo;
                }
            } else if (group == TypePathItem.class) {
                return targetPath[nodeIndex];
            } else if (group == ElementEntry.class) {
                return entries[nodeIndex];
            }
            throw new IndexOutOfBoundsException();
        }

        int byteSize() {
            int size = 6 + targetInfo.byteSize() + 2 * targetPath.length;
            for (ElementEntry entry : entries) {
                size += entry.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            targetInfo = EmptyTarget.createByTag(in.readUnsignedByte());
            targetInfo.read(in);
            int count = in.readUnsignedByte();
            targetPath = new TypePathItem[count];
            for (int index = 0; index < count; index++) {
                TypePathItem item = new TypePathItem();
                item.read(in);
                targetPath[index] = item;
            }
            typeIndex = in.readUnsignedShort();
            count = in.readUnsignedShort();
            entries = new ElementEntry[count];
            for (int index = 0; index < count; index++) {
                ElementEntry entry = new ElementEntry();
                entry.read(in);
                entries[index] = entry;
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            targetInfo = EmptyTarget.createByTag(0xff & buffer.get());
            targetInfo.read(buffer);
            int count = 0xff & buffer.get();
            targetPath = new TypePathItem[count];
            for (int index = 0; index < count; index++) {
                TypePathItem item = new TypePathItem();
                item.read(buffer);
                targetPath[index] = item;
            }
            typeIndex = 0xffff & buffer.getShort();
            count = 0xffff & buffer.getShort();
            entries = new ElementEntry[count];
            for (int index = 0; index < count; index++) {
                ElementEntry entry = new ElementEntry();
                entry.read(buffer);
                entries[index] = entry;
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(targetInfo.tag);
            targetInfo.write(out);
            out.writeByte(targetPath.length);
            for (TypePathItem item : targetPath) {
                item.write(out);
            }
            out.writeShort(typeIndex);
            out.writeShort(entries.length);
            for (ElementEntry entry : entries) {
                entry.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) targetInfo.tag);
            targetInfo.write(buffer);
            buffer.put((byte) targetPath.length);
            for (TypePathItem item : targetPath) {
                item.write(buffer);
            }
            buffer.putShort((short) typeIndex);
            buffer.putShort((short) entries.length);
            for (ElementEntry entry : entries) {
                entry.write(buffer);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "type annotation";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            typeIndex = remap.applyAsInt(typeIndex);
            for (ElementEntry entry : entries) {
                entry.remapConstant(remap);
            }
        }
    }

    private TypeAnnotation[] annotations;

    TypeAnnotationsAttributeInfo(boolean visible) {
        super(visible);
    }

    public static final String VISIBLE_NAME = "RuntimeVisibleTypeAnnotations";

    public static final String INVISIBLE_NAME = "RuntimeInvisibleTypeAnnotations";

    @NotNull
    @Override
    public String attributeName() {
        return visible ? VISIBLE_NAME : INVISIBLE_NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (TypeAnnotation annotation : annotations) {
            size += annotation.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int length = in.readUnsignedShort();
        annotations = new TypeAnnotation[length];
        for (int index = 0; index < length; index++) {
            TypeAnnotation annotation = new TypeAnnotation();
            annotation.read(in);
            annotations[index] = annotation;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort();
        annotations = new TypeAnnotation[length];
        for (int index = 0; index < length; index++) {
            TypeAnnotation annotation = new TypeAnnotation();
            annotation.read(buffer);
            annotations[index] = annotation;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(annotations.length);
        for (TypeAnnotation annotation : annotations) {
            annotation.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) annotations.length);
        for (TypeAnnotation annotation : annotations) {
            annotation.write(buffer);
        }
    }
}
