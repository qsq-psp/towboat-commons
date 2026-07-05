package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;

/**
 * Created on 2026/5/7.
 */
@CodeHistory(date = "2026/5/7")
public class InputSourceTransformer implements JsonContextTransformer<InputSource> {

    public static final InputSourceTransformer INSTANCE = new InputSourceTransformer();

    static final FastString PUBLIC_ID = new FastString("publicID");

    static final FastString SYSTEM_ID = new FastString("systemID");

    static final FastString ENCODING = new FastString("encoding");

    static final FastString BYTE_STREAM = new FastString("byteStream");

    static final FastString CHARACTER_STREAM = new FastString("characterStream");

    @Override
    public void transform(InputSource source, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String str = source.getPublicId();
            if (str != null) {
                out.stringKey(PUBLIC_ID);
                out.stringValue(source.getPublicId());
            }
            str = source.getSystemId();
            if (str != null) {
                out.stringKey(SYSTEM_ID);
                out.stringValue(source.getSystemId());
            }
            str = source.getEncoding();
            if (str != null) {
                out.stringKey(ENCODING);
                out.stringValue(source.getEncoding());
            }
        }
        {
            InputStream byteStream = source.getByteStream();
            if (byteStream != null) {
                out.stringKey(BYTE_STREAM);
                out.stringValue(byteStream.getClass().getName());
            }
        }
        {
            Reader characterStream = source.getCharacterStream();
            if (characterStream != null) {
                out.stringKey(CHARACTER_STREAM);
                out.stringValue(characterStream.getClass().getName());
            }
        }
        out.closeObject();
    }
}
