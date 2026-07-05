package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2021/7/15", project = "va", name = "ThrowableJsonValue")
@CodeHistory(date = "2021/7/15", project = "webbiton", name = "ThrowableValue")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "ThrowableValue")
@CodeHistory(date = "2026/5/1")
public class ThrowableTransformer implements JsonContextTransformer<Throwable> {

    public static final ThrowableTransformer INSTANCE = new ThrowableTransformer();

    static final FastString MESSAGE = new FastString("message");

    static final FastString CAUSE = new FastString("cause");

    static final FastString SUPPRESSED = new FastString("suppressed");

    @Override
    public void transform(@NotNull Throwable in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        if (context == null) {
            context = new JsonContext();
        }
        if (context.addContainerObject(in)) {
            try {
                out.openObject();
                {
                    out.stringKey(ClassLoaderTransformer.CLASS);
                    out.stringValue(in.getClass().getName());
                    String message = in.getMessage();
                    if (message != null) {
                        out.stringKey(MESSAGE);
                        out.stringValue(message);
                    }
                    Throwable cause = in.getCause();
                    if (cause != null) {
                        out.stringKey(CAUSE);
                        transform(cause, out, context);
                    }
                    Throwable[] suppressedArray = in.getSuppressed();
                    if (suppressedArray != null && suppressedArray.length != 0) {
                        out.stringKey(SUPPRESSED);
                        out.openArray();
                        for (Throwable suppressed : suppressedArray) {
                            transform(suppressed, out, context);
                        }
                        out.closeArray();
                    }
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(in);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("throwable loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("throwable loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }
}
