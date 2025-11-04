package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.io.nest.LimitedDataInput;
import mujica.reflect.basic.BytecodeFieldType;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@CodeHistory(date = "2025/9/19")
public abstract class MemberInfo implements ClassFileNode.Dependent, BiConsumer<AttributeInfo.Statistics, String> {

    private static final long serialVersionUID = 0xD47608DCBC6CE532L;

    int accessFlags;

    @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
    String name;

    @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
    String descriptor;

    AttributeInfo[] attributes;

    transient SignatureAttributeInfo signature;

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
        AttributeInfo.writeArray(attributes, context, out);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) accessFlags);
        buffer.putShort((short) context.putUtf8(name));
        buffer.putShort((short) context.putUtf8(descriptor));
        AttributeInfo.writeArray(attributes, context, buffer);
    }

    protected void afterRead() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof SignatureAttributeInfo) {
                signature = (SignatureAttributeInfo) attribute;
            }
        }
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        for (AttributeInfo attribute : attributes) {
            attribute.accept(statistics, prefix);
        }
    }

    public void filterAttributeInfo(@NotNull Predicate<AttributeInfo> predicate) {
        attributes = AttributeInfo.filterArray(attributes, predicate);
    }

    public void sortAttributeInfo(@NotNull Comparator<AttributeInfo> comparator) {
        Arrays.sort(attributes, comparator);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        for (AttributeInfo attribute : attributes) {
            attribute.remapConstant(remap);
        }
    }

    @CodeHistory(date = "2019/8/14", project = "bone", name = "JavaFieldInfo")
    @CodeHistory(date = "2025/9/4")
    public static class FieldInfo extends MemberInfo {

        private static final long serialVersionUID = 0x98925D32E43835A8L;

        transient ConstantValueAttributeInfo constantValue;

        @Override
        protected void afterRead() {
            for (AttributeInfo attribute : attributes) {
                if (attribute instanceof SignatureAttributeInfo) {
                    signature = (SignatureAttributeInfo) attribute;
                } else if (attribute instanceof ConstantValueAttributeInfo) {
                    constantValue = (ConstantValueAttributeInfo) attribute;
                }
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
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

        @Override
        public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
            prefix += "Field.";
            super.accept(statistics, prefix);
        }
    }

    @CodeHistory(date = "2025/9/3")
    public static class MethodInfo extends MemberInfo implements Consumer<CodeAttributeInfo.Statistics> {

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
                } else if (attribute instanceof SignatureAttributeInfo) {
                    signature = (SignatureAttributeInfo) attribute;
                } else if (attribute instanceof ExceptionsAttributeInfo) {
                    exceptions = (ExceptionsAttributeInfo) attribute;
                }
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
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

        @Override
        public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
            prefix += "Method.";
            super.accept(statistics, prefix);
        }

        @Override
        public void accept(@NotNull CodeAttributeInfo.Statistics statistics) {
            if (code != null) {
                code.accept(statistics);
            }
        }

        public void writeAssemble(@NotNull ClassFile context, @NotNull IndentWriter writer) throws IOException {
            if (code != null) {
                writer.write(toString(context));
                writer.indentIn();
                code.writeAssemble(context, writer);
                writer.indentOut();
                writer.newLine();
            }
        }
    }
}
