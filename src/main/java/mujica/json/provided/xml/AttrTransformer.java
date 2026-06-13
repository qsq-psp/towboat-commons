package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

/**
 * Created on 2026/5/2.
 */
public class AttrTransformer implements JsonContextTransformer<Attr> {

    public static final AttrTransformer INSTANCE = new AttrTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString SPECIFIED = new FastString("specified");

    static final FastString IS_ID = new FastString("ID");

    @Override
    public void transform(Attr in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAME);
            out.stringValue(in.getName());
            out.stringKey(SPECIFIED);
            out.booleanValue(in.getSpecified());
            out.stringKey(AttributesTransformer.VALUE);
            out.stringValue(in.getValue());
            out.stringKey(IS_ID);
            out.booleanValue(in.isId());
        }
        out.closeObject();
    }
}
