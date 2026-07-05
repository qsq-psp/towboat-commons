package mujica.xml;

import mujica.ds.any.list.TruncateList;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Created on 2026/6/21.
 */
public class ElementStructureCheckAdapter<H extends ContentHandler> extends ContentHandlerAdapter<H> {

    @NotNull
    protected final TruncateList<String> stack = new TruncateList<>();

    public ElementStructureCheckAdapter(@NotNull H h) {
        super(h);
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        h.startElement(uri, localName, qualifiedName, attributes);
        stack.add(uri);
        stack.add(localName);
        stack.add(qualifiedName);
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) throws SAXException {
        final String expectedQualifiedName = stack.removeLast();
        final String expectedLocalName = stack.removeLast();
        final String expectedUri = stack.removeLast();
        if (!expectedUri.equals(uri)) {
            throw new SAXException("uri mismatch");
        }
        if (!expectedLocalName.equals(localName)) {
            throw new SAXException("local name mismatch");
        }
        if (!expectedQualifiedName.equals(qualifiedName)) {
            throw new SAXException("qualified name mismatch");
        }
        h.endElement(uri, localName, qualifiedName);
    }
}
