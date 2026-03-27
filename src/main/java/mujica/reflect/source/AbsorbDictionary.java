package mujica.reflect.source;

import mujica.io.function.IORunnable;
import mujica.reflect.modifier.CodeHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
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

    public static void main(String[] args) throws IOException {
        (new AbsorbDictionary()).run();
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
}
