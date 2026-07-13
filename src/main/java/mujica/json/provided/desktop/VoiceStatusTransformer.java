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
            out.key("active");
            out.booleanValue(in.active);
            out.key("channel");
            out.numberValue(in.channel);
            out.key("bank");
            out.numberValue(in.bank);
            out.key("program");
            out.numberValue(in.program);
            out.key("note");
            out.numberValue(in.note);
            out.key("volume");
            out.numberValue(in.volume);
        }
        out.closeObject();
    }
}
