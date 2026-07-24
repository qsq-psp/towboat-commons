package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFileFormat;

@CodeHistory(date = "2026/5/9")
public class AudioFileFormatTransformer implements JsonContextTransformer<AudioFileFormat> {

    public static final AudioFileFormatTransformer INSTANCE = new AudioFileFormatTransformer();

    @Override
    public void transform(@NotNull AudioFileFormat audioFileFormat, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("type");
            out.stringValue(audioFileFormat.getType().toString());
            out.key("extension");
            out.stringValue(audioFileFormat.getType().getExtension());
            out.key("byteLength");
            out.numberValue(audioFileFormat.getByteLength());
            out.key("format");
            AudioFormatTransformer.INSTANCE.transform(audioFileFormat.getFormat(), out, context);
            out.key("frameLength");
            out.numberValue(audioFileFormat.getFrameLength());
        }
        out.closeObject();
    }
}
