package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ParseHint;
import indi.qsq.json.entity.JsonConstant;
import indi.qsq.util.text.Quote;
import indi.qsq.util.ds.ByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2022/8/8.
 */
class JsonByteArrayType extends JsonType {

    private static final long serialVersionUID = 0x1133EEA24D440DFBL;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonByteArrayType.class);

    enum Form {

        PRIMITIVE_ARRAY(byte[].class),
        BOXED_ARRAY(Byte[].class),
        NIO_BYTE_BUFFER(ByteBuffer.class),
        NETTY_BYTE_BUF(ByteBuf.class);

        public final Class<?> clazz;

        Form(Class<?> clazz) {
            this.clazz = clazz;
        }

        Object wrap(byte[] array) {
            switch (this) {
                default:
                    return array;
                case BOXED_ARRAY:
                    return ByteArray.Box.box(array);
                case NIO_BYTE_BUFFER:
                    return ByteBuffer.wrap(array);
                case NETTY_BYTE_BUF:
                    return Unpooled.wrappedBuffer(array);
            }
        }

        Object convert(ByteBuf buf) {
            switch (this) {
                default:
                    return ByteBufUtil.getBytes(buf);
                case BOXED_ARRAY:
                    return ByteArray.Box.box(ByteBufUtil.getBytes(buf));
                case NIO_BYTE_BUFFER:
                    return toNioBuffer(buf);
                case NETTY_BYTE_BUF:
                    return buf;
            }
        }

        static ByteBuffer toNioBuffer(ByteBuf buf) {
            buf.readerIndex(0);
            try {
                return buf.nioBuffer();
            } catch (UnsupportedOperationException e) {
                return ByteBuffer.wrap(ByteBufUtil.getBytes(buf));
            } finally {
                buf.release();
            }
        }

        static Form parse(Class<?> clazz) {
            if (clazz == PRIMITIVE_ARRAY.clazz) {
                return PRIMITIVE_ARRAY;
            } else if (NETTY_BYTE_BUF.clazz.isAssignableFrom(clazz)) {
                return NETTY_BYTE_BUF; // it is used more often
            } else if (NIO_BYTE_BUFFER.clazz.isAssignableFrom(clazz)) {
                return NIO_BYTE_BUFFER;
            } else if (clazz == BOXED_ARRAY.clazz) {
                return BOXED_ARRAY;
            } else {
                return null;
            }
        }
    }

    Form form = Form.PRIMITIVE_ARRAY;

    JsonByteArrayType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonByteArrayType clone() {
        final JsonByteArrayType that = new JsonByteArrayType();
        that.setJsonType(this);
        that.form = this.form;
        return that;
    }

    @Override
    protected boolean linkCollectClass(Class<?> clazz) {
        final Form parsed = Form.parse(clazz);
        if (parsed != null) {
            form = parsed;
        }
        return false;
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        if (isObject) {
            return ConverterFrame.INSTANCE;
        } else {
            return new ByteArrayFrame();
        }
    }

    class ByteArrayFrame extends ConverterFrame {

        final ByteBuf buf = Unpooled.buffer();

        @Override
        protected Object finish(JsonParser jp) {
            return form.convert(buf);
        }

        @Override
        protected void numberValue(long value, JsonConverter jv) {
            if (anyParseConfig(ParseHint.APPLY_TRUNCATE) || Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
                buf.writeByte((int) value);
            } else if (jv.logEnabled()) {
                LOGGER.debug("Discard outbound value {} in {}", value, jv);
            }
        }
    }

    @Override
    protected Object parseString(String value, JsonConverter jv) {
        if (allParseConfigs(ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_BASE_N_STRING)) {
            try {
                return form.wrap(parseBaseN(value));
            } catch (RuntimeException e) {
                if (jv.logEnabled()) {
                    LOGGER.debug("Fail to parse String {} to byte array in {}", Quote.DEFAULT.apply(value), jv);
                }
                return JsonConstant.UNDEFINED;
            }
        } else {
            if (jv.logEnabled()) {
                LOGGER.debug("Discard String value {} while parsing byte array in {}", Quote.DEFAULT.apply(value), jv);
            }
            return JsonConstant.UNDEFINED;
        }
    }

    private static final Pattern BASE_N_STRING = Pattern.compile("base(2|4|8|16|32|64),\\s*", Pattern.CASE_INSENSITIVE);

    private byte[] parseBaseN(String string) {
        final Matcher matcher = BASE_N_STRING.matcher(string);
        if (!matcher.find()) {
            throw new NumberFormatException("pattern");
        }
        final int radix = Integer.parseInt(matcher.group(1));
        switch (radix) {
            case 2:
                return parseBinary(string, matcher.end());
            case 4:
                return parseQuarternary(string, matcher.end());
            case 16:
                return parseHexadecimal(string, matcher.end());
            case 32:
                return parseBase32(string, matcher.end());
            case 64:
                return Base64.getDecoder().decode(string.substring(matcher.end()));
        }
        throw new IllegalArgumentException("Unsupported radix: " + radix); // never
    }

    private byte[] parseBinary(String string, int offset) {
        int size = string.length() - offset;
        if ((size & 0x7) != 0) {
            throw new IllegalArgumentException("size: " + size);
        }
        size >>>= 3;
        final byte[] array = new byte[size];
        for (int index = 0; index < size; index++) {
            int value = 0;
            for (int shift = 0x7; shift >= 0; shift--) {
                int ch = string.charAt(offset++);
                if (ch == '1') {
                    value |= 1 << shift;
                } else if (ch != '0') {
                    throw new NumberFormatException();
                }
            }
            array[index] = (byte) value;
        }
        return array;
    }

    private byte[] parseQuarternary(String string, int offset) {
        int size = string.length() - offset;
        if ((size & 0x3) != 0) {
            throw new IllegalArgumentException("size: " + size);
        }
        size >>>= 2;
        final byte[] array = new byte[size];
        for (int index = 0; index < size; index++) {
            int value = 0;
            for (int shift = 0x6; shift >= 0; shift -= 2) {
                int ch = string.charAt(offset++);
                if ('0' <= ch && ch <= '3') {
                    value |= (ch - '0') << shift;
                } else {
                    throw new NumberFormatException();
                }
            }
            array[index] = (byte) value;
        }
        return array;
    }

    private byte[] parseHexadecimal(String string, int offset) {
        return StringUtil.decodeHexDump(string, offset, string.length() - offset);
    }

    /**
     * RFC4648
     */
    private byte[] parseBase32(String string, int offset) {
        int length = string.length();
        while (string.charAt(length - 1) == '=') {
            length--;
        }
        final byte[] array = new byte[(length * 5 + 0x7) >>> 3];
        int index = 0;
        int state = 0;
        int value = 0;
        while (offset < length) {
            int ch = string.charAt(offset++);
            if ('A' <= ch && ch <= 'Z') {
                ch -= 'A';
            } else if ('2' <= ch && ch <= '7') {
                ch -= '2' - 26;
            } else {
                throw new NumberFormatException();
            }
            switch (state++ & 0x7) {
                case 0:
                    value = ch << 3;
                    break;
                case 1:
                    array[index++] = (byte) (value | (ch >> 2));
                    value = ch << 6;
                    break;
                case 2:
                    value |= ch << 1;
                    break;
                case 3:
                    array[index++] = (byte) (value | (ch >> 4));
                    value = ch << 4;
                    break;
                case 4:
                    array[index++] = (byte) (value | (ch >> 1));
                    value = ch << 7;
                    break;
                case 5:
                    value |= ch << 2;
                    break;
                case 6:
                    array[index++] = (byte) (value | (ch >> 3));
                    value = ch << 5;
                    break;
                case 7:
                    array[index++] = (byte) (value | ch);
                    break;
            }
        }
        if (index < array.length) {
            array[index] = (byte) value;
        }
        return array;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("form");
        jc.stringValue(form.name());
    }

    @Override
    public String typeName() {
        return "byte-array";
    }
}
