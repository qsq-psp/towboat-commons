package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
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
            out.key("encoding");
            out.stringValue(in.getEncoding().toString());
            out.key("sampleRate");
            out.numberValue(in.getSampleRate());
            out.key("sampleSizeInBits");
            out.numberValue(in.getSampleSizeInBits());
            out.key("channels");
            out.numberValue(in.getChannels());
            out.key("frameSize");
            out.numberValue(in.getFrameSize());
            out.key("frameRate");
            out.numberValue(in.getFrameRate());
            out.key("bigEndian");
            out.booleanValue(in.isBigEndian());
        }
        out.closeObject();
    }
}
