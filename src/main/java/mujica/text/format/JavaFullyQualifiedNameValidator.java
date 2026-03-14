package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/10")
@ReferencePage(title = "JLS12 Names", href = "https://docs.oracle.com/javase/specs/jls/se12/html/jls-6.html#jls-6.7")
public class JavaFullyQualifiedNameValidator extends AbstractFormatValidator.IntervalForm {

    /**
     * Strings like:
     * java.lang.Exception
     * java.util.ArrayList
     * java.util.Map$Entry
     */
    public static final JavaFullyQualifiedNameValidator SOURCE = new JavaFullyQualifiedNameValidator('.');

    /**
     * Strings like:
     * java/lang/Exception
     * java/util/ArrayList
     * java/util/Map$Entry
     */
    public static final JavaFullyQualifiedNameValidator BYTECODE = new JavaFullyQualifiedNameValidator('/');

    final char identifierSeparator;

    public JavaFullyQualifiedNameValidator(char identifierSeparator) {
        super();
        this.identifierSeparator = identifierSeparator;
    }

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int startIndex, int endIndex) {
        for (int index = startIndex; index <= endIndex; index++) {
            if (index == endIndex || string.charAt(index) == identifierSeparator) {
                LocalizedFailureMessage message = JavaIdentifierValidator.INSTANCE.get(locale, string.subSequence(startIndex, index).toString());
                if (message != BooleanFailureMessage.SUCCESS) {
                    return message;
                }
                startIndex = index + 1;
            }
        }
        return BooleanFailureMessage.SUCCESS;
    }
}
