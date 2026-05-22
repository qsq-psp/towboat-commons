package mujica.json.provided.xml;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Locator;

/**
 * Created on 2026/5/6.
 */
public class LocatorTransformer implements JsonContextTransformer<Locator> {

    public static final LocatorTransformer INSTANCE = new LocatorTransformer();

    static final FastString LINE_NUMBER = new FastString("lineNumber");

    static final FastString COLUMN_NUMBER = new FastString("columnNumber");

    @Override
    public void transform(@NotNull Locator in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String id = in.getPublicId();
            if (id != null) {
                out.stringKey(InputSourceTransformer.PUBLIC_ID);
                out.stringValue(in.getPublicId());
            }
            id = in.getSystemId();
            if (id != null) {
                out.stringKey(InputSourceTransformer.SYSTEM_ID);
                out.stringValue(in.getSystemId());
            }
        }
        {
            out.stringKey(LINE_NUMBER);
            out.numberValue(in.getLineNumber());
            out.stringKey(COLUMN_NUMBER);
            out.numberValue(in.getColumnNumber());
        }
        out.closeObject();
    }
}
