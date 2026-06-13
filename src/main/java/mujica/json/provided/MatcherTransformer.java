package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

/**
 * Created on 2026/6/9.
 */
@CodeHistory(date = "2026/6/9")
public class MatcherTransformer implements JsonContextTransformer<Matcher> {

    public static final MatcherTransformer INSTANCE = new MatcherTransformer();

    @Override
    public void transform(@NotNull Matcher in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(PatternTransformer.PATTERN);
            PatternTransformer.INSTANCE.transform(in.pattern(), out, context);
            int groupCount = in.groupCount();
            out.stringKey("groupCount");
            out.numberValue(groupCount);
            out.stringKey("regionStart");
            out.numberValue(in.regionStart());
            out.stringKey("regionEnd");
            out.numberValue(in.regionEnd());
            out.stringKey("transparentBounds");
            out.booleanValue(in.hasTransparentBounds());
            out.stringKey("anchoringBounds");
            out.booleanValue(in.hasAnchoringBounds());
            out.stringKey("hitEnd");
            out.booleanValue(in.hitEnd());
            out.stringKey("requireEnd");
            out.booleanValue(in.requireEnd());
            try {
                int[] startIndexes = null;
                int[] endIndexes = null;
                for (int groupIndex = 0; groupIndex <= groupCount; groupIndex++) {
                    int startIndex = in.start(groupIndex);
                    int endIndex = in.end(groupIndex);
                    if (groupCount == 0) {
                        out.stringKey("groups");
                        out.openArray();
                        {
                            out.openArray();
                            {
                                out.numberValue(startIndex);
                                out.numberValue(endIndex);
                            }
                            out.closeArray();
                        }
                        out.closeArray();
                        return;
                    }
                    if (groupIndex == 0) {
                        startIndexes = new int[groupCount + 1];
                        endIndexes = new int[groupCount + 1];
                    }
                    startIndexes[groupIndex] = startIndex;
                    endIndexes[groupIndex] = endIndex;
                }
                out.stringKey("groups");
                out.openArray();
                for (int groupIndex = 0; groupIndex <= groupCount; groupIndex++) {
                    out.openArray();
                    {
                        out.numberValue(startIndexes[groupIndex]);
                        out.numberValue(endIndexes[groupIndex]);
                    }
                    out.closeArray();
                }
                out.closeArray();
            } catch (IllegalStateException ignore) {}
        }
        out.closeObject();
    }
}
