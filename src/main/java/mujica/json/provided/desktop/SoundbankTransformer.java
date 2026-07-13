package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Instrument;
import javax.sound.midi.Soundbank;

@CodeHistory(date = "2026/6/19")
public class SoundbankTransformer implements JsonContextTransformer<Soundbank> {

    public static final SoundbankTransformer INSTANCE = new SoundbankTransformer();

    @Override
    public void transform(@NotNull Soundbank in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(DataFlavorTransformer.NAME);
            out.stringValue(in.getName());
            out.key("version");
            out.stringValue(in.getVersion());
            out.key("description");
            out.stringValue(in.getDescription());
        }
        {
            Instrument[] instruments = in.getInstruments();
            if (instruments != null && instruments.length != 0) {
                out.key("instruments");
                out.openArray();
                for (Instrument instrument : instruments) {
                    InstrumentTransformer.INSTANCE.transform(instrument, out, context);
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
