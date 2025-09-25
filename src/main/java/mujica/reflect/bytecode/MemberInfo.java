package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.invoke.BytecodeFieldType;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/19")
public abstract class MemberInfo implements ClassFileNode.Dependent {

    private static final long serialVersionUID = 0xD47608DCBC6CE532L;

    int accessFlags;

    String name; // CONSTANT_UTF8

    String descriptor; // CONSTANT_UTF8

    AttributeInfo[] attributes;

    public MemberInfo() {
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
            return AttributeInfo.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == AttributeInfo.class) {
            return attributes.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        accessFlags = in.readUnsignedShort();
        name = context.getUtf8(in.readUnsignedShort());
        descriptor = context.getUtf8(in.readUnsignedShort());
        attributes = AttributeInfo.readArray(context, in);
        afterRead();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        accessFlags = 0xffff & buffer.getShort();
        name = context.getUtf8(0xffff & buffer.getShort());
        descriptor = context.getUtf8(0xffff & buffer.getShort());
        attributes = AttributeInfo.readArray(context, buffer);
        afterRead();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(accessFlags);
        out.writeShort(context.putUtf8(name));
        out.writeShort(context.putUtf8(descriptor));
        AttributeInfo.writeArray(context, out, attributes);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) accessFlags);
        buffer.putShort((short) context.putUtf8(name));
        buffer.putShort((short) context.putUtf8(descriptor));
        AttributeInfo.writeArray(context, buffer, attributes);
    }

    protected void afterRead() {
        // pass
    }

    void remapConstantPool(@NotNull IntUnaryOperator remap) {
        //
    }

    void floatToDouble() {
        //
    }

    void doubleToFloat() {
        //
    }

    @CodeHistory(date = "2025/9/4")
    public static class FieldInfo extends MemberInfo {

        private static final long serialVersionUID = 0x98925D32E43835A8L;

        transient ConstantValueAttributeInfo constantValue;

        @Override
        protected void afterRead() {
            for (AttributeInfo attribute : attributes) {
                if (attribute instanceof ConstantValueAttributeInfo) {
                    constantValue = (ConstantValueAttributeInfo) attribute;
                }
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            final StringBuilder sb = new StringBuilder();
            if ((accessFlags & Modifier.PUBLIC) != 0) {
                sb.append("public ");
            }
            if ((accessFlags & Modifier.PROTECTED) != 0) {
                sb.append("protected ");
            }
            if ((accessFlags & Modifier.PRIVATE) != 0) {
                sb.append("private ");
            }
            if ((accessFlags & Modifier.STATIC) != 0) {
                sb.append("static ");
            }
            if ((accessFlags & Modifier.FINAL) != 0) {
                sb.append("final ");
            }
            if ((accessFlags & Modifier.VOLATILE) != 0) {
                sb.append("volatile ");
            }
            if ((accessFlags & Modifier.TRANSIENT) != 0) {
                sb.append("transient ");
            }
            if ((accessFlags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            if ((accessFlags & 0x4000) != 0) {
                sb.append("enum ");
            }
            sb.append((new BytecodeFieldType(descriptor)).toSourceString()).append(' ').append(name);
            if (constantValue != null) {
                sb.append(" = ").append(constantValue.constantValueToString(context));
            }
            return sb.append(';').toString();
        }
    }

    @CodeHistory(date = "2025/9/3")
    public static class MethodInfo extends MemberInfo {

        private static final long serialVersionUID = 0xE90ADC507B5F0952L;

        transient MethodParametersAttributeInfo methodParameters;

        transient ExceptionsAttributeInfo exceptions;

        transient CodeAttributeInfo code;

        @Override
        protected void afterRead() {
            for (AttributeInfo attribute : attributes) {
                if (attribute instanceof MethodParametersAttributeInfo) {
                    methodParameters = (MethodParametersAttributeInfo) attribute;
                } else if (attribute instanceof CodeAttributeInfo) {
                    code = (CodeAttributeInfo) attribute;
                } else if (attribute instanceof ExceptionsAttributeInfo) {
                    exceptions = (ExceptionsAttributeInfo) attribute;
                }
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            final StringBuilder sb = new StringBuilder();
            if ((accessFlags & Modifier.PUBLIC) != 0) {
                sb.append("public ");
            }
            if ((accessFlags & Modifier.PROTECTED) != 0) {
                sb.append("protected ");
            }
            if ((accessFlags & Modifier.PRIVATE) != 0) {
                sb.append("private ");
            }
            if ((accessFlags & Modifier.STATIC) != 0) {
                sb.append("static ");
            }
            if ((accessFlags & Modifier.FINAL) != 0) {
                sb.append("final ");
            }
            if ((accessFlags & Modifier.SYNCHRONIZED) != 0) {
                sb.append("synchronized ");
            }
            if ((accessFlags & 0x0040) != 0) { // Modifier.VOLATILE
                sb.append("bridge ");
            }
            if ((accessFlags & 0x0080) != 0) { // Modifier.TRANSIENT
                sb.append("varargs ");
            }
            if ((accessFlags & Modifier.NATIVE) != 0) {
                sb.append("native ");
            }
            if ((accessFlags & Modifier.ABSTRACT) != 0) {
                sb.append("abstract ");
            }
            if ((accessFlags & Modifier.STRICT) != 0) {
                sb.append("strictfp ");
            }
            if ((accessFlags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            sb.append(descriptor).append(" ").append(name);
            if (exceptions != null) {
                sb.append(" throws ");
                exceptions.append(context, sb);
            }
            return sb.append(';').toString();
        }
    }
}
