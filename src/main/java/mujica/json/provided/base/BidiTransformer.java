package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Bidi;

@CodeHistory(date = "2026/6/7")
public class BidiTransformer implements JsonContextTransformer<Bidi> {

    @Override
    public void transform(@NotNull Bidi bidi, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("mixed");
            out.booleanValue(bidi.isMixed());
            out.key("leftToRight");
            out.booleanValue(bidi.isLeftToRight());
            out.key("rightToLeft");
            out.booleanValue(bidi.isRightToLeft());
            out.key("length");
            out.numberValue(bidi.getLength());
            out.key("baseLevel");
            out.numberValue(bidi.getBaseLevel());
        }
        {
            int runCount = bidi.getRunCount();
            if (runCount > 0) {
                out.key("runs");
                out.openArray();
                for (int runIndex = 0; runIndex < runCount; runIndex++) {
                    out.openObject();
                    {
                        out.key("level");
                        out.numberValue(bidi.getRunLevel(runIndex));
                        out.key("start");
                        out.numberValue(bidi.getRunStart(runIndex));
                        out.key("limit");
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
