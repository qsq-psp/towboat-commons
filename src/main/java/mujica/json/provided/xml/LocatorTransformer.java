package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;

/**
 * Created on 2026/5/6.
 */
public class LocatorTransformer implements JsonContextTransformer<Locator> {

    public static final LocatorTransformer INSTANCE = new LocatorTransformer();

    static final FastString VERSION = new FastString("version");

    static final FastString LINE_NUMBER = new FastString("lineNumber");

    static final FastString COLUMN_NUMBER = new FastString("columnNumber");

    @Override
    public void transform(@NotNull Locator locator, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        if (locator instanceof Locator2) {
            Locator2 locator2 = (Locator2) locator;
            out.key(VERSION);
            out.stringValue(locator2.getXMLVersion());
            out.key(InputSourceTransformer.ENCODING);
            out.stringValue(locator2.getEncoding());
        }
        {
            String id = locator.getPublicId();
            if (id != null) {
                out.key(InputSourceTransformer.PUBLIC_ID);
                out.stringValue(locator.getPublicId());
            }
            id = locator.getSystemId();
            if (id != null) {
                out.key(InputSourceTransformer.SYSTEM_ID);
                out.stringValue(locator.getSystemId());
            }
        }
        {
            out.key(LINE_NUMBER);
            out.numberValue(locator.getLineNumber());
            out.key(COLUMN_NUMBER);
            out.numberValue(locator.getColumnNumber());
        }
        out.closeObject();
    }
}
