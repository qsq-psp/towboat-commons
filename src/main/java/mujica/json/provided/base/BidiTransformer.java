package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Bidi;

/**
 * Created on 2026/6/7.
 */
@CodeHistory(date = "2026/6/7")
public class BidiTransformer implements JsonContextTransformer<Bidi> {

    @Override
    public void transform(@NotNull Bidi bidi, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey("mixed");
            out.booleanValue(bidi.isMixed());
            out.stringKey("leftToRight");
            out.booleanValue(bidi.isLeftToRight());
            out.stringKey("rightToLeft");
            out.booleanValue(bidi.isRightToLeft());
            out.stringKey("length");
            out.numberValue(bidi.getLength());
            out.stringKey("baseLevel");
            out.numberValue(bidi.getBaseLevel());
        }
        {
            int runCount = bidi.getRunCount();
            if (runCount > 0) {
                out.stringKey("runs");
                out.openArray();
                for (int runIndex = 0; runIndex < runCount; runIndex++) {
                    out.openObject();
                    {
                        out.stringKey("level");
                        out.numberValue(bidi.getRunLevel(runIndex));
                        out.stringKey("start");
                        out.numberValue(bidi.getRunStart(runIndex));
                        out.stringKey("limit");
                        out.numberValue(bidi.getRunLimit(runIndex));
                    }
                    out.closeObject();
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
