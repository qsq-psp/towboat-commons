package mujica.text.pattern;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.regex.MatchResult;

@CodeHistory(date = "2022/12/24", project = "Ultramarine", name = "AsyncMatcher.CumulativeMatchResult")
@CodeHistory(date = "2025/3/30")
public class OffsetMatchResult implements MatchResult {
    
    @NotNull
    final MatchResult relative;
    
    int offset;

    public OffsetMatchResult(@NotNull MatchResult relative, int offset) {
        super();
        this.relative = relative;
        this.offset = offset;
    }

    @Override
    public int start() {
        return offset + relative.start();
    }

    @Override
    public int start(int group) {
        return offset + relative.start(group);
    }

    @Override
    public int end() {
        return offset + relative.end();
    }

    @Override
    public int end(int group) {
        return offset + relative.end(group);
    }

    @Override
    public String group() {
        return relative.group();
    }

    @Override
    public String group(int group) {
        return relative.group(group);
    }

    @Override
    public int groupCount() {
        return relative.groupCount();
    }

    @Override
    public String toString() {
        return "OffsetMatchResult[relative = " + relative + ", offset = " + offset + "]";
    }
}
