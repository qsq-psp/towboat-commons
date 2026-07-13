package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.VoiceStatus;

/**
 * Created on 2026/6/22.
 */
@CodeHistory(date = "2026/6/22")
public class SynthesizerTransformer implements JsonContextTransformer<Synthesizer> {

    public static final SynthesizerTransformer INSTANCE = new SynthesizerTransformer();

    @Override
    public void transform(@NotNull Synthesizer synthesizer, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            MidiDeviceTransformer.transformExposed(synthesizer, out);
            out.key("maxPolyphony");
            out.numberValue(synthesizer.getMaxPolyphony());
            out.key("latency");
            out.numberValue(synthesizer.getLatency());
            out.key("channels");
            out.openArray();
            for (MidiChannel channel : synthesizer.getChannels()) {
                MidiChannelTransformer.INSTANCE.transform(channel, out, context);
            }
            out.closeArray();
            out.key("voiceStatus");
            out.openArray();
            for (VoiceStatus voiceStatus : synthesizer.getVoiceStatus()) {
                VoiceStatusTransformer.INSTANCE.transform(voiceStatus, out, context);
            }
            out.closeArray();
            out.key("defaultSoundBank");
            SoundbankTransformer.INSTANCE.transform(synthesizer.getDefaultSoundbank(), out, context);
            out.key("loadedInstruments");
            for (Instrument instrument : synthesizer.getLoadedInstruments()) {
                InstrumentTransformer.INSTANCE.transform(instrument, out, context);
            }
        }
        out.closeObject();
    }
}
