package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;

@CodeHistory(date = "2026/5/1")
public class AudioFormatTransformer implements JsonContextTransformer<AudioFormat> {

    public static final AudioFormatTransformer INSTANCE = new AudioFormatTransformer();

    @Override
    public void transform(@NotNull AudioFormat audioFormat, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("encoding");
            out.stringValue(audioFormat.getEncoding().toString());
            out.key("sampleRate");
            out.numberValue(audioFormat.getSampleRate());
            out.key("sampleSizeInBits");
            out.numberValue(audioFormat.getSampleSizeInBits());
            out.key("channels");
            out.numberValue(audioFormat.getChannels());
            out.key("frameSize");
            out.numberValue(audioFormat.getFrameSize());
            out.key("frameRate");
            out.numberValue(audioFormat.getFrameRate());
            out.key("bigEndian");
            out.booleanValue(audioFormat.isBigEndian());
        }
        out.closeObject();
    }
}
