package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;

@CodeHistory(date = "2026/6/9")
public class MatcherTransformer implements JsonContextTransformer<Matcher> {

    public static final MatcherTransformer INSTANCE = new MatcherTransformer();

    @Override
    public void transform(@NotNull Matcher matcher, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(PatternTransformer.PATTERN);
            PatternTransformer.INSTANCE.transform(matcher.pattern(), out, context);
            int groupCount = matcher.groupCount();
            out.key("groupCount");
            out.numberValue(groupCount);
            out.key("regionStart");
            out.numberValue(matcher.regionStart());
            out.key("regionEnd");
            out.numberValue(matcher.regionEnd());
            out.key("transparentBounds");
            out.booleanValue(matcher.hasTransparentBounds());
            out.key("anchoringBounds");
            out.booleanValue(matcher.hasAnchoringBounds());
            out.key("hitEnd");
            out.booleanValue(matcher.hitEnd());
            out.key("requireEnd");
            out.booleanValue(matcher.requireEnd());
            try {
                int[] startIndexes = null;
                int[] endIndexes = null;
                for (int groupIndex = 0; groupIndex <= groupCount; groupIndex++) {
                    int startIndex = matcher.start(groupIndex);
                    int endIndex = matcher.end(groupIndex);
                    if (groupCount == 0) {
                        out.key("groups");
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
                out.key("groups");
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
