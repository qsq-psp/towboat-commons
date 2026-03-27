package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;

@CodeHistory(date = "2026/2/23", name = "PercentAppender")
@CodeHistory(date = "2026/3/8")
public class PercentEncodeAppender extends CharSequenceAppender {

    @ReferencePage(title = "encodeURI()", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI")
    public static final IntPredicate URI = (ch) -> {
        // A–Z a–z 0–9 - _ . ! ~ * ' ( )
        // ; / ? : @ & = + $ , #
        switch (ch) {
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
            case '_':
            case '.':
            case '!':
            case '~':
            case '*':
            case '\'':
            case '(':
            case ')':
            case ';':
            case '/':
            case '?':
            case ':':
            case '@':
            case '&':
            case '=':
            case '+':
            case '$':
            case ',':
            case '#':
                return true;
            default:
                return false;
        }
    };

    @ReferencePage(title = "encodeURIComponent()", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURIComponent")
    public static final IntPredicate URI_COMPONENT = (ch) -> {
        // A–Z a–z 0–9 - _ . ! ~ * ' ( )
        switch (ch) {
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            case '-':
            case '_':
            case '.':
            case '!':
            case '~':
            case '*':
            case '\'':
            case '(':
            case ')':
                return true;
            default:
                return false;
        }
    };

    @NotNull
    protected final IntPredicate noEscape;

    public PercentEncodeAppender(@NotNull IntPredicate noEscape) {
        super();
        this.noEscape = noEscape;
    }

    @NotNull
    public static PercentEncodeAppender encodeURI() {
        return new PercentEncodeAppender(URI);
    }

    @NotNull
    public static PercentEncodeAppender encodeURIComponent() {
        return new PercentEncodeAppender(URI_COMPONENT);
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string) {
        return isIdentity(string, 0, string.length());
    }

    @Override
    public boolean isIdentity(@NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            if (noEscape.test(string.charAt(index))) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string) {
        return deltaCharCount(string, 0, string.length());
    }

    @Override
    public int deltaCharCount(@NotNull CharSequence string, int startIndex, int endIndex) {
        int count = 0;
        for (int index = startIndex; index < endIndex; index++) {
            int ch = string.charAt(index);
            if (noEscape.test(ch)) {
                continue;
            }
            if (ch < 0x80) {
                count += 3 - 1;
            } else if (ch < 0x800 || Character.isSurrogate((char) ch)) {
                count += 2 * 3 - 1;
            } else {
                count += 3 * 3 - 1;
            }
        }
        return count;
    }

    @Override
    public int charEditDistance(@NotNull CharSequence string, int startIndex, int endIndex) {
        int distance = 0;
        for (int index = startIndex; index < endIndex; index++) {
            int ch = string.charAt(index);
            if (noEscape.test(ch)) {
                continue;
            }
            if (ch < 0x80) {
                distance += 3;
                if (ch == '%') {
                    distance--;
                }
            } else if (ch < 0x800 || Character.isSurrogate((char) ch)) {
                distance += 2 * 3;
            } else {
                distance += 3 * 3;
            }
        }
        return distance;
    }
}
