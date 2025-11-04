package mujica.reflect.bytecode;

import mujica.io.buffer.ByteBufferUtil;
import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2019", project = "bone", name = "JavaConstantUtf8")
@CodeHistory(date = "2025/9/6")
@ReferencePage(title = "JVMS12 The CONSTANT_Utf8_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
class Utf8ConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0xCE9EA2B9C2469887L;

    String string;

    Utf8ConstantInfo() {
        super();
    }

    Utf8ConstantInfo(@NotNull String string) {
        super();
        this.string = string;
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_UTF8;
    }

    protected int section() {
        return 6;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return null;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        string = in.readUTF();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        string = ByteBufferUtil.readMUTF8(buffer);
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeUTF(string);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        ByteBufferUtil.writeMUTF8(string, buffer);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " utf8 " + Quote.JSON.apply(string);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Utf8ConstantInfo && string.equals(((Utf8ConstantInfo) obj).string);
    }
}
