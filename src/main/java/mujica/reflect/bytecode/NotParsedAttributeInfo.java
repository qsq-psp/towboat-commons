package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/15.
 */
public class NotParsedAttributeInfo extends AttributeInfo {

    @NotNull
    final String name;

    byte[] data;

    public NotParsedAttributeInfo(@NotNull String name) {
        super();
        this.name = name;
    }

    @NotNull
    @Override
    public String attributeName() {
        return name;
    }

    @Override
    public int byteSize() {
        return data.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        data = new byte[(int) in.getRemaining()];
        in.readFully(data);
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        data = new byte[buffer.remaining()];
        buffer.get(data);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.write(data);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.put(data);
    }
}
