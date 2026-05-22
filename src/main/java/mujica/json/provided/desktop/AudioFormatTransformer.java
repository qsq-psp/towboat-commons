package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;

/**
 * Created on 2026/5/1.
 */
@CodeHistory(date = "2026/5/1")
public class AudioFormatTransformer implements JsonContextTransformer<AudioFormat> {

    public static final AudioFormatTransformer INSTANCE = new AudioFormatTransformer();

    @Override
    public void transform(AudioFormat in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("encoding");
            out.stringValue(in.getEncoding().toString());
            out.stringKey("sampleRate");
            out.numberValue(in.getSampleRate());
            out.stringKey("sampleSizeInBits");
            out.numberValue(in.getSampleSizeInBits());
            out.stringKey("channels");
            out.numberValue(in.getChannels());
            out.stringKey("frameSize");
            out.numberValue(in.getFrameSize());
            out.stringKey("frameRate");
            out.numberValue(in.getFrameRate());
            out.stringKey("bigEndian");
            out.booleanValue(in.isBigEndian());
        }
        out.closeObject();
    }
}
