package mujica.json.io;

import mujica.io.buffer.TerminalStyle;
import mujica.io.buffer.TerminalStyleTransition;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "TerminalScriptStringWriter")
@CodeHistory(date = "2026/4/24")
public class JsonTerminalStringBuilderWriter extends JsonIndentStringBuilderWriter {

    @NotNull
    TerminalStyle stringKeyStyle = TerminalStyle.RESET_STYLE;

    @NotNull
    public TerminalStyle getStringKeyStyle() {
        return stringKeyStyle;
    }

    public void setStringKeyStyle(@NotNull TerminalStyle stringKeyStyle) {
        this.stringKeyStyle = stringKeyStyle;
    }

    @NotNull
    TerminalStyle stringValueStyle = TerminalStyle.RESET_STYLE;

    @NotNull
    public TerminalStyle getStringValueStyle() {
        return stringValueStyle;
    }

    public void setStringValueStyle(@NotNull TerminalStyle stringValueStyle) {
        this.stringValueStyle = stringValueStyle;
    }

    @NotNull
    TerminalStyle numberValueStyle = TerminalStyle.RESET_STYLE;

    @NotNull
    public TerminalStyle getNumberValueStyle() {
        return numberValueStyle;
    }

    public void setNumberValueStyle(@NotNull TerminalStyle numberValueStyle) {
        this.numberValueStyle = numberValueStyle;
    }

    @NotNull
    TerminalStyle booleanValueStyle = TerminalStyle.RESET_STYLE;

    @NotNull
    public TerminalStyle getBooleanValueStyle() {
        return booleanValueStyle;
    }

    public void setBooleanValueStyle(@NotNull TerminalStyle booleanValueStyle) {
        this.booleanValueStyle = booleanValueStyle;
    }

    @NotNull
    TerminalStyle escapeSequenceStyle = TerminalStyle.RESET_STYLE;

    @NotNull
    public TerminalStyle getEscapeSequenceStyle() {
        return escapeSequenceStyle;
    }

    public void setEscapeSequenceStyle(@NotNull TerminalStyle escapeSequenceStyle) {
        this.escapeSequenceStyle = escapeSequenceStyle;
    }

    @NotNull
    TerminalStyleTransition transition = new TerminalStyleTransition();


    public JsonTerminalStringBuilderWriter(@NotNull StringBuilder sb) {
        super(sb);
    }

    public JsonTerminalStringBuilderWriter() {
        super();
    }
}
