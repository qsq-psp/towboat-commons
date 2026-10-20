package mujica.json.provided.xml;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.namespace.QName;

/**
 * Created on 2026/5/1.
 */
@CodeHistory(date = "2026/5/1")
public class QNameTransformer implements JsonContextTransformer<QName> {

    public static final QNameTransformer INSTANCE = new QNameTransformer();

    static final FastString NAMESPACE_URI = new FastString("namespaceURI");

    static final FastString LOCAL_PART = new FastString("localPart");

    static final FastString PREFIX = new FastString("prefix");

    @Override
    public void transform(QName qName, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(NAMESPACE_URI);
            out.stringValue(qName.getNamespaceURI());
            out.key(LOCAL_PART);
            out.stringValue(qName.getLocalPart());
            out.key(PREFIX);
            out.stringValue(qName.getPrefix());
        }
        out.closeObject();
    }
}
