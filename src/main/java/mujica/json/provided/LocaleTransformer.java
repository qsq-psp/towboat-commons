package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

@CodeHistory(date = "2022/7/19", project = "Ultramarine", name = "LocaleValueSerializer")
@CodeHistory(date = "2026/4/19")
public class LocaleTransformer implements JsonContextTransformer<Locale>, JsonStructure {

    public static final LocaleTransformer INSTANCE = new LocaleTransformer();

    static final FastString LANGUAGE = new FastString("language");

    static final FastString SCRIPT = new FastString("script");

    static final FastString COUNTRY = new FastString("country");

    static final FastString VARIANT = new FastString("variant");

    static final FastString DISPLAY_LANGUAGE = new FastString("displayLanguage");

    static final FastString DISPLAY_SCRIPT = new FastString("displayScript");

    static final FastString DISPLAY_COUNTRY = new FastString("displayCountry");

    static final FastString DISPLAY_VARIANT = new FastString("displayVariant");

    static final FastString DISPLAY_NAME = new FastString("displayName");

    static final FastString EXTENSIONS = new FastString("extensions");

    private static void emptyToUndefined(@NotNull FastString key, @NotNull String value, @NotNull JsonHandler out) {
        if (value.isEmpty()) {
            return;
        }
        out.stringKey(key);
        out.stringValue(value);
    }

    private static void emptyToUndefined(@NotNull Character key, String value, @NotNull JsonHandler out) {
        if (value == null || value.isEmpty()) {
            return;
        }
        out.stringKey(String.valueOf(key));
        out.stringValue(value);
    }

    private static void emptyToUndefined(Locale in, @NotNull JsonHandler out) {
        final Set<Character> extensionKeys = in.getExtensionKeys();
        if (extensionKeys.isEmpty()) {
            return;
        }
        out.stringKey(EXTENSIONS);
        out.openObject();
        for (Character extensionKey : extensionKeys) {
            if (extensionKey == null) {
                continue;
            }
            emptyToUndefined(extensionKey, in.getExtension(extensionKey), out);
        }
        out.closeObject();
    }

    @Override
    public void transform(Locale in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            emptyToUndefined(LANGUAGE, in.getLanguage(), out);
            emptyToUndefined(SCRIPT, in.getScript(), out);
            emptyToUndefined(COUNTRY, in.getCountry(), out);
            emptyToUndefined(VARIANT, in.getVariant(), out);
            emptyToUndefined(DISPLAY_LANGUAGE, in.getDisplayLanguage(), out);
            emptyToUndefined(DISPLAY_SCRIPT, in.getDisplayScript(), out);
            emptyToUndefined(DISPLAY_COUNTRY, in.getDisplayCountry(), out);
            emptyToUndefined(DISPLAY_VARIANT, in.getDisplayVariant(), out);
            out.stringKey(DISPLAY_NAME);
            out.stringValue(in.getDisplayName());
            emptyToUndefined(in, out); // extensions
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Locale.getDefault(), jh, null);
    }
}
