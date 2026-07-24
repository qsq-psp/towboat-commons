package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.DataLine;

@CodeHistory(date = "2026/5/4")
public class DataLineTransformer implements JsonContextTransformer<DataLine> {

    public static final DataLineTransformer INSTANCE = new DataLineTransformer();

    @Override
    public void transform(@NotNull DataLine dataLine, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(DataFlavorTransformer.CLASS);
            out.stringValue(dataLine.getClass().getName());
            out.key("running");
            out.booleanValue(dataLine.isRunning());
            out.key("active");
            out.booleanValue(dataLine.isActive());
            out.key("format");
            AudioFormatTransformer.INSTANCE.transform(dataLine.getFormat(), out, context);
            out.key("bufferSize");
            out.numberValue(dataLine.getBufferSize());
            out.key("available");
            out.numberValue(dataLine.available());
            out.key("framePosition");
            out.numberValue(dataLine.getLongFramePosition());
            out.key("microsecondPosition");
            out.numberValue(dataLine.getMicrosecondPosition());
            out.key("level");
            out.numberValue(dataLine.getLevel());
        }
        out.closeObject();
    }
}
