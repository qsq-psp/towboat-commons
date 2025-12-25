package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.io.nest.LimitedDataInput;
import mujica.io.nest.LimitedInput;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@CodeHistory(date = "2019/8/15", project = "bone", name = "JavaClassFile")
@CodeHistory(date = "2025/9/9")
public class ClassFile implements ClassFileNode.Independent, BiConsumer<AttributeInfo.Statistics, String>, Consumer<CodeAttributeInfo.Statistics> {

    private static final long serialVersionUID = 0xd029789af7932a07L;

    static final int MAGIC = 0xcafebabe;

    @NotNull
    final ConstantPool constantPool = new ConstantPool();

    FieldInfo[] fields;

    MethodInfo[] methods;

    AttributeInfo[] attributes;

    @DataType("u16-{0}")
    @ConstantType(tags = ClassConstantInfo.TAG)
    int thisClass;

    @DataType("u16")
    @ConstantType(tags = ClassConstantInfo.TAG, zero = true)
    int superClass;

    @DataType("u16-{0}")
    @ConstantType(tags = ClassConstantInfo.TAG)
    short[] superInterfaces;

    @DataType("u16")
    int accessFlags;

    @DataType("u16")
    int majorVersion;

    @DataType("u16")
    int minorVersion;

    public ClassFile() {
        super();
    }

    @NotNull
    public ConstantPool getConstantPool() {
        return constantPool;
    }

