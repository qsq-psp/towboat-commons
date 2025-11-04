package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@CodeHistory(date = "2025/10/21")
public class TerminalStyleTransition implements Serializable {

    private static final long serialVersionUID = 0xAD09F24ABCF225AFL;

    @NotNull
    private TerminalStyle src, dst;

    public TerminalStyleTransition() {
        super();
        this.src = TerminalStyle.RESET_STYLE;
        this.dst = TerminalStyle.RESET_STYLE;
    }

    public TerminalStyleTransition(@NotNull TerminalStyle src, @NotNull TerminalStyle dst) {
        super();
        this.src = Objects.requireNonNull(src);
        this.dst = Objects.requireNonNull(dst);
    }

    @NotNull
    public TerminalStyle getSrc() {
        return src;
    }

    @NotNull
    public TerminalStyle getDst() {
        return dst;
    }

    @NotNull
    public TerminalStyleTransition setSrc(@NotNull TerminalStyle src) {
        this.src = src;
        return this;
    }

    @NotNull
    public TerminalStyleTransition setDst(@NotNull TerminalStyle dst) {
        this.dst = dst;
        return this;
    }

    @NotNull
    public TerminalStyleTransition copy() {
        src = dst;
        return this;
    }

    public boolean isEmpty() {
        return src == dst || src.mode == dst.mode && src.foreground == dst.foreground && src.background == dst.background;
    }

    public boolean needReset() {
        return src != dst && (
                src.mode != null && dst.mode == null ||
                src.foreground != null && dst.foreground == null ||
                src.background != null && dst.background == null
        );
    }

    private void stringifyNoReset(@NotNull StringBuilder out) {
        boolean subsequent = false;
        out.append(TerminalStyle.COMMAND_PREFIX);
        if (dst.mode != null && src.mode != dst.mode) {
            out.append(dst.mode.code);
            subsequent = true;
        }
        if (dst.foreground != null && src.foreground != dst.foreground) {
            if (subsequent) {
                out.append(';');
            }
            out.append(dst.foreground.foregroundCode);
            subsequent = true;
        }
        if (dst.background != null && src.background != dst.background) {
            if (subsequent) {
                out.append(';');
            }
            out.append(dst.background.backgroundCode);
        }
        out.append('m');
    }

    public void stringify(@NotNull StringBuilder out) {
        if (isEmpty()) {
            return;
        }
        if (needReset()) {
            out.append(TerminalStyle.RESET_COMMAND);
        }
        stringifyNoReset(out);
    }
}
