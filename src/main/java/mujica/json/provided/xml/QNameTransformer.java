package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;

/**
 * Created on 2026/5/1.
 */
public class QNameTransformer implements JsonContextTransformer<QName> {

    public static final QNameTransformer INSTANCE = new QNameTransformer();

    static final FastString NAMESPACE_URI = new FastString("namespaceURI");

    static final FastString LOCAL_PART = new FastString("localPart");

    static final FastString PREFIX = new FastString("prefix");

    @Override
    public void transform(QName in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAMESPACE_URI);
            out.stringValue(in.getNamespaceURI());
            out.stringKey(LOCAL_PART);
            out.stringValue(in.getLocalPart());
            out.stringKey(PREFIX);
            out.stringValue(in.getPrefix());
        }
        out.closeObject();
    }
}
