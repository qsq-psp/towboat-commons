package mujica.json.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

/**
 * Created on 2026/6/9.
 */
public class JsonIndentCharStreamWriter extends JsonCharStreamWriter {

    @Nullable
    protected String indent;

    @NotNull
    public JsonIndentCharStreamWriter setIndent(@Nullable String indent) {
        this.indent = indent;
        return this;
    }

    public JsonIndentCharStreamWriter(@NotNull Writer out) {
        super(out);
        setFlags(ConfigFlags.ESCAPE_EXTRA | ConfigFlags.COMMA_SPACE | ConfigFlags.COLON_SPACE);
    }

    protected void writeOpenClose() throws IOException {
        if (indent != null) {
            out.write(System.lineSeparator());
            for (int count = stack.intLength() - 1; count > 0; count--) {
                out.write(indent);
            }
        }
    }

    protected void writeComma() throws IOException {
        if (indent != null) {
            out.write(',');
            out.write(System.lineSeparator());
            for (int count = stack.intLength(); count > 0; count--) {
                out.write(indent);
            }
        } else if ((flags & ConfigFlags.COMMA_SPACE) != 0) {
            out.write(", ");
        } else {
            out.write(',');
        }
    }

    @Override
    public void openArray() {
        try {
            anyValue();
            stack.offerLast(STATE_NEW_ARRAY);
            out.write('[');
            writeOpenClose();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void closeArray() {
        final int state = stack.removeLast();
        try {
            writeOpenClose();
            if (state == STATE_NEW_ARRAY || state == STATE_ARRAY) {
                out.write(']');
            } else {
                stack.offerLast(state);
                throwState();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void openObject() {
        try {
            anyValue();
            stack.offerLast(STATE_NEW_OBJECT);
            out.write('{');
            writeOpenClose();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void closeObject() {
        final int state = stack.removeLast();
        try {
            writeOpenClose();
            if (state == STATE_NEW_OBJECT || state == STATE_OBJECT) {
                out.write('}');
            } else {
                stack.offerLast(state);
                throwState();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void openJsonp(@NotNull CharSequence name) {
        final int state = stack.removeLast();
        if (state != STATE_START) {
            stack.offerLast(state);
            throwState();
        }
        stack.offerLast(STATE_JSONP);
        stack.offerLast(STATE_START);
        try {
            out.write(name.toString());
            out.write('(');
            writeOpenClose();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void closeJsonp() {
        int state = stack.removeLast();
        if (state != STATE_END) {
            stack.offerLast(state);
            throwState();
        }
        state = stack.removeLast();
        if (state != STATE_JSONP) {
            stack.offerLast(state);
            stack.offerLast(STATE_END);
            throwState();
        }
        stack.offerLast(STATE_END);
        try {
            writeOpenClose();
            out.write(')');
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
