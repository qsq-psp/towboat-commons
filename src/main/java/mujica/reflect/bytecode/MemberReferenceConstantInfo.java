package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/23")
@ReferencePage(title = "JVMS12 The CONSTANT_Fieldref_info, CONSTANT_Methodref_info, and CONSTANT_InterfaceMethodref_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.2")
class MemberReferenceConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0x91460FF49A925505L;

    @MagicConstant(intValues = {ConstantPool.CONSTANT_FIELDREF, ConstantPool.CONSTANT_METHODREF, ConstantPool.CONSTANT_INTERFACEMETHODREF})
    final int tag;

    @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
    int classIndex;

    @ConstantType(tags = ConstantPool.CONSTANT_NAMEANDTYPE)
    int nameAndTypeIndex;

    MemberReferenceConstantInfo(@MagicConstant(intValues = {ConstantPool.CONSTANT_FIELDREF, ConstantPool.CONSTANT_METHODREF, ConstantPool.CONSTANT_INTERFACEMETHODREF}) int tag) {
        super();
        this.tag = tag;
    }

    @Override
    protected int tag() {
        return tag;
    }

    protected int section() {
        return 2;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        switch (tag) {
            case ConstantPool.CONSTANT_FIELDREF:
                return Field.class;
            case ConstantPool.CONSTANT_METHODREF:
            case ConstantPool.CONSTANT_INTERFACEMETHODREF:
                return Method.class;
            default:
                return Member.class;
        }
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
        out.writeShort(classIndex);
        out.writeShort(nameAndTypeIndex);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putShort((short) classIndex);
        buffer.putShort((short) nameAndTypeIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        String tagString;
        switch (tag) {
            case ConstantPool.CONSTANT_FIELDREF:
                tagString = " field-reference ";
                break;
            case ConstantPool.CONSTANT_METHODREF:
                tagString = " method-reference ";
                break;
            case ConstantPool.CONSTANT_INTERFACEMETHODREF:
                tagString = " interface-method-reference ";
                break;
            default:
                tagString = " ? ";
                break;
        }
        return "#" + newIndex + tagString + context.constantPool.getToString(context, classIndex)
                + " " + context.constantPool.getToString(context, nameAndTypeIndex);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
        context.constantPool.getAndRetain(context, classIndex);
        context.constantPool.getAndRetain(context, nameAndTypeIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        classIndex = remap.applyAsInt(classIndex);
        nameAndTypeIndex = remap.applyAsInt(nameAndTypeIndex);
    }

    @Override
    public int hashCode() {
        return (tag * 0x8f + classIndex) * 0x1033f + nameAndTypeIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MemberReferenceConstantInfo)) {
            return false;
        }
        final MemberReferenceConstantInfo that = (MemberReferenceConstantInfo) obj;
        return this.tag == that.tag && this.classIndex == that.classIndex && this.nameAndTypeIndex == that.nameAndTypeIndex;
    }
}
