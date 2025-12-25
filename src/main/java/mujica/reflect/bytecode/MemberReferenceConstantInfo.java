package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.Name;
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

    private static final long serialVersionUID = 0x91460ff49a925505L;

    @DataType("u8")
    @MagicConstant(intValues = {TAG_FIELDREF, TAG_METHODREF, TAG_INTERFACEMETHODREF})
    final int tag;

    @DataType("u16-{0}")
    @ConstantType(tags = ClassConstantInfo.TAG)
    int classIndex;

    @DataType("u16-{0}")
    @ConstantType(tags = NameAndTypeConstantInfo.TAG)
    int nameAndTypeIndex;

    MemberReferenceConstantInfo(@MagicConstant(intValues = {TAG_FIELDREF, TAG_METHODREF, TAG_INTERFACEMETHODREF}) int tag) {
        super();
        this.tag = tag;
    }

    @Name(value = "field reference", language = "en")
    public static final int TAG_FIELDREF = 9;

    @Name(value = "method reference", language = "en")
    public static final int TAG_METHODREF = 10;

    @Name(value = "interface method reference", language = "en")
    public static final int TAG_INTERFACEMETHODREF = 11;

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
            case TAG_FIELDREF:
                return Field.class;
            case TAG_METHODREF:
            case TAG_INTERFACEMETHODREF:
                return Method.class;
            default: // never
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
            case TAG_FIELDREF:
                tagString = " field-reference ";
                break;
            case TAG_METHODREF:
                tagString = " method-reference ";
                break;
            case TAG_INTERFACEMETHODREF:
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
