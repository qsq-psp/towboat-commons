package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFileFormat;

/**
 * Created on 2026/5/9.
 */
public class AudioFileFormatTransformer implements JsonContextTransformer<AudioFileFormat> {

    public static final AudioFileFormatTransformer INSTANCE = new AudioFileFormatTransformer();

    @Override
    public void transform(@NotNull AudioFileFormat in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("type");
            out.stringValue(in.getType().toString());
            out.stringKey("extension");
            out.stringValue(in.getType().getExtension());
            out.stringKey("byteLength");
            out.numberValue(in.getByteLength());
            out.stringKey("format");
            AudioFormatTransformer.INSTANCE.transform(in.getFormat(), out, context);
            out.stringKey("frameLength");
            out.numberValue(in.getFrameLength());
        }
        out.closeObject();
    }
}
