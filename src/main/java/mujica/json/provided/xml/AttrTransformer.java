package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

@CodeHistory(date = "2026/5/2")
public class AttrTransformer implements JsonContextTransformer<Attr> {

    public static final AttrTransformer INSTANCE = new AttrTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString SPECIFIED = new FastString("specified");

    static final FastString IS_ID = new FastString("ID");

    @Override
    public void transform(@NotNull Attr attrNode, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(NAME);
            out.stringValue(attrNode.getName());
            out.key(SPECIFIED);
            out.booleanValue(attrNode.getSpecified());
            out.key(AttributesTransformer.VALUE);
            out.stringValue(attrNode.getValue());
            out.key(IS_ID);
            out.booleanValue(attrNode.isId());
        }
        out.closeObject();
    }
}
