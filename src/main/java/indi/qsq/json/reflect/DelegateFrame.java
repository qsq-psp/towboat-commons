package indi.qsq.json.reflect;

import indi.qsq.json.io.RecursiveReader;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/12/20.
 */
public class DelegateFrame extends ConverterFrame {

    @NotNull
    protected final ConverterFrame delegate;

    public DelegateFrame(@NotNull ConverterFrame delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    protected ConverterFrame start(Object object) {
        return delegate.start(object);
    }

    @Override
    protected Object finish(JsonParser jp) {
        return delegate.finish(jp);
    }

    @Override
    protected void key(String key, JsonParser jp) {
        delegate.key(key, jp);
    }

    @Override
    protected ConverterFrame openValue(boolean isObject, JsonConverter jv) {
        return delegate.openValue(isObject, jv);
    }

    @Override
    protected void closeValue(Object value, boolean isObject, JsonConverter jv) {
        delegate.closeValue(value, isObject, jv);
    }

    @Override
    protected void nullValue(JsonConverter jv) {
        delegate.nullValue(jv);
    }

    @Override
    protected void booleanValue(boolean value, JsonConverter jv) {
        delegate.booleanValue(value, jv);
    }

    protected void numberValue(long value, JsonConverter jv) {
        delegate.numberValue(value, jv);
    }

    protected void numberValue(double value, JsonConverter jv) {
        delegate.numberValue(value, jv);
    }

    protected void stringValue(String value, JsonConverter jv) {
        delegate.stringValue(value, jv);
    }

    @Override
    public void accept(RecursiveReader reader) {
        delegate.accept(reader);
    }

    @Override
    public String toString() {
        return "DelegateFrame[" + delegate + "]";
    }
}
