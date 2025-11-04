package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/10/2")
@ReferencePage(title = "JVMS12 The NestMembers Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.29")
public class NestMembersAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xDA1D7CEF6E976BCDL;

    @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
    private int[] indexes;

    NestMembersAttributeInfo() {
        super();
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

    public static final String NAME = "NestMembers";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 2 * indexes.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int count = in.readUnsignedShort();
        indexes = new int[count];
        for (int index = 0; index < count; index++) {
            indexes[index] = in.readUnsignedShort();
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int count = 0xffff & buffer.getShort();
        indexes = new int[count];
        for (int index = 0; index < count; index++) {
            indexes[index] = 0xffff & buffer.getShort();
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(indexes.length);
        for (int index : indexes) {
            out.writeShort(index);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) indexes.length);
        for (int index : indexes) {
            buffer.putShort((short) index);
        }
    }

    private void append(@NotNull ClassFile context, @NotNull StringBuilder out) {
        boolean subsequent = false;
        for (int index : indexes) {
            if (subsequent) {
                out.append(", ");
            }
            out.append(context.constantPool.getSourceClassName(index));
            subsequent = true;
        }
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        final StringBuilder sb = new StringBuilder();
        sb.append(NAME).append(" [");
        append(context, sb);
        return sb.append("]").toString();
    }
}
