package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.VoiceStatus;

@CodeHistory(date = "2026/6/17")
public class VoiceStatusTransformer implements JsonContextTransformer<VoiceStatus> {

    public static final VoiceStatusTransformer INSTANCE = new VoiceStatusTransformer();

    @Override
    public void transform(@NotNull VoiceStatus voiceStatus, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("active");
            out.booleanValue(voiceStatus.active);
            out.key("channel");
            out.numberValue(voiceStatus.channel);
            out.key("bank");
            out.numberValue(voiceStatus.bank);
            out.key("program");
            out.numberValue(voiceStatus.program);
            out.key("note");
            out.numberValue(voiceStatus.note);
            out.key("volume");
            out.numberValue(voiceStatus.volume);
        }
        out.closeObject();
    }
}
