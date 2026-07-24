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
    public void transform(@NotNull MidiChannel midiChannel, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("pressure");
            out.numberValue(midiChannel.getChannelPressure());
            out.key("program");
            out.numberValue(midiChannel.getProgram());
            out.key("pitchBend");
            out.numberValue(midiChannel.getPitchBend());
            out.key("mono");
            out.booleanValue(midiChannel.getMono());
            out.key("omni");
            out.booleanValue(midiChannel.getOmni());
            out.key("mute");
            out.booleanValue(midiChannel.getMute());
            out.key("solo");
            out.booleanValue(midiChannel.getSolo());
        }
        out.closeObject();
    }
}
