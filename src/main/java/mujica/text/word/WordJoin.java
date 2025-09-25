package mujica.text.word;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

@CodeHistory(date = "2022/6/13", project = "Ultramarine")
@CodeHistory(date = "2025/3/7")
public class WordJoin implements Serializable {

    private static final long serialVersionUID = 0x27565ac0fb5946ccL;

    public static final WordJoin CONCAT = new WordJoin();

    public WordJoin() {
        super();
    }

    public void join(@NotNull StringBuilder out, Iterable<String> words) {
        for (String word : words) {
            out.append(word);
        }
    }

    public String join(Iterable<String> words) {
        final StringBuilder out = new StringBuilder();
        join(out, words);
        return out.toString();
    }

    public void join(StringBuilder out, @NotNull String[] words) {
        join(out, Arrays.asList(words));
    }

    public String join(@NotNull String[] words) {
        final StringBuilder out = new StringBuilder();
        join(out, words);
        return out.toString();
    }

    public static class ByChar extends WordJoin {

        private static final long serialVersionUID = 0xf027349cf64689f1L;

        public static final ByChar SPACE = new ByChar(' ');
        
        public static final ByChar DOT = new ByChar('.');
        
        public static final ByChar SLASH = new ByChar('/');

        public static final ByChar COLON = new ByChar(':');

        protected final char separatorChar;

        public ByChar(char separatorChar) {
            super();
            this.separatorChar = separatorChar;
        }

        @Override
        public void join(@NotNull StringBuilder out, Iterable<String> words) {
            boolean subsequent = false;
            for (String word : words) {
                if (subsequent) {
                    out.append(separatorChar);
                }
                out.append(word);
                subsequent = true;
            }
        }
    }

    public static class CaseByChar extends ByChar {

        private static final long serialVersionUID = 0x68e519c96bea2d1aL;

        public static final CaseByChar DASH = new CaseByChar('-', false); // aka kebab-case
        
        public static final CaseByChar UNDERSCORE = new CaseByChar('_', true);

        protected final boolean upper;

        public CaseByChar(char separatorChar, boolean upper) {
            super(separatorChar);
            this.upper = upper;
        }

        @Override
        public void join(@NotNull StringBuilder out, Iterable<String> words) {
            boolean subsequent = false;
            for (String word : words) {
                if (subsequent) {
                    out.append(separatorChar);
                }
                if (upper) {
                    out.append(word.toUpperCase());
                } else {
                    out.append(word.toLowerCase());
                }
                subsequent = true;
            }
        }
    }

    public static class ByCharSequence extends WordJoin {

        private static final long serialVersionUID = 0x8baf21f03f41f37aL;

        public static final ByCharSequence COMMA_SPACE = new ByCharSequence(", ");

        public static final ByCharSequence TWO_COLONS = new ByCharSequence("::");

        protected final CharSequence separatorCharSequence;

        public ByCharSequence(CharSequence separatorCharSequence) {
            super();
            this.separatorCharSequence = separatorCharSequence;
        }

        @Override
        public void join(@NotNull StringBuilder out, Iterable<String> words) {
            boolean subsequent = false;
            for (String word : words) {
                if (subsequent) {
                    out.append(separatorCharSequence);
                }
                out.append(word);
                subsequent = true;
            }
        }
    }

    @CodeHistory(date = "2018/11/24", name = "CamelCase")
    @CodeHistory(date = "2022/6/13")
    public static class Case extends WordJoin {

        private static final long serialVersionUID = 0xc2c5f57961ce302dL;

        public static final Case PASCAL = new Case(true); // PascalCase for java ClassName
        
        public static final Case CAMEL = new Case(false); // camelCase for java memberName (fieldName or methodName)

        protected final boolean firstUpper;

        public Case(boolean firstUpper) {
            super();
            this.firstUpper = firstUpper;
        }

        @Override
        public void join(@NotNull StringBuilder out, Iterable<String> words) {
            boolean subsequent = firstUpper;
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }
                if (subsequent) {
                    out.append(Character.toUpperCase(word.charAt(0)));
                    out.append(word.substring(1).toLowerCase());
                } else {
                    out.append(word.toLowerCase());
                }
                subsequent = true;
            }
        }
    }

    public static class Abbreviation extends WordJoin {

        private static final long serialVersionUID = 0x2a4c124d92ae4690L;

        public static final Abbreviation INSTANCE = new Abbreviation();

        @Override
        public void join(@NotNull StringBuilder out, Iterable<String> words) {
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }
                out.append(Character.toUpperCase(word.charAt(0)));
            }
        }
    }
}