    @Override
    public int groupCount() {
        return 4;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        switch (groupIndex) {
            case 0:
                return ConstantPool.class;
            case 1:
                return FieldInfo.class;
            case 2:
                return MethodInfo.class;
            case 3:
                return AttributeInfo.class;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == AttributeInfo.class) {
            return attributes.length;
        } else if (group == MethodInfo.class) {
            return methods.length;
        } else if (group == FieldInfo.class) {
            return fields.length;
        } else if (group == ConstantPool.class) {
            return 1;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else if (group == MethodInfo.class) {
            return methods[nodeIndex];
        } else if (group == FieldInfo.class) {
            return fields[nodeIndex];
        } else if (group == ConstantPool.class && nodeIndex == 0) {
            return constantPool;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        in.setBehavior(LimitedInput.Behavior.THROW);
        {
            int magic = in.readInt();
            if (magic != MAGIC) {
                throw new ClassFormatError("magic");
            }
        }
        minorVersion = in.readUnsignedShort();
        majorVersion = in.readUnsignedShort();
        constantPool.read(in);
        accessFlags = in.readUnsignedShort();
        thisClass = in.readUnsignedShort();
        superClass = in.readUnsignedShort();
        {
            int interfaceCount = in.readUnsignedShort();
            superInterfaces = new short[interfaceCount];
            for (int index = 0; index < interfaceCount; index++) {
                superInterfaces[index] = in.readShort();
            }
        }
        {
            int fieldCount = in.readUnsignedShort();
            fields = new FieldInfo[fieldCount];
            for (int index = 0; index < fieldCount; index++) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.read(constantPool, in);
                fields[index] = fieldInfo;
            }
        }
        {
            int methodCount = in.readUnsignedShort();
            methods = new MethodInfo[methodCount];
            for (int index = 0; index < methodCount; index++) {
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.read(constantPool, in);
                methods[index] = methodInfo;
            }
        }
        attributes = AttributeInfo.readArray(constantPool, in);
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        {
            int magic = buffer.getInt();
            if (magic != MAGIC) {
                throw new ClassFormatError("magic");
            }
        }
        minorVersion = 0xffff & buffer.getShort();
        majorVersion = 0xffff & buffer.getShort();
        constantPool.read(buffer);
        accessFlags = 0xffff & buffer.getShort();
        thisClass = 0xffff & buffer.getShort();
        superClass = 0xffff & buffer.getShort();
        {
            int interfaceCount = 0xffff & buffer.getShort();
            superInterfaces = new short[interfaceCount];
            for (int index = 0; index < interfaceCount; index++) {
                superInterfaces[index] = buffer.getShort();
            }
        }
        {
            int fieldCount = 0xffff & buffer.getShort();
            fields = new FieldInfo[fieldCount];
            for (int index = 0; index < fieldCount; index++) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.read(constantPool, buffer);
                fields[index] = fieldInfo;
            }
        }
        {
            int methodCount = 0xffff & buffer.getShort();
            methods = new MethodInfo[methodCount];
            for (int index = 0; index < methodCount; index++) {
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.read(constantPool, buffer);
                methods[index] = methodInfo;
            }
        }
        attributes = AttributeInfo.readArray(constantPool, buffer);
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(MAGIC);
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);
        constantPool.write(out);
        out.writeShort(accessFlags);
        out.writeShort(thisClass);
        out.writeShort(superClass);
        out.writeShort(superInterfaces.length);
        for (short superInterface : superInterfaces) {
            out.writeShort(superInterface);
        }
        out.writeShort(fields.length);
        for (MemberInfo fieldInfo : fields) {
            fieldInfo.write(constantPool, out);
        }
        out.writeShort(methods.length);
        for (MemberInfo methodInfo : methods) {
            methodInfo.write(constantPool, out);
        }
        AttributeInfo.writeArray(attributes, constantPool, out);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(MAGIC);
        buffer.putShort((short) minorVersion);
        buffer.putShort((short) majorVersion);
        constantPool.write(buffer);
        buffer.putShort((short) accessFlags);
        buffer.putShort((short) thisClass);
        buffer.putShort((short) superClass);
        buffer.putShort((short) superInterfaces.length);
        for (short superInterface : superInterfaces) {
            buffer.putShort(superInterface);
        }
        buffer.putShort((short) fields.length);
        for (MemberInfo fieldInfo : fields) {
            fieldInfo.write(constantPool, buffer);
        }
        buffer.putShort((short) methods.length);
        for (MemberInfo methodInfo : methods) {
            methodInfo.write(constantPool, buffer);
        }
        AttributeInfo.writeArray(attributes, constantPool, buffer);
    }

    public void write(@NotNull IndentWriter writer) throws IOException {
        write(this, writer, this);
    }

    @NotNull
    ModuleAttributeInfo getModuleAttributeInfo() {
        ModuleAttributeInfo moduleAttribute = null;
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof ModuleAttributeInfo) {
                if (moduleAttribute == null) {
                    moduleAttribute = (ModuleAttributeInfo) attribute;
                } else {
                    throw new RuntimeException("multiple module attribute info");
                }
            }
        }
        if (moduleAttribute == null) {
            throw new RuntimeException("no module attribute info");
        }
        return moduleAttribute;
    }

    @NotNull
    public String getModuleName() {
        return getModuleAttributeInfo().getModuleName(this);
    }

    public void writeModule(@NotNull IndentWriter writer) throws IOException {
        write(this, writer, getModuleAttributeInfo());
    }

    private static void write(@NotNull ClassFile context, @NotNull IndentWriter writer, @NotNull ClassFileNode node) throws IOException {
        writer.write(node.toString(context));
        boolean firstChild = true;
        final int groupCount = node.groupCount();
        for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
            Class<? extends ClassFileNode> group = node.getGroup(groupIndex);
            int nodeCount = node.nodeCount(group);
            for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
                if (firstChild) {
                    firstChild = false;
                    writer.indentIn();
                }
                writer.newLine();
                write(context, writer, node.getNode(group, nodeIndex));
            }
        }
        if (!firstChild) {
            writer.indentOut();
        }
    }

    public void writeReference(@NotNull IndentWriter writer) throws IOException {
        constantPool.writeReference(this, writer);
    }

    public void writeAssemble(@NotNull IndentWriter writer) throws IOException {
        for (MethodInfo method : methods) {
            method.writeAssemble(this, writer);
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
        sb.append(context.constantPool.getSourceClassName(thisClass)).append(' ');
        if (superClass != 0) {
            String superClassName = context.constantPool.getSourceClassName(superClass);
            if (!superClassName.equals("java.lang.Object")) {
                sb.append("extends ").append(superClassName).append(' ');
            }
        }
        if (superInterfaces.length != 0) {
            sb.append("implements ");
            for (short superInterface : superInterfaces) {
                sb.append(context.constantPool.getSourceClassName(superInterface)).append(", ");
            }
        }
        {
            int last = sb.length() - 1;
            if (last >= 0 && sb.charAt(last) == ' ') {
                sb.deleteCharAt(last);
                last--;
            }
            if (last >= 0 && sb.charAt(last) == ',') {
                sb.deleteCharAt(last);
            }
        }
        return sb.append(';').toString();
    }

    @Override
    @NotNull
    public String toString() {
        return toString(this);
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        for (MemberInfo member : fields) {
            member.accept(statistics, prefix);
        }
        for (MemberInfo member : methods) {
            member.accept(statistics, prefix);
        }
        for (AttributeInfo attribute : attributes) {
            attribute.accept(statistics, prefix);
        }
    }

    public void accept(@NotNull AttributeInfo.Statistics statistics) {
        accept(statistics, "");
    }

    @Override
    public void accept(@NotNull CodeAttributeInfo.Statistics statistics) {
        for (MethodInfo method : methods) {
            method.accept(statistics);
        }
    }

    public void filterAttributeInfo(@NotNull Predicate<AttributeInfo> predicate) {
        for (MemberInfo memberInfo : fields) {
            memberInfo.filterAttributeInfo(predicate);
        }
        for (MemberInfo memberInfo : methods) {
            memberInfo.filterAttributeInfo(predicate);
        }
        attributes = AttributeInfo.filterArray(attributes, predicate);
    }

    public void simplifyAttributeInfo() {
        filterAttributeInfo(AttributeInfo::isNecessary);
    }

    public void sortAttributeInfo(@NotNull Comparator<AttributeInfo> comparator) {
        for (MemberInfo memberInfo : fields) {
            memberInfo.sortAttributeInfo(comparator);
        }
        for (MemberInfo memberInfo : methods) {
            memberInfo.sortAttributeInfo(comparator);
        }
        Arrays.sort(attributes, comparator);
    }

    public void shuffleAttributeInfo() {
        sortAttributeInfo((new RandomContext()).shuffleComparator());
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        thisClass = remap.applyAsInt(thisClass);
        superClass = remap.applyAsInt(superClass);
        AttributeInfo.remapConstant(remap, superInterfaces);
        for (MemberInfo memberInfo : fields) {
            memberInfo.remapConstant(remap);
        }
        for (MemberInfo memberInfo : methods) {
            memberInfo.remapConstant(remap);
        }
        for (AttributeInfo attribute : attributes) {
            attribute.remapConstant(remap);
        }
    }
}
