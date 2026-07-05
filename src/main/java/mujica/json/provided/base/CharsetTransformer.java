package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2023/4/20", project = "Ultramarine", name = "CharsetValueSerializer")
@CodeHistory(date = "2026/4/21")
public class CharsetTransformer implements JsonContextTransformer<Charset>, JsonStructure {

    public static final CharsetTransformer INSTANCE = new CharsetTransformer();

    static final FastString ALIASES = new FastString("aliases");

    static final FastString REGISTERED = new FastString("registered");

    static final FastString CAN_ENCODE = new FastString("canEncode");

    static final FastString AVERAGE_BYTES_PER_CHAR = new FastString("averageBytesPerChar");

    static final FastString MAX_BYTES_PER_CHAR = new FastString("maxBytesPerChar");

    static final FastString AVERAGE_CHARS_PER_BYTE = new FastString("averageCharsPerByte");

    static final FastString MAX_CHARS_PER_BYTE = new FastString("maxCharsPerByte");

    @Override
    public void transform(@NotNull Charset charset, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(charset.name());
            out.stringKey(ALIASES);
            out.arrayValue(charset.aliases());
            out.stringKey(REGISTERED);
            out.booleanValue(charset.isRegistered());
            boolean canEncode = charset.canEncode();
            out.stringKey(CAN_ENCODE);
            out.booleanValue(canEncode);
            if (canEncode) {
                try {
                    CharsetEncoder encoder = charset.newEncoder();
                    out.stringKey(AVERAGE_BYTES_PER_CHAR);
                    out.numberValue(encoder.averageBytesPerChar());
                    out.stringKey(MAX_BYTES_PER_CHAR);
                    out.numberValue(encoder.maxBytesPerChar());
                } catch (Exception e) {
                    if (context != null) {
                        context.getLogger().debug("{}", charset, e);
                    }
                }
            }
            try {
                CharsetDecoder decoder = charset.newDecoder();
                out.stringKey(AVERAGE_CHARS_PER_BYTE);
                out.numberValue(decoder.averageCharsPerByte());
                out.stringKey(MAX_CHARS_PER_BYTE);
                out.numberValue(decoder.maxCharsPerByte());
            } catch (Exception e) {
                if (context != null) {
                    context.getLogger().debug("{}", charset, e);
                }
            }
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Charset.defaultCharset(), jh, null);
    }
}
