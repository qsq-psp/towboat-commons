package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
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

    static final FastString NAME = new FastString("name");

    static final FastString ALIASES = new FastString("aliases");

    static final FastString REGISTERED = new FastString("registered");

    static final FastString CAN_ENCODE = new FastString("canEncode");

    static final FastString AVERAGE_BYTES_PER_CHAR = new FastString("averageBytesPerChar");

    static final FastString MAX_BYTES_PER_CHAR = new FastString("maxBytesPerChar");

    static final FastString AVERAGE_CHARS_PER_BYTE = new FastString("averageCharsPerByte");

    static final FastString MAX_CHARS_PER_BYTE = new FastString("maxCharsPerByte");

    @Override
    public void transform(Charset in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAME);
            out.stringValue(in.name());
            out.stringKey(ALIASES);
            out.arrayValue(in.aliases());
            out.stringKey(REGISTERED);
            out.booleanValue(in.isRegistered());
            boolean canEncode = in.canEncode();
            out.stringKey(CAN_ENCODE);
            out.booleanValue(canEncode);
            if (canEncode) {
                try {
                    CharsetEncoder encoder = in.newEncoder();
                    out.stringKey(AVERAGE_BYTES_PER_CHAR);
                    out.numberValue(encoder.averageBytesPerChar());
                    out.stringKey(MAX_BYTES_PER_CHAR);
                    out.numberValue(encoder.maxBytesPerChar());
                } catch (Exception e) {
                    if (context != null) {
                        context.getLogger().debug("{}", in, e);
                    }
                }
            }
            try {
                CharsetDecoder decoder = in.newDecoder();
                out.stringKey(AVERAGE_CHARS_PER_BYTE);
                out.numberValue(decoder.averageCharsPerByte());
                out.stringKey(MAX_CHARS_PER_BYTE);
                out.numberValue(decoder.maxCharsPerByte());
            } catch (Exception e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
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
