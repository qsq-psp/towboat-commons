package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.VoiceStatus;

/**
 * Created on 2026/6/17.
 */
public class VoiceStatusTransformer implements JsonContextTransformer<VoiceStatus> {

    public static final VoiceStatusTransformer INSTANCE = new VoiceStatusTransformer();

    @Override
    public void transform(@NotNull VoiceStatus in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey("active");
            out.booleanValue(in.active);
            out.stringKey("channel");
            out.numberValue(in.channel);
            out.stringKey("bank");
            out.numberValue(in.bank);
            out.stringKey("program");
            out.numberValue(in.program);
            out.stringKey("note");
            out.numberValue(in.note);
            out.stringKey("volume");
            out.numberValue(in.volume);
        }
        out.closeObject();
    }
}
