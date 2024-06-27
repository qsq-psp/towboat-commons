package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Set;

/**
 * Created on 2022/7/19.
 */
@SuppressWarnings("unused")
public class LocaleValueSerializer implements ValueSerializer<Locale>, JsonStructure {

    public static final LocaleValueSerializer INSTANCE = new LocaleValueSerializer();

    void serialize(String key, Locale value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("language");
        jc.stringValue(value.getLanguage());
        jc.key("script");
        jc.stringValue(value.getScript());
        jc.key("country");
        jc.stringValue(value.getCountry());
        jc.key("variant");
        jc.stringValue(value.getVariant());
        jc.key("displayLanguage");
        jc.stringValue(value.getDisplayLanguage());
        jc.key("displayScript");
        jc.stringValue(value.getDisplayScript());
        jc.key("displayCountry");
        jc.stringValue(value.getDisplayCountry());
        jc.key("displayVariant");
        jc.stringValue(value.getDisplayVariant());
        jc.key("displayName");
        jc.stringValue(value.getDisplayName());
        final Set<Character> extensionKeys = value.getExtensionKeys();
        if (!extensionKeys.isEmpty()) {
            jc.key("extensions");
            jc.openObject();
            for (Character extensionKey : extensionKeys) {
                if (extensionKey == null) {
                    continue;
                }
                jc.key(extensionKey.toString());
                jc.objectValue(value.getExtension(extensionKey));
            }
            jc.closeObject();
        }
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Locale value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Locale.getDefault(), jc);
    }
}
