package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CodeHistory(date = "2026/5/9")
@ReferenceCode(groupId = "com.google.guava", artifactId = "guava", version = "33.6.0-jre", fullyQualifiedName = "com.google.common.base.JdkPattern")
class JdkRegexStringSearch implements StringSearch {

    @NotNull
    final Pattern pattern;

    public JdkRegexStringSearch(@NotNull Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public int firstIndexOf(@NotNull CharSequence string) {
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.start();
        } else {
            return -1;
        }
    }
}
