package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/25.
 */
public class ProcessHandleInfoTransformer implements JsonContextTransformer<ProcessHandle.Info> {

    public static final ProcessHandleInfoTransformer INSTANCE = new ProcessHandleInfoTransformer();

    static final FastString COMMAND = new FastString("command");

    static final FastString COMMAND_LINE = new FastString("commandLine");

    static final FastString ARGUMENTS = new FastString("arguments");

    static final FastString START = new FastString("start");

    static final FastString DURATION = new FastString("duration");

    static final FastString USER = new FastString("user");

    @Override
    public void transform(ProcessHandle.Info in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            in.command().ifPresent(command -> {
                out.stringKey(COMMAND);
                out.stringValue(command);
            });
            in.commandLine().ifPresent(commandLine -> {
                out.stringKey(COMMAND_LINE);
                out.stringValue(commandLine);
            });
            in.arguments().ifPresent(arguments -> {
                out.stringKey(ARGUMENTS);
                out.arrayValue(arguments);
            });
            in.startInstant().ifPresent(start -> {
                out.stringKey(START);
                InstantTransformer.INSTANCE.transform(start, out, context);
            });
            // duration
            in.user().ifPresent(user -> {
                out.stringKey(USER);
                out.stringValue(user);
            });
        }
        out.closeObject();
    }
}
