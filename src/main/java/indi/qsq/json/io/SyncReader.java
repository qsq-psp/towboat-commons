package indi.qsq.json.io;

import indi.qsq.json.api.JsonConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Created in infrastructure on 2022/1/9, named SimpleReader.
 * Created on 2022/6/5, named Reader.
 * Renamed on 2023/4/29.
 */
public interface SyncReader extends ReaderFlags {

    @Override
    void config(int config);

    void read(@NotNull JsonConsumer jc);

    default void stringifyNeighbors(StringBuilder sb, int before, int after) {
        sb.append("\"\""); // not supported
    }

    default void stringifyNeighbors(StringBuilder sb) {
        stringifyNeighbors(sb, 12, 12);
    }

    default String neighborsToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyNeighbors(sb);
        return sb.toString();
    }
}
