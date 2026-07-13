package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.ext.Attributes2;

@CodeHistory(date = "2022/11/29", project = "Ultramarine", name = "SaxAttributesValueSerializer")
@CodeHistory(date = "2026/5/31")
public class AttributesTransformer implements JsonContextTransformer<Attributes> {

    static final FastString URI = new FastString("uri");

    static final FastString LOCAL_NAME = new FastString("localName");

    static final FastString QUALIFIED_NAME = new FastString("qualifiedName");

    static final FastString TYPE = new FastString("type");

    static final FastString VALUE = new FastString("value");

    static final FastString DECLARED = new FastString("declared");

    static final FastString SPECIFIED = new FastString("specified");

    @Override
    public void transform(@NotNull Attributes attributes, @NotNull JsonHandler out, JsonContext context) {
        final int length = attributes.getLength();
        if (length == 0) {
            out.emptyArrayValue();
            return;
        }
        out.openArray();
        for (int index = 0; index < length; index++) {
            out.openObject();
            {
                String uri = attributes.getURI(index);
                if (!uri.isEmpty()) {
                    out.key(URI);
                    out.stringValue(uri);
                }
            }
            {
                String localName = attributes.getLocalName(index);
                if (!localName.isEmpty()) {
                    out.key(LOCAL_NAME);
                    out.stringValue(localName);
                }
            }
            {
                String qualifiedName = attributes.getQName(index);
                if (!qualifiedName.isEmpty()) {
                    out.key(QUALIFIED_NAME);
                    out.stringValue(qualifiedName);
                }
            }
            {
                String type = attributes.getType(index);
                if (!type.isEmpty()) {
                    out.key(TYPE);
                    out.stringValue(type);
                }
            }
            {
                String value = attributes.getValue(index);
                if (!value.isEmpty()) {
                    out.key(VALUE);
                    out.stringValue(value);
                }
            }
            if (attributes instanceof Attributes2) {
                Attributes2 attributes2 = (Attributes2) attributes;
                out.key(DECLARED);
                out.booleanValue(attributes2.isDeclared(index));
                out.key(SPECIFIED);
                out.booleanValue(attributes2.isSpecified(index));
            }
            out.closeObject();
        }
        out.closeArray();
    }
}
