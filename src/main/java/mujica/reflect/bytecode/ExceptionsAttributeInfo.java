package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/23.
 */
@CodeHistory(date = "2025/9/23")
public class ExceptionsAttributeInfo extends AttributeInfo {

    int[] indexes; // CONSTANT_CLASS

    public static final String NAME = "Exceptions";

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
        super.write(context, out);
        out.writeShort(indexes.length);
        for (int index : indexes) {
            out.writeShort(index);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) indexes.length);
        for (int index : indexes) {
            buffer.putShort((short) index);
        }
    }

    public void append(@NotNull ClassFile context, @NotNull StringBuilder out) {
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
    public String toString(@NotNull ClassFile context, int position) {
        final StringBuilder sb = new StringBuilder();
        sb.append(NAME).append(' ');
        append(context, sb);
        return sb.toString();
    }
}
