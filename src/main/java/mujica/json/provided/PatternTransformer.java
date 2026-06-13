package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created on 2026/4/26.
 */
@CodeHistory(date = "2026/4/26")
public class PatternTransformer implements JsonContextTransformer<Pattern> {

    public static final PatternTransformer INSTANCE = new PatternTransformer();

    static final FastString PATTERN = new FastString("pattern");

    static final FastString CASE_INSENSITIVE = new FastString("caseInsensitive");

    static final FastString MULTILINE = new FastString("multiline");

    static final FastString DOTALL = new FastString("dotall");

    static final FastString UNICODE_CASE = new FastString("unicodeCase");

    static final FastString CANON_EQ = new FastString("canonEQ");

    static final FastString UNIX_LINES = new FastString("unixLines");

    static final FastString LITERAL = new FastString("literal");

    static final FastString UNICODE_CHARACTER_CLASS = new FastString("unicodeCharacterClass");

    static final FastString COMMENTS = new FastString("comments");

    @Override
    public void transform(Pattern in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(PATTERN);
            out.stringValue(in.pattern());
            int flags = in.flags();
            if ((flags & (Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL | Pattern.UNICODE_CASE)) != 0) {
                if ((flags & Pattern.CASE_INSENSITIVE) != 0) {
                    out.stringKey(CASE_INSENSITIVE);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.MULTILINE) != 0) {
                    out.stringKey(MULTILINE);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.DOTALL) != 0) {
                    out.stringKey(DOTALL);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.UNICODE_CASE) != 0) {
                    out.stringKey(UNICODE_CASE);
                    out.booleanValue(true);
                }
            }
            if ((flags & (Pattern.CANON_EQ | Pattern.UNIX_LINES | Pattern.LITERAL | Pattern.UNICODE_CHARACTER_CLASS | Pattern.COMMENTS)) != 0) {
                if ((flags & Pattern.CANON_EQ) != 0) {
                    out.stringKey(CANON_EQ);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.UNIX_LINES) != 0) {
                    out.stringKey(UNIX_LINES);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.LITERAL) != 0) {
                    out.stringKey(LITERAL);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.UNICODE_CHARACTER_CLASS) != 0) {
                    out.stringKey(UNICODE_CHARACTER_CLASS);
                    out.booleanValue(true);
                }
                if ((flags & Pattern.COMMENTS) != 0) {
                    out.stringKey(COMMENTS);
                    out.booleanValue(true);
                }
            }
        }
        out.closeObject();
    }
}
