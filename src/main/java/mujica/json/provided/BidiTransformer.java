package mujica.json.provided;

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
    public void transform(@NotNull Bidi in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey("mixed");
            out.booleanValue(in.isMixed());
            out.stringKey("leftToRight");
            out.booleanValue(in.isLeftToRight());
            out.stringKey("rightToLeft");
            out.booleanValue(in.isRightToLeft());
            out.stringKey("length");
            out.numberValue(in.getLength());
            out.stringKey("baseLevel");
            out.numberValue(in.getBaseLevel());
        }
        {
            int runCount = in.getRunCount();
            if (runCount > 0) {
                out.stringKey("runs");
                out.openArray();
                for (int runIndex = 0; runIndex < runCount; runIndex++) {
                    out.openObject();
                    {
                        out.stringKey("level");
                        out.numberValue(in.getRunLevel(runIndex));
                        out.stringKey("start");
                        out.numberValue(in.getRunStart(runIndex));
                        out.stringKey("limit");
                        out.numberValue(in.getRunLimit(runIndex));
                    }
                    out.closeObject();
                }
                out.closeArray();
            }
        }
        out.closeObject();
    }
}
