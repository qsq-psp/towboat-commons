package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Sequencer;

/**
 * Created on 2026/6/21.
 */
@CodeHistory(date = "2026/6/21")
public class SequencerTransformer implements JsonContextTransformer<Sequencer> {

    public static final SequencerTransformer INSTANCE = new SequencerTransformer();

    @Override
    public void transform(@NotNull Sequencer sequencer, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            MidiDeviceTransformer.transformExposed(sequencer, out);
            out.stringKey("running");
            out.booleanValue(sequencer.isRunning());
            out.stringKey("recording");
            out.booleanValue(sequencer.isRecording());
            out.stringKey("tempoBPM");
            out.numberValue(sequencer.getTempoInBPM());
            out.stringKey("tempoMPQ");
            out.numberValue(sequencer.getTempoInMPQ());
            out.stringKey("tempoFactor");
            out.numberValue(sequencer.getTempoFactor());
            out.stringKey("tickLength");
            out.numberValue(sequencer.getTickLength());
            out.stringKey("tickPosition");
            out.numberValue(sequencer.getTickPosition());
        }
        {
            long length = sequencer.getMicrosecondLength();
            if (length != 0L) {
                out.stringKey("microsecondLength");
                out.numberValue(length);
            }
        }
        {
            Sequencer.SyncMode mode = sequencer.getMasterSyncMode();
            if (mode != null) {
                out.stringKey("masterSyncMode");
                out.stringValue(mode.toString());
            }
        }
        {
            Sequencer.SyncMode mode = sequencer.getSlaveSyncMode();
            if (mode != null) {
                out.stringKey("slaveSyncMode");
                out.stringValue(mode.toString());
            }
        }
        {
            long loopStart = sequencer.getLoopStartPoint();
            if (loopStart != 0L) {
                out.stringKey("loopStart");
                out.numberValue(loopStart);
            }
        }
        {
            long loopEnd = sequencer.getLoopEndPoint();
            if (loopEnd != 0L) {
                out.stringKey("loopEnd");
                out.numberValue(loopEnd);
            }
        }
        {
            int loopCount = sequencer.getLoopCount();
            if (loopCount != 0) {
                out.stringKey("loopCount");
                out.numberValue(loopCount);
            }
        }
        out.closeObject();
    }
}
