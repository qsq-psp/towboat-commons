package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Objects;

@CodeHistory(date = "2022/8/18", project = "Ultramarine")
@CodeHistory(date = "2025/10/21")
@ReferencePage(title = "Console Virtual Terminal Sequences", href = "https://learn.microsoft.com/en-us/windows/console/console-virtual-terminal-sequences#text-formatting")
public class TerminalStyle implements Serializable {

    private static final long serialVersionUID = 0xf0fd49fd765051f0L;

    public static final TerminalStyle RESET_STYLE = new TerminalStyle(null, null, null, null, null);

    static final String COMMAND_PREFIX = "\u001b[";

    static final String RESET_COMMAND = COMMAND_PREFIX + "0m";

    @Nullable
    public final TerminalDisplayColor foreground, background;

    @Nullable
    public final Boolean bold, underline, through;

    public TerminalStyle(@Nullable TerminalDisplayColor foreground, @Nullable TerminalDisplayColor background,
                         @Nullable Boolean bold, @Nullable Boolean underline, @Nullable Boolean through) {
        super();
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.underline = underline;
        this.through = through;
    }

    public boolean needReset() {
        return foreground == null || background == null || bold == null || underline == null || through == null;
    }

    public boolean isReset() {
        return this == RESET_STYLE || foreground == null && background == null && bold == null && underline == null && through == null;
    }

    private void stringifyNoReset(@NotNull StringBuilder out) {
        boolean subsequent = false;
        out.append(COMMAND_PREFIX);
        if (foreground != null) {
            out.append(foreground.foregroundCode);
            subsequent = true;
        }
        if (background != null) {
            if (subsequent) {
                out.append(';');
            }
            out.append(background.backgroundCode);
            subsequent = true;
        }
        if (bold != null) {
            if (subsequent) {
                out.append(';');
            }
            out.append(TerminalDisplayMode.BOLD.getCode(bold));
            subsequent = true;
        }
        if (underline != null) {
            if (subsequent) {
                out.append(';');
            }
            out.append(TerminalDisplayMode.UNDERLINE.getCode(underline));
            subsequent = true;
        }
        if (through != null) {
            if (subsequent) {
                out.append(';');
            }
            out.append(TerminalDisplayMode.THROUGH.getCode(through));
        }
        out.append('m');
    }

    public void stringify(@NotNull StringBuilder out) {
        if (needReset()) {
            out.append(RESET_COMMAND);
            if (isReset()) {
                return;
            }
        }
        stringifyNoReset(out);
    }

    public String stringify() {
        if (isReset()) {
            return RESET_COMMAND;
        }
        final StringBuilder sb = new StringBuilder();
        stringify(sb);
        return sb.toString();
    }

    public void stringify(@NotNull String string, @NotNull StringBuilder out) {
        if (needReset()) {
            out.append(RESET_COMMAND);
            if (isReset()) {
                out.append(string);
                return;
            }
        }
        stringifyNoReset(out);
        out.append(string).append(RESET_COMMAND);
    }

    public String stringify(@NotNull String string) {
        final StringBuilder sb = new StringBuilder();
        stringify(string, sb);
        return sb.toString();
    }

    private void printNoReset(@NotNull PrintStream out) {
        boolean subsequent = false;
        out.print(COMMAND_PREFIX);
        if (foreground != null) {
            out.print(foreground.foregroundCode);
            subsequent = true;
        }
        if (background != null) {
            if (subsequent) {
                out.print(";");
            }
            out.print(background.backgroundCode);
            subsequent = true;
        }
        if (bold != null) {
            if (subsequent) {
                out.print(";");
            }
            out.print(TerminalDisplayMode.BOLD.getCode(bold));
            subsequent = true;
        }
        if (underline != null) {
            if (subsequent) {
                out.print(";");
            }
            out.print(TerminalDisplayMode.UNDERLINE.getCode(underline));
            subsequent = true;
        }
        if (through != null) {
            if (subsequent) {
                out.print(";");
            }
            out.print(TerminalDisplayMode.THROUGH.getCode(through));
        }
        out.print("m");
    }

    public void print(@NotNull PrintStream out) {
        if (needReset()) {
            out.print(RESET_COMMAND);
            if (isReset()) {
                return;
            }
        }
        printNoReset(out);
    }

    public void print(@NotNull String string, @NotNull PrintStream out) {
        if (needReset()) {
            out.print(RESET_COMMAND);
            if (isReset()) {
                out.print(string);
                return;
            }
        }
        printNoReset(out);
        out.print(string);
        out.print(RESET_COMMAND);
    }

    public void println(@NotNull String string, @NotNull PrintStream out) {
        if (needReset()) {
            out.print(RESET_COMMAND);
            if (isReset()) {
                out.print(string);
                return;
            }
        }
        printNoReset(out);
        out.print(string);
        out.println(RESET_COMMAND);
    }

    private void writeNumber(int value, @NotNull OutputStream out) throws IOException {
        final int next = value / 10;
        final int digit = value - 10 * next;
        if (next != 0) {
            writeNumber(next, out);
        }
        out.write('0' + digit);
    }

    private void writeNoReset(@NotNull OutputStream out) throws IOException {
        boolean subsequent = false;
        out.write(0x1b);
        out.write('[');
        if (foreground != null) {
            writeNumber(foreground.foregroundCode, out);
            subsequent = true;
        }
        if (background != null) {
            if (subsequent) {
                out.write(';');
            }
            writeNumber(background.backgroundCode, out);
            subsequent = true;
        }
        if (bold != null) {
            if (subsequent) {
                out.write(';');
            }
            writeNumber(TerminalDisplayMode.BOLD.getCode(bold), out);
            subsequent = true;
        }
        if (underline != null) {
            if (subsequent) {
                out.write(';');
            }
            writeNumber(TerminalDisplayMode.UNDERLINE.getCode(underline), out);
            subsequent = true;
        }
        if (through != null) {
            if (subsequent) {
                out.write(';');
            }
            writeNumber(TerminalDisplayMode.THROUGH.getCode(through), out);
        }
        out.write('m');
    }

    private void writeReset(@NotNull OutputStream out) throws IOException {
        out.write(0x1b);
        out.write('[');
        out.write('0');
        out.write('m');
    }

    public void write(@NotNull OutputStream out) throws IOException {
        if (needReset()) {
            writeReset(out);
            if (isReset()) {
                return;
            }
        }
        writeNoReset(out);
    }

    public void write(@NotNull byte[] data, @NotNull OutputStream out) throws IOException {
        if (needReset()) {
            writeReset(out);
            if (isReset()) {
                out.write(data);
                return;
            }
        }
        writeNoReset(out);
        out.write(data);
        writeReset(out);
    }

    private static final int M = 59;

    @Override
    public int hashCode() {
        int hash = Objects.hashCode(foreground);
        hash = hash * M + Objects.hashCode(background);
        hash = hash * M + Objects.hashCode(bold);
        hash = hash * M + Objects.hashCode(underline);
        hash = hash * M + Objects.hashCode(through);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TerminalStyle)) {
            return false;
        }
        final TerminalStyle that = (TerminalStyle) obj;
        return this.foreground == that.foreground && this.background == that.background
                && Objects.equals(this.bold, that.bold) && Objects.equals(this.underline, that.underline)
                && Objects.equals(this.through, that.through);
    }

    @Override
    public String toString() {
        return "TerminalStyle[foreground = " + foreground + ", background = " + background
                + ", bold = " + bold + ", underline = " + underline + ", through = " + through + "]";
    }
}
