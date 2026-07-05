package mujica.xml;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Created on 2026/6/20.
 */
public class ContentHandlerAdapter<H extends ContentHandler> implements ContentHandler {

    @NotNull
    protected H h;

    public ContentHandlerAdapter(@NotNull H h) {
        super();
        this.h = h;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        h.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        h.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        h.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        h.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        h.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        h.startElement(uri, localName, qualifiedName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qualifiedName) throws SAXException {
        h.endElement(uri, localName, qualifiedName);
    }

    @Override
    public void characters(char[] charArray, int start, int length) throws SAXException {
        h.characters(charArray, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] charArray, int start, int length) throws SAXException {
        h.ignorableWhitespace(charArray, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        h.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        h.skippedEntity(name);
    }
}
