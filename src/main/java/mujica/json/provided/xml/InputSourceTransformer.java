package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.InputSource;

/**
 * Created on 2026/5/7.
 */
public class InputSourceTransformer implements JsonContextTransformer<InputSource> {

    public static final InputSourceTransformer INSTANCE = new InputSourceTransformer();

    static final FastString PUBLIC_ID = new FastString("publicID");

    static final FastString SYSTEM_ID = new FastString("systemID");

    static final FastString ENCODING = new FastString("encoding");

    @Override
    public void transform(InputSource in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String str = in.getPublicId();
            if (str != null) {
                out.stringKey(PUBLIC_ID);
                out.stringValue(in.getPublicId());
            }
            str = in.getSystemId();
            if (str != null) {
                out.stringKey(SYSTEM_ID);
                out.stringValue(in.getSystemId());
            }
            str = in.getEncoding();
            if (str != null) {
                out.stringKey(ENCODING);
                out.stringValue(in.getEncoding());
            }
        }
        out.closeObject();
    }
}
