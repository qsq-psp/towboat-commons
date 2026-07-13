package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/25.
 */
@CodeHistory(date = "2026/4/25")
public class ProcessHandleInfoTransformer implements JsonContextTransformer<ProcessHandle.Info> {

    public static final ProcessHandleInfoTransformer INSTANCE = new ProcessHandleInfoTransformer();

    static final FastString COMMAND = new FastString("command");

    static final FastString COMMAND_LINE = new FastString("commandLine");

    static final FastString ARGUMENTS = new FastString("arguments");

    static final FastString START = new FastString("start");

    static final FastString DURATION = new FastString("duration");

    static final FastString USER = new FastString("user");

    @Override
    public void transform(ProcessHandle.Info info, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            info.command().ifPresent(command -> {
                out.key(COMMAND);
                out.stringValue(command);
            });
            info.commandLine().ifPresent(commandLine -> {
                out.key(COMMAND_LINE);
                out.stringValue(commandLine);
            });
            info.arguments().ifPresent(arguments -> {
                out.key(ARGUMENTS);
                out.arrayValue(arguments);
            });
            info.startInstant().ifPresent(start -> {
                out.key(START);
                InstantTransformer.INSTANCE.transform(start, out, context);
            });
            info.totalCpuDuration().ifPresent(duration -> {
                out.key(DURATION);
                DurationTransformer.INSTANCE.transform(duration, out, context);
            });
            info.user().ifPresent(user -> {
                out.key(USER);
                out.stringValue(user);
            });
        }
        out.closeObject();
    }
}
