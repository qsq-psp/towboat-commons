package mujica.reflect.bytecode;

import mujica.io.stream.IndentWriter;
import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created on 2025/9/9.
 */
public class ClassFile implements ClassFileNode.Independent {

    private static final long serialVersionUID = 0xD029789AF7932A07L;

    static final int MAGIC = 0xcafebabe;

    @NotNull
    final ConstantPool constantPool = new ConstantPool();

    MemberInfo.FieldInfo[] fields;

    MemberInfo.MethodInfo[] methods;

    AttributeInfo[] attributes;

    String thisClass; // CONSTANT_CLASS

    String superClass; // CONSTANT_CLASS

    String[] superInterfaces; // CONSTANT_CLASS

    int accessFlags;

    int majorVersion;

    int minorVersion;

    public ClassFile() {
        super();
    }

    private static final Class<?>[] GROUPS = {
            ConstantPool.class,
            MemberInfo.FieldInfo.class,
            MemberInfo.MethodInfo.class,
            AttributeInfo.class
    };

    @Override
    public int groupCount() {
        return GROUPS.length;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        return GROUPS[groupIndex];
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == AttributeInfo.class) {
            return attributes.length;
        } else if (group == MemberInfo.MethodInfo.class) {
            return methods.length;
        } else if (group == MemberInfo.FieldInfo.class) {
            return fields.length;
        } else if (group == ConstantPool.class) {
            return 1;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else if (group == MemberInfo.MethodInfo.class) {
            return methods[nodeIndex];
        } else if (group == MemberInfo.FieldInfo.class) {
            return fields[nodeIndex];
        } else if (group == ConstantPool.class && nodeIndex == 0) {
            return constantPool;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        {
            int magic = in.readInt();
            if (magic != MAGIC) {
                throw new IOException("magic");
            }
        }
        minorVersion = in.readUnsignedShort();
        majorVersion = in.readUnsignedShort();
        constantPool.read(in);
        accessFlags = in.readUnsignedShort();
        thisClass = constantPool.getClassName(in.readUnsignedShort());
        superClass = constantPool.getClassName(in.readUnsignedShort());
        {
            int interfaceCount = in.readUnsignedShort();
            superInterfaces = new String[interfaceCount];
            for (int index = 0; index < interfaceCount; index++) {
                superInterfaces[index] = constantPool.getClassName(in.readUnsignedShort());
            }
        }
        {
            int fieldCount = in.readUnsignedShort();
            fields = new MemberInfo.FieldInfo[fieldCount];
            for (int index = 0; index < fieldCount; index++) {
                MemberInfo.FieldInfo fieldInfo = new MemberInfo.FieldInfo();
                fieldInfo.read(constantPool, in);
                fields[index] = fieldInfo;
            }
        }
        {
            int methodCount = in.readUnsignedShort();
            methods = new MemberInfo.MethodInfo[methodCount];
            for (int index = 0; index < methodCount; index++) {
                MemberInfo.MethodInfo methodInfo = new MemberInfo.MethodInfo();
                methodInfo.read(constantPool, in);
                methods[index] = methodInfo;
            }
        }
        attributes = AttributeInfo.readArray(constantPool, in);
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(MAGIC);
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);
        constantPool.write(out);
        out.writeShort(accessFlags);
        out.writeShort(constantPool.putClassName(thisClass));
        out.writeShort(constantPool.putClassName(superClass));
        out.writeShort(superInterfaces.length);
        for (String superInterface : superInterfaces) {
            out.writeShort(constantPool.putClassName(superInterface));
        }
        out.writeShort(fields.length);
        for (MemberInfo fieldInfo : fields) {
            fieldInfo.write(constantPool, out);
        }
        out.writeShort(methods.length);
        for (MemberInfo methodInfo : methods) {
            methodInfo.write(constantPool, out);
        }
        AttributeInfo.writeArray(constantPool, out, attributes);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
    }

    public void write(@NotNull IndentWriter writer) throws IOException {
        write(this, writer, this, 0);
    }

    private static void write(@NotNull ClassFile context, @NotNull IndentWriter writer, @NotNull ClassFileNode node, int index) throws IOException {
        writer.write(node.toString(context, index));
        boolean firstChild = true;
        final int groupCount = node.groupCount();
        for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
            Class<?> group = node.getGroup(groupIndex);
            int nodeCount = node.nodeCount(group);
            for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
                if (firstChild) {
                    firstChild = false;
                    writer.indentIn();
                }
                writer.newLine();
                write(context, writer, node.getNode(group, nodeIndex), nodeIndex);
            }
        }
        if (!firstChild) {
            writer.indentOut();
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
        if ((accessFlags & 0x1000) != 0) {
            sb.append("synthetic ");
        }
        if ((accessFlags & 0x8000) != 0) {
            sb.append("module ");
        } else if ((accessFlags & 0x4000) != 0) {
            sb.append("enum ");
        } else if ((accessFlags & Modifier.INTERFACE) != 0) {
            sb.append("interface ");
        } else {
            if ((accessFlags & Modifier.ABSTRACT) != 0) {
                sb.append("abstract ");
            }
            sb.append("class ");
        }
        sb.append(thisClass.replace('/', '.')).append(' ');
        if (!superClass.equals("java/lang/Object")) {
            sb.append("extends ").append(superClass.replace('/', '.')).append(' ');
        }
        if (superInterfaces.length != 0) {
            sb.append("implements ");
            for (String superInterface : superInterfaces) {
                sb.append(superInterface.replace('/', '.')).append(", ");
            }
        }
        int last = sb.length() - 1;
        if (last >= 0 && sb.charAt(last) == ' ') {
            sb.deleteCharAt(last);
            last--;
        }
        if (last >= 0 && sb.charAt(last) == ',') {
            sb.deleteCharAt(last);
        }
        return sb.append(';').toString();
    }

    @Override
    @NotNull
    public String toString() {
        return toString(this, 0);
    }
}
