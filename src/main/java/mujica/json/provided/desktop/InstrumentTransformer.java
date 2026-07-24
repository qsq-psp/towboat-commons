package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;

@CodeHistory(date = "2026/6/18")
public class InstrumentTransformer implements JsonContextTransformer<Instrument> {

    public static final InstrumentTransformer INSTANCE = new InstrumentTransformer();

    @Override
    public void transform(@NotNull Instrument instrument, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(DataFlavorTransformer.CLASS);
            out.stringValue(instrument.getClass().getName());
        }
        {
            String name = instrument.getName();
            if (name != null) {
                out.key(DataFlavorTransformer.NAME);
                out.stringValue(name);
            }
        }
        {
            Soundbank soundbank = instrument.getSoundbank();
            if (soundbank != null) {
                out.key("soundbank");
                out.stringValue(soundbank.getName());
            }
        }
        {
            Class<?> dataClass = instrument.getDataClass();
            if (dataClass != null) {
                out.key("dataClass");
                out.stringValue(dataClass.getName());
            }
        }
        {
            Patch patch = instrument.getPatch();
            if (patch != null) {
                out.key("patch");
                out.openObject();
                {
                    out.key("bank");
                    out.numberValue(patch.getBank());
                    out.key("program");
                    out.numberValue(patch.getProgram());
                }
                out.closeObject();
            }
        }
        out.closeObject();
    }
}
