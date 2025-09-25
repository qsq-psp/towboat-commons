package mujica.io.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import mujica.ds.of_int.IntSlot;
import mujica.io.function.ObjectCodec;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/7")
public class JdkObjectCodec implements ObjectCodec {

    public static Object copy(Object src) throws Exception {
        if (src == null) {
            return null;
        }
        final ByteBuf buf = Unpooled.buffer();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteBufOutputStream(buf));
            oos.writeObject(src);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(buf));
            return ois.readObject();
        } finally {
            buf.release();
        }
    }

    @Override
    public Object decode(@NotNull byte[] data, @NotNull IntSlot pointer) {
        final int length = data.length;
        final ByteArrayInputStream is = new ByteArrayInputStream(data, pointer.getInt(), length);
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pointer.setInt(length - is.available());
        }
    }

    @Override
    public Object decode(@NotNull ByteBuffer in) {
        return null;
    }

    @Override
    public Object decode(@NotNull ByteBuf in) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(in))) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object decode(@NotNull InputStream is) throws IOException {
        try {
            return (new ObjectInputStream(is)).readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void encode(Object value, @NotNull ByteBuf out) {
        try {
            (new ObjectOutputStream(new ByteBufOutputStream(out))).writeObject(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void encode(Object value, @NotNull OutputStream os) throws IOException {
        (new ObjectOutputStream(os)).writeObject(value);
    }
}
