package mujica.json.provided.base;

import mujica.ds.bit.Bit;
import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

@CodeHistory(date = "2026/4/24")
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

    private static <T> void streamForEach(@NotNull Stream<T> stream, @NotNull Runnable before, @NotNull Consumer<T> consumer, @NotNull Runnable after) {
        final Bit notEmpty = new Bit(false);
        stream.forEach(item -> {
            if (!notEmpty.getBit()) {
                notEmpty.setBit(true);
                before.run();
            }
            consumer.accept(item);
        });
        if (notEmpty.getBit()) {
            after.run();
        }
    }

    @Override
    public void transform(@NotNull ProcessHandle processHandle, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(PID);
            out.numberValue(processHandle.pid());
            processHandle.parent().ifPresent(parent -> {
                out.key(PARENT);
                out.numberValue(parent.pid());
            });
            streamForEach(processHandle.children(), () -> {
                out.key(CHILDREN);
                out.openArray();
            }, child -> out.numberValue(child.pid()), out::closeArray);
            out.key(INFO);
            ProcessHandleInfoTransformer.INSTANCE.transform(processHandle.info(), out, context);
            out.key(SUPPORTS_NORMAL_TERMINATION);
            out.booleanValue(processHandle.supportsNormalTermination());
            out.key(ALIVE);
            out.booleanValue(processHandle.isAlive());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ProcessHandle.current(), jh, null);
    }
}
