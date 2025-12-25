package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

/**
 * Created on 2025/10/1.
 */
@CodeHistory(date = "2025/10/1")
@ReferencePage(title = "JVMS12 The BootstrapMethods Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.23")
class BootstrapMethodsAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xef6fb4a1403d9a98L;

    private static class BootstrapMethod implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x228d7825fa59893eL;

        @ConstantType(tags = MemberReferenceConstantInfo.TAG_METHODREF)
        int methodReferenceIndex;

        @ConstantType(tags = {
                IntegerConstantInfo.TAG,
                FloatConstantInfo.TAG,
                LongConstantInfo.TAG,
                DoubleConstantInfo.TAG,
                StringConstantInfo.TAG,
                ConstantPool.CONSTANT_METHODHANDLE,
                ConstantPool.CONSTANT_METHODTYPE,
                ConstantPool.CONSTANT_DYNAMIC
        }) // loadable
        int[] argumentIndexes;

        BootstrapMethod() {
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
                return ConstantReferenceNodeAdapter.class;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            if (group == ConstantReferenceNodeAdapter.class) {
                return argumentIndexes.length;
            } else {
                return 0;
            }
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            if (group == ConstantReferenceNodeAdapter.class) {
                return new ConstantReferenceNodeAdapter(argumentIndexes[nodeIndex]);
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public int byteSize() {
            return 4 + 2 * argumentIndexes.length;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            methodReferenceIndex = in.readUnsignedShort();
            final int argumentCount = in.readUnsignedShort();
            argumentIndexes = new int[argumentCount];
            for (int index = 0; index < argumentCount; index++) {
                argumentIndexes[index] = in.readUnsignedShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            methodReferenceIndex = 0xffff & buffer.getShort();
            final int argumentCount = 0xffff & buffer.getShort();
            argumentIndexes = new int[argumentCount];
            for (int index = 0; index < argumentCount; index++) {
                argumentIndexes[index] = 0xffff & buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(methodReferenceIndex);
            out.writeShort(argumentIndexes.length);
            for (int argumentIndex : argumentIndexes) {
                out.writeShort(argumentIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) methodReferenceIndex);
            buffer.putShort((short) argumentIndexes.length);
            for (int argumentIndex : argumentIndexes) {
                buffer.putShort((short) argumentIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return context.constantPool.getToString(context, methodReferenceIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            methodReferenceIndex = remap.applyAsInt(methodReferenceIndex);
            final int argumentCount = argumentIndexes.length;
            for (int index = 0; index < argumentCount; index++) {
                argumentIndexes[index] = remap.applyAsInt(argumentIndexes[index]);
            }
        }
    }

    private BootstrapMethod[] methods;

    BootstrapMethodsAttributeInfo() {
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
            return BootstrapMethod.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == BootstrapMethod.class) {
            return methods.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == BootstrapMethod.class) {
            return methods[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.CRITICAL;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    public static final String NAME = "BootstrapMethods";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (BootstrapMethod method : methods) {
            size += method.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int methodCount = in.readUnsignedShort();
        methods = new BootstrapMethod[methodCount];
        for (int index = 0; index < methodCount; index++) {
            BootstrapMethod method = new BootstrapMethod();
            method.read(in);
            methods[index] = method;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int methodCount = 0xffff & buffer.getShort();
        methods = new BootstrapMethod[methodCount];
        for (int index = 0; index < methodCount; index++) {
            BootstrapMethod method = new BootstrapMethod();
            method.read(buffer);
            methods[index] = method;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(methods.length);
        for (BootstrapMethod method : methods) {
            method.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) methods.length);
        for (BootstrapMethod method : methods) {
            method.write(buffer);
        }
    }
}
