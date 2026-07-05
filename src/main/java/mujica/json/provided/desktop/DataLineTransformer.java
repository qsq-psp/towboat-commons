package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.DataLine;

@CodeHistory(date = "2026/5/4")
public class DataLineTransformer implements JsonContextTransformer<DataLine> {

    public static final DataLineTransformer INSTANCE = new DataLineTransformer();

    @Override
    public void transform(@NotNull DataLine in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(DataFlavorTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey("running");
            out.booleanValue(in.isRunning());
            out.stringKey("active");
            out.booleanValue(in.isActive());
            out.stringKey("format");
            AudioFormatTransformer.INSTANCE.transform(in.getFormat(), out, context);
            out.stringKey("bufferSize");
            out.numberValue(in.getBufferSize());
            out.stringKey("available");
            out.numberValue(in.available());
            out.stringKey("framePosition");
            out.numberValue(in.getLongFramePosition());
            out.stringKey("microsecondPosition");
            out.numberValue(in.getMicrosecondPosition());
            out.stringKey("level");
            out.numberValue(in.getLevel());
        }
        out.closeObject();
    }
}
