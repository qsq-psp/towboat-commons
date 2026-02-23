package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/2/2")
public class SimpleUniversalStringifier implements UniversalStringifier {

    public static final SimpleUniversalStringifier INSTANCE = new SimpleUniversalStringifier();

    @NotNull
    @Override
    public String apply(@NotNull Object object) {
        if (object.getClass().isArray()) {
            if (object instanceof Object[]) {
                return Arrays.deepToString((Object[]) object);
            } else {
                String string = Arrays.deepToString(new Object[] {object});
                return string.substring(1, string.length() - 1);
            }
        } else {
            return object.toString();
        }
    }
}
