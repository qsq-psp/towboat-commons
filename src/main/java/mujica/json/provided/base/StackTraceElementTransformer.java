package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/10/2.
 */
public class StackTraceElementTransformer implements JsonContextTransformer<StackTraceElement> {

    static final FastString MODULE_NAME = new FastString("moduleName");

    static final FastString MODULE_VERSION = new FastString("moduleVersion");

    @Override
    public void transform(StackTraceElement in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String name = in.getFileName();
            if (name != null) {
                out.key(StackWalkerFrameTransformer.FILE_NAME);
                out.stringValue(name);
            }
        }
        {
            int lineNumber = in.getLineNumber();
            if (lineNumber >= 0) {
                out.key(StackWalkerFrameTransformer.LINE_NUMBER);
                out.numberValue(lineNumber);
            }
        }
        {
            String info = in.getModuleName();
            if (info != null) {
                out.key(MODULE_NAME);
                out.stringValue(info);
            }
            info = in.getModuleVersion();
            if (info != null) {
                out.key(MODULE_VERSION);
                out.stringValue(info);
            }
            info = in.getClassLoaderName();
            if (info != null) {
                out.key(ThreadTransformer.CLASS_LOADER);
                out.stringValue(info);
            }
        }
        {
            out.key(StackWalkerFrameTransformer.CLASS_NAME);
            out.stringValue(in.getClassName());
            out.key(StackWalkerFrameTransformer.METHOD_NAME);
            out.stringValue(in.getMethodName());
            out.key(StackWalkerFrameTransformer.NATIVE);
            out.booleanValue(in.isNativeMethod());
        }
        out.closeObject();
    }
}
