package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.MidiChannel;

@CodeHistory(date = "2026/6/16")
public class MidiChannelTransformer implements JsonContextTransformer<MidiChannel> {

    public static final MidiChannelTransformer INSTANCE = new MidiChannelTransformer();

    @Override
    public void transform(@NotNull MidiChannel in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("pressure");
            out.numberValue(in.getChannelPressure());
            out.key("program");
            out.numberValue(in.getProgram());
            out.key("pitchBend");
            out.numberValue(in.getPitchBend());
            out.key("mono");
            out.booleanValue(in.getMono());
            out.key("omni");
            out.booleanValue(in.getOmni());
            out.key("mute");
            out.booleanValue(in.getMute());
            out.key("solo");
            out.booleanValue(in.getSolo());
        }
        out.closeObject();
    }
}
