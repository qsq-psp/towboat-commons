package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.util.ds.Index;
import indi.qsq.util.text.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created on 2022/10/5, named InitializationBufferedReaderReader.
 * Renamed on 2023/4/11.
 *
 * The reader to convert *.ini to json
 *
 * Alternate suffix: *.cfg, *.conf
 *
 * https://www.yuque.com/initit/initit/uq2f4k
 */
public class InitializationBufferedReader implements SyncReader {

    private final BufferedReader reader; // need to call readLine()

    private String section;

    private int lineNumber;

    public InitializationBufferedReader(java.io.Reader reader) {
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

    @SuppressWarnings("DefaultAnnotationParam")
    private static class StringInterval {

        String string;

        @Index(of = "string", inclusive = true)
        int fromIndex;

        @Index(of = "string", inclusive = false)
        int toIndex;

        private void trim() {
            while (fromIndex < toIndex && string.charAt(fromIndex) <= ' ') {
                fromIndex++;
            }
            while (toIndex > fromIndex && string.charAt(toIndex - 1) <= ' ') {
                toIndex--;
            }
        }

        String sectionName() {
            trim();
            return substring();
        }

        void key(@NotNull JsonConsumer jc) {
            trim();
            jc.key(substring());
        }

        private String substring() {
            return string.substring(fromIndex, toIndex);
        }

        void value(@NotNull JsonConsumer jc) {
            trim();
            if (toIndex - fromIndex >= 2 && string.charAt(fromIndex) == '"' && string.charAt(toIndex - 1) == '"') {
                jc.stringValue(unescape(fromIndex + 1, toIndex - 1));
                return;
            }
            try {
                jc.numberValue(Long.parseLong(string, fromIndex, toIndex, 10));
            } catch (NumberFormatException e) {
                switch (substring().toLowerCase()) {
                    case "null":
                        jc.nullValue();
                        break;
                    case "false":
                        jc.booleanValue(false);
                        break;
                    case "true":
                        jc.booleanValue(true);
                        break;
                    case "nan":
                    case "+nan":
                    case "-nan":
                        jc.numberValue(Double.NaN);
                        break;
                    case "inf":
                    case "+inf":
                    case "infinity":
                    case "+infinity":
                        jc.numberValue(Double.POSITIVE_INFINITY);
                        break;
                    case "-inf":
                    case "-infinity":
                        jc.numberValue(Double.NEGATIVE_INFINITY);
                        break;
                    default:
                        throw new IllegalArgumentException(toString());
                }
            }
        }

        @NotNull
        private String unescape(@Index(of = "string", inclusive = true) int fromIndex, @Index(of = "string", inclusive = false) int toIndex) {
            StringBuilder sb = null;
            int index0 = fromIndex;
            while (index0 < toIndex) {
                int index1 = string.indexOf('\\', index0);
                if (index1 == -1) {
                    break;
                }
                if (index1 + 1 >= toIndex) {
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
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                index0 = index1 + 2;
            }
            if (sb != null) {
                return sb.append(string, index0, toIndex).toString();
            } else {
                return string.substring(fromIndex, toIndex);
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("StringInterval[start = ").append(fromIndex);
            sb.append(", end = ").append(toIndex);
            if (string != null) {
                sb.append(", length = ").append(string.length());
                sb.append("substring = ");
                Quote.AUTO.append(sb, substring());
                sb.append("string = ");
                Quote.AUTO.append(sb, string);
            }
            return sb.append("]").toString();
        }
    }

    @Override
    public void read(@NotNull JsonConsumer jc) {
        final StringInterval interval = new StringInterval();
        section = null;
        jc.openObject();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank() || line.charAt(0) == ';') {
                    continue;
                }
                int length = line.length();
                if (line.charAt(0) =='[' && line.charAt(length - 1) == ']') {
                    if (section != null) {
                        jc.closeObject();
                    }
                    interval.string = line;
                    interval.fromIndex = 1;
                    interval.toIndex = length - 1;
                    section = interval.sectionName();
                    jc.key(section);
                    jc.openObject();
                } else {
                    int index = line.indexOf('=');
                    if (index == -1) {
                        throw new IllegalArgumentException(toString());
                    }
                    interval.string = line;
                    interval.fromIndex = 0;
                    interval.toIndex = index;
                    interval.key(jc);
                    interval.fromIndex = index + 1;
                    interval.toIndex = length;
                    interval.value(jc);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (section != null) {
            jc.closeObject();
        }
        jc.closeObject();
    }

    @Override
    public String toString() {
        return "InitializationBufferedReader[section = " + Quote.DEFAULT.apply(section, "null") + "lineNumber = " + lineNumber + "]";
    }
}
