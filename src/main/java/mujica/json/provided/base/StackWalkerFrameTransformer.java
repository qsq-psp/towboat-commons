package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/10/2.
 */
public class StackWalkerFrameTransformer implements JsonContextTransformer<StackWalker.StackFrame> {

    static final FastString CLASS_NAME = new FastString("className");

    static final FastString METHOD_NAME = new FastString("methodName");

    static final FastString METHOD_DESCRIPTOR = new FastString("methodDescriptor");

    static final FastString BYTE_CODE_INDEX = new FastString("byteCodeIndex");

    static final FastString FILE_NAME = new FastString("fileName");

    static final FastString LINE_NUMBER = new FastString("lineNumber");

    static final FastString NATIVE = new FastString("native");

    @Override
    public void transform(StackWalker.StackFrame in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String name = in.getClassName();
            if (name != null) {
                out.key(CLASS_NAME);
                out.stringValue(name);
            }
            name = in.getMethodName();
            if (name != null) {
                out.key(METHOD_NAME);
                out.stringValue(name);
            }
        }
        try {
            String descriptor = in.getDescriptor();
            if (descriptor != null) {
                out.key(METHOD_DESCRIPTOR);
                out.stringValue(descriptor);
            }
        } catch (UnsupportedOperationException ignore) {}
        {
            int byteCodeIndex = in.getByteCodeIndex();
            if (byteCodeIndex >= 0) {
                out.key(BYTE_CODE_INDEX);
                out.numberValue(byteCodeIndex);
            }
        }
        {
            String name = in.getFileName();
            if (name != null) {
                out.key(FILE_NAME);
                out.stringValue(name);
            }
        }
        {
            int lineNumber = in.getLineNumber();
            if (lineNumber >= 0) {
                out.key(LINE_NUMBER);
                out.numberValue(lineNumber);
            }
        }
        {
            out.key(NATIVE);
            out.booleanValue(in.isNativeMethod());
        }
        out.closeObject();
    }
}
