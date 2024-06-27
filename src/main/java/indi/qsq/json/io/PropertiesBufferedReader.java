package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2022/10/5, named PropertiesBufferedReaderReader.
 * Renamed on 2023/4/11.
 *
 * The reader to convert *.properties to json
 */
public class PropertiesBufferedReader implements SyncReader {

    private final BufferedReader reader; // need to call readLine()

    private int lineNumber, pairCount;

    public PropertiesBufferedReader(java.io.Reader reader) {
        super();
        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        } else {
            this.reader = new BufferedReader(reader, 0x400); // 1024
        }
    }

    @Override
    public void config(int config) {
        // pass
    }

    private static final Pattern EMPTY_LINE = Pattern.compile("^(#|!|\\s*$)");

    private static final Pattern SEPARATOR = Pattern.compile("(?<!\\\\)\\s*[=:]\\s*");

    @Override
    public void read(@NotNull JsonConsumer jc) {
        jc.openObject();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (EMPTY_LINE.matcher(line).find()) {
                    continue;
                }
                pairCount++;
                Matcher matcher = SEPARATOR.matcher(line);
                if (matcher.find()) {
                    jc.key(unescape(line.substring(0, matcher.start())));
                    jc.stringValue(line.substring(matcher.end()));
                } else {
                    throw new IllegalArgumentException(toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jc.closeObject();
    }

    @NotNull
    private String unescape(@NotNull String string) {
        final int length = string.length();
        StringBuilder sb = null;
        int index0 = 0;
        while (index0 < length) {
            int index1 = string.indexOf('\\', index0);
            if (index1 == -1) {
                break;
            }
            if (index1 + 1 >= length) {
                throw new IllegalArgumentException();
            }
            if (sb == null) {
                sb = new StringBuilder();
                sb.append(string, index0, index1);
            }
            switch (string.charAt(index1 + 1)) {
                case 'b':
                    sb.append('\b');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case '=':
                    sb.append('=');
                    break;
                case ':':
                    sb.append(':');
                    break;
                case '\\':
                    sb.append('\\');
                    break;
            }
            index0 = index1 + 2;
        }
        if (sb != null) {
            return sb.append(string, index0, length).toString();
        } else {
            return string;
        }
    }

    @Override
    public String toString() {
        return "PropertiesBufferedReader[lineNumber = " + lineNumber + ", pairCount = " + pairCount + "]";
    }
}
