package mujica.json.container;

import mujica.ds.text.sequence.TowboatCharSequence;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2022/6/1", project = "Ultramarine", name = "TypedString")
@CodeHistory(date = "2025/10/11")
public final class FastString extends TowboatCharSequence implements Serializable {

    private static final long serialVersionUID = 0xe6acf0939adaa8c6L;

    @NotNull
    public final String string;

    public FastString(@NotNull String string) {
        super();
        this.string = string;
    }

    transient byte[] array;

    @NotNull
    byte[] getArray() {
        if (array == null) {
            array = string.getBytes(StandardCharsets.US_ASCII);
        }
        return array;
    }

    transient ByteBuffer buffer;

    @NotNull
    ByteBuffer getBuffer() {
        if (buffer == null) {
            buffer = ByteBuffer.wrap(getArray());
        }
        return buffer;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @NotNull
    @Override
    public char[] toCharArray() {
        return string.toCharArray();
    }

    public void write(@NotNull OutputStream os) throws IOException {
        os.write(getArray());
    }

    public void write(@NotNull DataOutput os) throws IOException {
        os.write(getArray());
    }

    public void write(@NotNull FileChannel fc) throws IOException {
        fc.write(getBuffer().position(0));
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof FastString && this.string.equals(((FastString) obj).string));
    }

    @Override
    @NotNull
    public String toString() {
        return string;
    }
}
