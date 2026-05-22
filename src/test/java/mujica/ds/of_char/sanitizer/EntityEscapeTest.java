package mujica.ds.of_char.sanitizer;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2026/4/30.
 */
public class EntityEscapeTest {

    private void caseEscape(@NotNull EntityEscapeAppender appender, @NotNull String in, @NotNull String out) {
        final StringBuilder sb = new StringBuilder();
        appender.append(in, sb);
        Assert.assertEquals(out, sb.toString());
    }

    private void caseEscape(@NotNull EntityEscapeAppender appender, @NotNull String string) {
        Assert.assertTrue(appender.isIdentity(string));
        caseEscape(appender, string, string);
    }

    @Test
    public void caseIdentity() {
        final EntityEscapeAppender appender = new EntityEscapeAppender();
        caseEscape(appender, "");
        caseEscape(appender, "!");
        caseEscape(appender, "   ");
        caseEscape(appender, "-+-");
        caseEscape(appender, "\\\\//");
        caseEscape(appender, "/* # */");
        caseEscape(appender, "([{}])");
    }

    @Test
    public void caseSimple() {
        final EntityEscapeAppender appender = new EntityEscapeAppender();
        caseEscape(appender, "<", "&lt;");
        caseEscape(appender, ">", "&gt;");
        caseEscape(appender, "&", "&amp;");
        caseEscape(appender, "%<", "%&lt;");
        caseEscape(appender, "%>", "%&gt;");
        caseEscape(appender, "%&", "%&amp;");
        caseEscape(appender, "<\r\n", "&lt;\r\n");
        caseEscape(appender, ">\r\n", "&gt;\r\n");
        caseEscape(appender, "&\r\n", "&amp;\r\n");
    }

    @Test
    public void caseComplex() {
        final EntityEscapeAppender appender = new EntityEscapeAppender();
        caseEscape(appender, "&lt;<", "&amp;lt;&lt;");
        caseEscape(appender, "&gt;>", "&amp;gt;&gt;");
        caseEscape(appender, "&amp;", "&amp;amp;");
        caseEscape(appender, "\"P&A\"", "&quot;P&amp;A&quot;");
    }

    @Test
    public void caseLoad() {
        final EntityEscapeAppender appender = new EntityEscapeAppender();
        appender.load(null, false);
        caseEscape(appender, "&lt;<", "&amp;lt;&lt;");
        caseEscape(appender, "&gt;>", "&amp;gt;&gt;");
        caseEscape(appender, "&amp;", "&amp;amp;");
        caseEscape(appender, "\"P&A\"", "&quot;P&amp;A&quot;");
        caseEscape(appender, "$£...", "$&pound;...");
        caseEscape(appender, "ΑΒ", "&Alpha;&Beta;");
        caseEscape(appender, "αβ", "&alpha;&beta;");
    }

    @Test
    public void caseLoadIfAbsent() {
        final EntityEscapeAppender appender = new EntityEscapeAppender();
        appender.load(null, true);
        caseEscape(appender, "&lt;<", "&amp;lt;&lt;");
        caseEscape(appender, "&gt;>", "&amp;gt;&gt;");
        caseEscape(appender, "&amp;", "&amp;amp;");
        caseEscape(appender, "\"P&A\"", "&quot;P&amp;A&quot;");
        caseEscape(appender, "$£...", "$&pound;...");
        caseEscape(appender, "ΑΒ", "&Alpha;&Beta;");
        caseEscape(appender, "αβ", "&alpha;&beta;");
    }
}
