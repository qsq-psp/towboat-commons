package indi.um.json.reflect;

import indi.um.util.io.BinarySerializer;
import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ReflectOperations;
import indi.um.json.api.SerializeFrom;
import indi.um.json.io.JsonByteBufWriter;
import indi.um.json.io.JsonStringWriter;
import indi.um.json.io.TerminalScriptStringWriter;
import indi.um.util.ds.WeakIdentityHashSet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2022/6/9.
 */
public class JsonSerializer extends JsonConverter implements BinarySerializer {

    final WeakIdentityHashSet trace = new WeakIdentityHashSet();

    public JsonSerializer() {
        super();
    }

    public boolean add(Object object) {
        return trace.add(object);
    }

    public void remove(Object object) {
        trace.remove(object);
    }

    public void serialize(String key, Object value, JsonConsumer jc, ConversionConfig cc) {
        if (value != null) {
            trace.clear();
            JsonType.serializeObject(key, value, jc, cc, this);
        } else if (!cc.anySerializeConfig(SerializeFrom.NULL, true)) {
            jc.optionalKey(key);
            jc.nullValue();
        }
    }

    public void serialize(Object value, JsonConsumer jc, ConversionConfig cc) {
        serialize(null, value, jc, cc);
    }

    public void serialize(String key, Object value, JsonConsumer jc) {
        if (value != null) {
            trace.clear();
            JsonType.serializeObject(key, value, jc, new ConversionConfig(), this);
        } else {
            jc.optionalKey(key);
            jc.nullValue();
        }
        // do not forget to flush!
        if (jc instanceof JsonByteBufWriter) {
            ((JsonByteBufWriter) jc).flush();
        }
    }

    public void serialize(Object value, JsonConsumer jc) {
        serialize(null, value, jc);
    }

    public String stringify(Object value) {
        final JsonStringWriter stringWriter = new JsonStringWriter();
        serialize(null, value, stringWriter);
        return stringWriter.get();
    }

    public void print(Object value, OutputStream os, Charset charset) throws IOException {
        final JsonStringWriter stringWriter = new JsonStringWriter();
        stringWriter.setIndent(2);
        serialize(null, value, stringWriter);
        final OutputStreamWriter streamWriter = new OutputStreamWriter(os, charset);
        streamWriter.write(stringWriter.get());
        streamWriter.flush();
    }

    public void print(Object value, OutputStream os) throws IOException {
        print(value, os, StandardCharsets.UTF_8);
    }

    public void print(Object value, PrintStream ps) {
        final TerminalScriptStringWriter stringWriter = new TerminalScriptStringWriter();
        stringWriter.setIndent(4);
        serialize(null, value, stringWriter);
        ps.println(stringWriter.get());
    }

    public void print(Object value) {
        print(value, System.out);
    }

    public void serialize(Object value, ByteBuf byteBuf) {
        final JsonByteBufWriter byteBufWriter = new JsonByteBufWriter(byteBuf);
        serialize(null, value, byteBufWriter);
        byteBufWriter.flush();
    }

    public void serialize(Object value, OutputStream os) throws IOException {
        final ByteBuf byteBuf = Unpooled.buffer();
        try {
            serialize(value, byteBuf);
            byteBuf.readBytes(os, byteBuf.readableBytes());
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public int operation() {
        return ReflectOperations.JSON_SERIALIZE;
    }

    public String traceToString() {
        return trace.toString();
    }

    @Override
    public void stringify(StringBuilder sb) {
        super.stringify(sb);
        sb.append(", trace = ").append(trace.size());
    }

    public class WithPadding implements BinarySerializer {

        final String name;

        public WithPadding(String name) {
            super();
            this.name = name;
        }

        @Override
        public void serialize(Object value, ByteBuf byteBuf) {
            final JsonByteBufWriter byteBufWriter = new JsonByteBufWriter(byteBuf);
            byteBufWriter.openJsonp(name);
            JsonSerializer.this.serialize(null, value, byteBufWriter);
            byteBufWriter.closeJsonp();
            byteBufWriter.flush();
        }

        public void serialize(Object value, OutputStream os) throws IOException {
            final ByteBuf byteBuf = Unpooled.buffer();
            try {
                serialize(value, byteBuf);
                byteBuf.readBytes(os, byteBuf.readableBytes());
            } finally {
                byteBuf.release();
            }
        }
    }
}
