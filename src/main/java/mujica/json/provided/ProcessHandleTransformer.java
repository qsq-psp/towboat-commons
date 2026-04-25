package mujica.json.provided;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created on 2026/4/24.
 */
public class ProcessHandleTransformer implements JsonContextTransformer<ProcessHandle>, JsonStructure {

    public static final ProcessHandleTransformer INSTANCE = new ProcessHandleTransformer();

    @Name(value = "process identifier", language = "en")
    @Name(value = "进程标识符", language = "zh")
    static final FastString PID = new FastString("pid");

    static final FastString PARENT = new FastString("parent");

    static final FastString CHILDREN = new FastString("children");

    static final FastString INFO = new FastString("info");

    static final FastString SUPPORTS_NORMAL_TERMINATION = new FastString("supportsNormalTermination");

    static final FastString ALIVE = new FastString("alive");

    @Override
    public void transform(ProcessHandle in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(PID);
            out.numberValue(in.pid());
            in.parent().ifPresent(parent -> {
                out.stringKey(PARENT);
                out.numberValue(parent.pid());
            });
            streamForEach(in.children(), () -> {
                out.stringKey(CHILDREN);
                out.openArray();
            }, child -> out.numberValue(child.pid()), out::closeArray);
            out.stringKey(INFO);
            ProcessHandleInfoTransformer.INSTANCE.transform(in.info(), out, context);
            out.stringKey(SUPPORTS_NORMAL_TERMINATION);
            out.booleanValue(in.supportsNormalTermination());
            out.stringKey(ALIVE);
            out.booleanValue(in.isAlive());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ProcessHandle.current(), jh, null);
    }

    private static <T> void streamForEach(@NotNull Stream<T> stream, @NotNull Runnable before, @NotNull Consumer<T> consumer, @NotNull Runnable after) {
        final PublicBooleanSlot notEmpty = new PublicBooleanSlot(false);
        stream.forEach(item -> {
            if (!notEmpty.getBoolean()) {
                notEmpty.setBoolean(true);
                before.run();
            }
            consumer.accept(item);
        });
        if (notEmpty.getBoolean()) {
            after.run();
        }
    }
}
