package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Locator;

/**
 * Created on 2022/11/29.
 */
@SuppressWarnings("unused")
public class SaxLocatorValueSerializer implements ValueSerializer<Locator> {

    @Override
    public void serialize(String key, Locator value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        String id;
        jc.openObject();
        jc.optionalStringEntry("publicID", value.getPublicId());
        jc.optionalStringEntry("systemID", value.getSystemId());
        jc.key("lineNumber");
        jc.numberValue(value.getLineNumber()); // start from 1
        jc.key("columnNumber");
        jc.numberValue(value.getColumnNumber()); // start from 1
        jc.closeObject();
    }
}
