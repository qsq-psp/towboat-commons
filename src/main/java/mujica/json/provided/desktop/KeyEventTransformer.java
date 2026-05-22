package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;

/**
 * Created on 2026/4/29.
 */
public class KeyEventTransformer implements JsonContextTransformer<KeyEvent> {

    public static final KeyEventTransformer INSTANCE = new KeyEventTransformer();

    static final FastString KEY_CODE = new FastString("keyCode");

    static final FastString KEY_CHAR = new FastString("keyChar");

    static final FastString KEY_LOCATION = new FastString("keyLocation");

    @Override
    public void transform(@NotNull KeyEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            InputEventTransformer.transformExposed(in, out);
            out.stringKey(KEY_CODE);
            out.numberValue(in.getKeyCode());
            out.stringKey(KEY_CHAR);
            out.stringValue(String.valueOf(in.getKeyChar()));
            int keyLocation = in.getKeyLocation();
            out.stringKey(KEY_LOCATION);
            switch (keyLocation) {
                case KeyEvent.KEY_LOCATION_UNKNOWN:
                    out.stringValue("unknown");
                    break;
                case KeyEvent.KEY_LOCATION_STANDARD:
                    out.stringValue("standard");
                    break;
                case KeyEvent.KEY_LOCATION_LEFT:
                    out.stringValue("left");
                    break;
                case KeyEvent.KEY_LOCATION_RIGHT:
                    out.stringValue("right");
                    break;
                case KeyEvent.KEY_LOCATION_NUMPAD:
                    out.stringValue("numpad");
                    break;
                default:
                    out.numberValue(keyLocation);
                    break;
            }
        }
        out.closeObject();
    }
}
