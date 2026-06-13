package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;

@CodeHistory(date = "2022/11/29", project = "Ultramarine", name = "SaxAttributesValueSerializer")
@CodeHistory(date = "2026/5/31")
public class AttributesTransformer implements JsonContextTransformer<Attributes> {

    static final FastString URI = new FastString("uri");

    static final FastString LOCAL_NAME = new FastString("localName");

    static final FastString QUALIFIED_NAME = new FastString("qualifiedName");

    static final FastString TYPE = new FastString("type");

    static final FastString VALUE = new FastString("value");

    @Override
    public void transform(@NotNull Attributes in, @NotNull JsonHandler out, JsonContext context) {
        final int length = in.getLength();
        if (length == 0) {
            out.emptyArrayValue();
            return;
        }
        out.openArray();
        for (int index = 0; index < length; index++) {
            out.openObject();
            {
                String uri = in.getURI(index);
                if (!uri.isEmpty()) {
                    out.stringKey(URI);
                    out.stringValue(uri);
                }
            }
            {
                String localName = in.getLocalName(index);
                if (!localName.isEmpty()) {
                    out.stringKey(LOCAL_NAME);
                    out.stringValue(localName);
                }
            }
            {
                String qualifiedName = in.getQName(index);
                if (!qualifiedName.isEmpty()) {
                    out.stringKey(QUALIFIED_NAME);
                    out.stringValue(qualifiedName);
                }
            }
            {
                String type = in.getType(index);
                if (!type.isEmpty()) {
                    out.stringKey(TYPE);
                    out.stringValue(type);
                }
            }
            {
                String value = in.getValue(index);
                if (!value.isEmpty()) {
                    out.stringKey(VALUE);
                    out.stringValue(value);
                }
            }
            out.closeObject();
        }
        out.closeArray();
    }
}
