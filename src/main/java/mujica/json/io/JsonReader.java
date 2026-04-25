package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantComposition;
import mujica.reflect.modifier.ConstantInterface;
import mujica.reflect.modifier.ReferencePage;
import org.intellij.lang.annotations.MagicConstant;

@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "ReaderStates")
@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "ReaderFlags")
@CodeHistory(date = "2025/10/27")
@ReferencePage(title = "JSON for Humans", href = "https://json5.org/")
@ConstantInterface(composition = ConstantComposition.OR)
public interface JsonReader { // built with input and output, used only once

    int FLAG_LINE_COMMENT                       = 0x0001;
    int FLAG_BLOCK_COMMENT                      = 0x0002;
    int FLAG_APOSTROPHE_QUOTE_STRING            = 0x0004;
    int FLAG_GRAVE_ACCENT_QUOTE_STRING          = 0x0008;
    int FLAG_LEADING_COMMA                      = 0x0010;
    int FLAG_TRAILING_COMMA                     = 0x0020;
    int FLAG_PLUS_SIGN_NUMBER                   = 0x0040;
    int FLAG_INFINITY_NAN_EXTENSION             = 0x0080;

    void setFlags(int flags);

    @MagicConstant(flagsFromClass = JsonReader.class)
    int getFlags();
}
