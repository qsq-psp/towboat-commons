package mujica.reflect.source;

import mujica.io.function.IORunnable;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

@CodeHistory(date = "2022/6/6", project = "LeetInAction", name = "MergeProjectDictionary")
@CodeHistory(date = "2026/2/27")
public final class AbsorbDictionary extends DefaultHandler implements IORunnable, Consumer<Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbsorbDictionary.class);

    private final SAXParserFactory parserFactory = SAXParserFactory.newDefaultInstance();

    private final HashSet<String> wordSet = new HashSet<>();

    private StringBuilder sb;

    AbsorbDictionary() {
        super();
    }

    public static void main(String[] args) {
        try {
            (new AbsorbDictionary()).run();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public void run() throws IOException {
        for (Path project : Files.newDirectoryStream(Path.of("..").toAbsolutePath().normalize())) {
            Path dictionaryDirectory = project.resolve(".idea").resolve("dictionaries");
            if (Files.notExists(dictionaryDirectory)) {
                continue;
            }
            for (Path dictionaryFile : Files.newDirectoryStream(dictionaryDirectory)) {
                if (!Files.isRegularFile(dictionaryFile)) {
                    continue;
                }
                if (!dictionaryFile.getFileName().toString().toUpperCase().endsWith(".XML")) {
                    continue;
                }
                LOGGER.info("dictionary file {}", dictionaryFile);
                accept(dictionaryFile);
            }
        }
        LOGGER.info("word count {}", wordSet.size());
        try {
            StreamResult result;
            Path currentProjectDictionary = Path.of("./.idea/dictionaries/" + getUserName() + ".xml");
            if (Files.exists(currentProjectDictionary)) {
                result = new StreamResult(Files.newBufferedWriter(currentProjectDictionary, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
            } else {
                result = new StreamResult(System.out);
            }
            write(result);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void accept(Path xmlDictionaryFile) {
        try (InputStream is = Files.newInputStream(xmlDictionaryFile)) {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(is, this);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if ("w".equals(qName)) {
            sb = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("w".equals(qName) && sb != null && sb.length() > 0) {
            wordSet.add(sb.toString());
        }
        sb = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (sb != null) {
            sb.append(ch, start, length);
        }
    }

    private void write(@NotNull Result result) throws Exception {
        final ArrayList<String> wordList = new ArrayList<>(wordSet);
        wordList.sort(null);
        final SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        factory.setAttribute("indent-number", 4);
        final TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "utf-8");
        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        handler.setResult(result);
        handler.startDocument();
        final AttributesImpl attributes = new AttributesImpl();
        {
            attributes.addAttribute("", "", "name", "CDATA", "ProjectDictionaryState");
            handler.startElement("", "", "component", attributes);
            attributes.clear();
            attributes.addAttribute("", "", "name", "CDATA", getUserName());
            handler.startElement("", "", "dictionary", attributes);
            attributes.clear();
            handler.startElement("", "", "words", attributes);
            for (String word : wordList) {
                handler.startElement("", "", "w", attributes);
                char[] ca = word.toCharArray();
                handler.characters(ca, 0, ca.length);
                handler.endElement("", "", "w");
            }
            handler.endElement("", "", "words");
            handler.endElement("", "", "dictionary");
            handler.endElement("", "", "component");
        }
        handler.endDocument();
    }

    @NotNull
    private String getUserName() {
        return Objects.requireNonNullElse(System.getenv("USERNAME"), "USER");
    }
}
