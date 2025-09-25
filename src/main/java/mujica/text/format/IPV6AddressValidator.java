package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/9/13")
public class IPV6AddressValidator extends AbstractFormatValidator.IntervalForm {

    public static final IPV6AddressValidator INSTANCE = new IPV6AddressValidator();

    @NotNull
    @Override
    LocalizedFailureMessage get(@Nullable Locale locale, @NotNull CharSequence string, int start, int end) {
        return null;
    }
}
