package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

/**
 * Created on 2026/6/20.
 */
@CodeHistory(date = "2026/6/20")
public class MidiDeviceTransformer implements JsonContextTransformer<MidiDevice>, JsonStructure {

    public static final MidiDeviceTransformer INSTANCE = new MidiDeviceTransformer();

    static void transformExposed(@NotNull MidiDevice.Info info, @NotNull JsonHandler out) {
        out.stringKey(DataFlavorTransformer.NAME);
        out.stringValue(info.getName());
        out.stringKey("vendor");
        out.stringValue(info.getVendor());
        out.stringKey("description");
        out.stringValue(info.getDescription());
        out.stringKey("version");
        out.stringValue(info.getVersion());
    }

    static void transformExposed(@NotNull MidiDevice device, @NotNull JsonHandler out) {
        transformExposed(device.getDeviceInfo(), out);
        out.stringKey("open");
        out.booleanValue(device.isOpen());
        {
            long position = device.getMicrosecondPosition();
            if (position != -1L) {
                out.stringKey("microsecondPosition");
                out.numberValue(position);
            }
        }
        {
            int maxReceivers = device.getMaxReceivers();
            if (maxReceivers != -1) {
                out.stringKey("maxReceiverCount");
                out.numberValue(maxReceivers);
            }
        }
        {
            int maxTransmitters = device.getMaxTransmitters();
            if (maxTransmitters != -1) {
                out.stringKey("maxTransmitterCount");
                out.numberValue(maxTransmitters);
            }
        }

    }

    @Override
    public void transform(@NotNull MidiDevice in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        transformExposed(in, out);
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        jh.openArray();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            jh.openObject();
            transformExposed(info, jh);
            jh.closeObject();
        }
        jh.closeArray();
    }
}
